package my.example.notes.db

import android.provider.BaseColumns

object MyDbNameClass : BaseColumns {

    const val TABLE_NAME = "my_notes"
    const val COLUMN_ID = "id"
    const val COLUMN_NAME_TITLE = "title"
    const val COLUMN_NAME_DESCRIPTION = "description"
    const val COLUMN_NAME_DATE = "date_of_death"
    const val COLUMN_STATUS = "status"

    const val DATABASE_VERSION = 2
    const val DATABASE_NAME = "Notes.db"

    const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
            "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "$COLUMN_NAME_TITLE TEXT," +
            "$COLUMN_NAME_DESCRIPTION TEXT," +
            "$COLUMN_NAME_DATE TEXT," +
            "$COLUMN_STATUS TEXT)"

    const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"

}