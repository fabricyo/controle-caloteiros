package me.fabricyo.quemdeve.feature.devedor.repository

import android.content.ContentValues
import me.fabricyo.quemdeve.bases.BaseRepository
import me.fabricyo.quemdeve.data.Devedor
import me.fabricyo.quemdeve.helpers.HelperDB
import java.lang.Exception
import java.sql.SQLDataException

class DevedorRepository(
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

    fun saveDeveodor(
        devedor: Devedor,
        onSucesso: () -> Unit,
        onError: (Exception) -> Unit
    ){
        try {
            val db = writeableDatabase
            if (db == null) onError(SQLDataException("Não foi possível se conectar ao banco de dados"))
            val content = ContentValues()
            content.put(HelperDB.COLUMNS_NOME, devedor.name)
            content.put(HelperDB.COLUMNS_VALUE, devedor.value)
            content.put(HelperDB.COLUMNS_LAST_VALUE, devedor.lastValue)
            content.put(HelperDB.COLUMNS_LAST_MOD, devedor.lastMod)
            db?.insert(HelperDB.TABLE_NAME, null, content)
            db?.close()
            onSucesso()
        }catch (ex: Exception){
            ex.printStackTrace()
            onError(SQLDataException("Não foi possível completar sua solicitação!"))
        }

    }

    fun updateDevedor(
        devedor: Devedor,
        onSucesso: () -> Unit,
        onError: (Exception) -> Unit
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
        }catch (ex: Exception){
            ex.printStackTrace()
            onError(SQLDataException("Não foi possível completar sua solicitação!"))
        }
    }

    fun deleteDevedor(
        idDevedor: Int,
        onSucesso: () -> Unit,
        onError: (Exception) -> Unit
    ){
        try {
            val db = writeableDatabase
            if (db == null) onError(SQLDataException("Bão foi possível se conectar ao base de dados"))
            val sql = "DELETE FROM ${HelperDB.TABLE_NAME} WHERE ${HelperDB.COLUMNS_ID} = ?"
            val arg = arrayOf("$idDevedor")
            db?.execSQL(sql, arg)
            db?.close()
            onSucesso()
        }catch (ex: Exception){
            ex.printStackTrace()
            onError(SQLDataException("Não foi possível completar sua solicitação!"))
        }
    }


}