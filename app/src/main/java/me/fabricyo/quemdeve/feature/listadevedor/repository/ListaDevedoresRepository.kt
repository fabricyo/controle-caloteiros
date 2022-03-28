package me.fabricyo.quemdeve.feature.listadevedor.repository

import me.fabricyo.quemdeve.bases.BaseRepository
import me.fabricyo.quemdeve.data.Devedor
import me.fabricyo.quemdeve.helpers.HelperDB
import me.fabricyo.quemdeve.helpers.HelperDB.Companion.COLUMNS_ID
import me.fabricyo.quemdeve.helpers.HelperDB.Companion.COLUMNS_LAST_MOD
import me.fabricyo.quemdeve.helpers.HelperDB.Companion.COLUMNS_LAST_VALUE
import me.fabricyo.quemdeve.helpers.HelperDB.Companion.COLUMNS_NOME
import me.fabricyo.quemdeve.helpers.HelperDB.Companion.COLUMNS_VALUE
import me.fabricyo.quemdeve.helpers.HelperDB.Companion.TABLE_NAME
import java.sql.SQLDataException

class ListaDevedoresRepository(
    var helperDBpar: HelperDB? = null
) : BaseRepository(helperDBpar) {

    fun requestListaDevedores(
        busca: String,
        onSucesso: ((List<Devedor>) -> Unit),
        onError: ((Exception) -> Unit)
    ) {
        try {

            val db = readableDatabase
            val lista = mutableListOf<Devedor>()
            val where = "$COLUMNS_NOME LIKE ?"
            val args = arrayOf("%$busca%")
            val cursor = db?.query(
                TABLE_NAME, null, where, args, null,
                null, null
            )
            if (cursor == null) {
                db?.close()
                onError(SQLDataException("Não foi possível fazer a query"))
                return
            }
            while (cursor.moveToNext()) {
                val devedor = Devedor(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMNS_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMNS_NOME)),
                    value = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMNS_VALUE)),
                    lastValue = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMNS_LAST_VALUE)),
                    lastMod = cursor.getString(cursor.getColumnIndexOrThrow(COLUMNS_LAST_MOD)),
                )
                lista.add(devedor)
            }
            db.close()
            onSucesso.invoke(lista)
        } catch (ex: Exception) {
            onError.invoke(ex)
        }
    }

    fun updateDevedor(
        devedor: Devedor,
        onSucesso: () -> Unit,
        onError: (java.lang.Exception) -> Unit
    ){
        try{
            val db = writeableDatabase
            if (db == null) onError(SQLDataException("Não foi possível se conectar ao banco de dados"))
            val sql = "UPDATE ${HelperDB.TABLE_NAME} SET " +
                    "${HelperDB.COLUMNS_NOME} = ?, ${HelperDB.COLUMNS_VALUE} = ?," +
                    " ${HelperDB.COLUMNS_LAST_VALUE} = ?, ${HelperDB.COLUMNS_LAST_MOD} = ? " +
                    "WHERE ${HelperDB.COLUMNS_ID} = ? "
            val arg = arrayOf(devedor.name, devedor.value, devedor.lastValue, devedor.lastMod, devedor.id)
            db?.execSQL(sql, arg)
            db?.close()
            onSucesso()
        }catch (ex: java.lang.Exception){
            ex.printStackTrace()
            onError(SQLDataException("Não foi possível completar sua solicitação!"))
        }
    }

}