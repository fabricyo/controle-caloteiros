package me.fabricyo.quemdeve.helpers

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class HelperDB(
    context: Context?,
) : SQLiteOpenHelper(context, NOME_BANCO, null, VERSION_ATUAL) {

    companion object{
        private val NOME_BANCO = "devedor.db"
        private val VERSION_ATUAL = 1
        val TABLE_NAME = "devedor"
        val COLUMNS_ID = "id"
        val COLUMNS_NOME = "name"
        val COLUMNS_VALUE = "value"
        val COLUMNS_LAST_VALUE = "last_value"
        val COLUMNS_LAST_MOD = "last_mod"
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ("+
                "$COLUMNS_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "$COLUMNS_NOME TEXT NOT NULL," +
                "$COLUMNS_VALUE INTEGER NOT NULL," +
                "$COLUMNS_LAST_VALUE INTEGER NOT NULL," +
                "$COLUMNS_LAST_MOD TEXT NOT NULL" +
                ")"
    }



    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        if(p1 != p2){
            db?.execSQL(DROP_TABLE)
            onCreate(db,)
        }
    }

//    fun buscarDevedores(buscar: String, isBuscaPorID: Boolean = false) : List<Devedor>{
//        val db = readableDatabase ?: return mutableListOf()
//        val lista = mutableListOf<Devedor>()
//        var where: String? = null
//        var args: Array<String>
//        if(isBuscaPorID){
//            where = "$COLUMNS_ID = ?"
//            args = arrayOf("$buscar")
//        }else{
//            where = "$COLUMNS_NOME LIKE ?"
//            args = arrayOf("%$buscar%")
//        }
//
////        val sql = "SELECT * FROM $TABLE_NAME WHERE $COLUMNS_NOME LIKE ?"
////        var cursor = db.rawQuery(sql, arrayOf(buscaSQL))
//        val cursor = db.query(TABLE_NAME, null, where, args,null,
//            null, null)
//        if (cursor == null){
//            db.close()
//            return mutableListOf()
//        }
//        while(cursor.moveToNext()){
//            val devedor = Devedor(
//                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMNS_ID)),
//                name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMNS_NOME)),
//                value = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMNS_VALUE)),
//                lastValue = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMNS_LAST_VALUE)),
//                lastMod = cursor.getString(cursor.getColumnIndexOrThrow(COLUMNS_LAST_MOD)),
//            )
//            lista.add(devedor)
//        }
//        cursor.close()
//        db.close()
//        return lista
//    }
//
//    fun salvarDevedor(devedor: Devedor){
//        val db = writableDatabase ?: return
////        val sql = "INSERT INTO $TABLE_NAME " +
////                "($COLUMNS_NOME, $COLUMNS_VALUE, $COLUMNS_LAST_VALUE $COLUMNS_LAST_MOD) VALUES" +
////                "(?,?,?,?)"
////        val array = arrayOf(devedor.name, devedor.value, devedor.lastValue, devedor.lastMod)
////        db.execSQL(sql, array)
//        val content = ContentValues()
//        content.put(COLUMNS_NOME, devedor.name)
//        content.put(COLUMNS_VALUE, devedor.value)
//        content.put(COLUMNS_LAST_VALUE, devedor.lastValue)
//        content.put(COLUMNS_LAST_MOD, devedor.lastMod)
//        db.insert(TABLE_NAME, null, content)
//        db.close()
//    }
//
//    fun deletarDevedor(id: Int){
//        val db = writableDatabase ?: return
//        val where = "id = ?"
//        val arg = arrayOf("$id")
//        db.delete(TABLE_NAME, where, arg)
//        db.close()
//    }
//
//    fun updateDevedor(devedor: Devedor){
//        val db = writableDatabase ?: return
////        val content = ContentValues()
////        content.put(COLUMNS_NOME, devedor.name)
////        content.put(COLUMNS_VALUE, devedor.value)
////        content.put(COLUMNS_LAST_VALUE, devedor.value)
////        content.put(COLUMNS_LAST_MOD, devedor.lastMod)
////        val where = "id = ?"
////        val arg = arrayOf("${devedor.id}")
////        db.update(TABLE_NAME, content, where, arg)
//        val sql = "UPDATE $TABLE_NAME SET " +
//                "$COLUMNS_NOME = ?, $COLUMNS_VALUE = ?, $COLUMNS_LAST_VALUE = ?, $COLUMNS_LAST_MOD = ? " +
//                "WHERE $COLUMNS_ID = ? "
//        val arg = arrayOf(devedor.name, devedor.value, devedor.lastValue, devedor.lastMod, devedor.id)
//        db.execSQL(sql, arg)
//        db.close()
//    }

}