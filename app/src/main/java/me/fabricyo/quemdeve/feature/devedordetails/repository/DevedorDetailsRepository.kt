package me.fabricyo.quemdeve.feature.devedordetails.repository

import me.fabricyo.quemdeve.bases.BaseRepository
import me.fabricyo.quemdeve.data.Devedor
import me.fabricyo.quemdeve.helpers.HelperDB
import java.lang.Exception
import java.sql.SQLDataException

class DevedorDetailsRepository(
    helperDB: HelperDB?
) : BaseRepository(helperDB){

    fun getDevedor(
        idDevedor: Int,
        onSucesso: (Devedor) -> Unit,
        onError: (Exception) -> Unit
    ){
        try {
            val db = readableDatabase
            val lista = mutableListOf<Devedor>()
            val where = "${HelperDB.COLUMNS_ID} = ?"
            val args = arrayOf("${idDevedor}")
            val cursor = db?.query(HelperDB.TABLE_NAME, null, where, args, null,
                null, null)
            if(cursor == null){
                db?.close()
                onError(SQLDataException("Não foi possível acessar o banco de dados"))
                return
            }
            while (cursor.moveToNext()){
                val devedor = Devedor(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(HelperDB.COLUMNS_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(HelperDB.COLUMNS_NOME)),
                    value = cursor.getInt(cursor.getColumnIndexOrThrow(HelperDB.COLUMNS_VALUE)),
                    lastValue = cursor.getInt(cursor.getColumnIndexOrThrow(HelperDB.COLUMNS_LAST_VALUE)),
                    lastMod = cursor.getString(cursor.getColumnIndexOrThrow(HelperDB.COLUMNS_LAST_MOD)),
                )
                lista.add(devedor)
            }
            cursor.close()
            db.close()
            val contato = lista.getOrNull(0)
            if(contato != null) onSucesso(contato)
            else onError(SQLDataException("Não conseguimos resgatar o contato"))
        }catch (ex: Exception){
            ex.printStackTrace()
            onError(SQLDataException("Não foi possível completar sua solicitação!"))
        }
    }
}