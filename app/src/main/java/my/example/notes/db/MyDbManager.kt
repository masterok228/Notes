package my.example.notes.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

import kotlin.collections.ArrayList

class MyDbManager(context: Context) {

    private val myDbHelper = MyDbHelper(context)
    private var db: SQLiteDatabase? = null


    fun openDb() {
        db = myDbHelper.writableDatabase //открыта для действий
    }

    fun insertToDb(title: String?, description: String?, date: String?, status: String?) {

        val values = ContentValues().apply {

            put(MyDbNameClass.COLUMN_NAME_TITLE, title)
            put(MyDbNameClass.COLUMN_NAME_DESCRIPTION, description)
            put(MyDbNameClass.COLUMN_NAME_DATE, date)
            put(MyDbNameClass.COLUMN_STATUS, status)
        }
        db?.insert(MyDbNameClass.TABLE_NAME, null, values)
    }

    fun updateToDb(
        id: String,
        title: String?,
        description: String?,
        date: String?,
        status: String?,
    ) {
        val values = ContentValues().apply {

            db = myDbHelper.writableDatabase
            put(MyDbNameClass.COLUMN_NAME_TITLE, title)
            put(MyDbNameClass.COLUMN_NAME_DESCRIPTION, description)
            put(MyDbNameClass.COLUMN_NAME_DATE, date)
            put(MyDbNameClass.COLUMN_STATUS, status)
        }
        db?.update(MyDbNameClass.TABLE_NAME, values, "id = ?", arrayOf(id))
    }

    fun deleteData(id: String){

        db = myDbHelper.writableDatabase
        db?.delete(
            MyDbNameClass.TABLE_NAME,
            "id = ?",
            arrayOf(id))
    }

    fun readDbData(): List<ListNotes> {

        val dataList = ArrayList<ListNotes>()
        val cursor = db?.query(MyDbNameClass.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            MyDbNameClass.COLUMN_NAME_DATE
        )
        with(cursor) {
            while (this?.moveToNext()!!) { //Ставим !! чтобы наш курсор не был null и только в том случае двигаться к следующему элементу
                val dateID =
                    cursor?.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_ID))
                val dataTitle =
                    cursor?.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_TITLE))  //будет выдавать название по индексу
                val dataDesc =
                    cursor?.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_DESCRIPTION))
                val dataDate =
                    cursor?.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_DATE))
                val dataStatus =
                    cursor?.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_STATUS))

                val note = ListNotes()
                note.id = dateID.toString()
                note.title = dataTitle.toString()
                note.desc = dataDesc.toString()
                note.date = dataDate.toString()
                note.status = dataStatus.toString()

                dataList.add(note)
            }
        }
        cursor?.close()
        return sortList(dataList)
    }

    fun closeDb() {
        myDbHelper.close()
    }

    private fun sortList(list:ArrayList<ListNotes>) : List<ListNotes> {

        return list.sortedWith(compareBy(ListNotes::status))

    }

}