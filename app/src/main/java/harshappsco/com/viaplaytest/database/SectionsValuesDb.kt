package harshappsco.com.viaplaytest.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SectionsValuesDb(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME_SECTIONS, null, DATABASE_VERSION_SECTIONS) {
    private val SQL_CREATE_ENTRIES_SECTIONSVALUES = "CREATE TABLE ${SectionsValues.TABLE_NAME} (" +
            "${SectionsValues.TABLE_ID} INTEGER PRIMARY KEY," +
            "${SectionsValues.SECTION_TITLE}," +
            "${SectionsValues.SECTION_DESCRIPTION}," +
            "${SectionsValues.SECTION_HREF}," +
            "${SectionsValues.SECTION_ID} TEXT UNIQUE" +
            ")"

    private val SQL_CREATE_ENTRIES_SECTION = "CREATE TABLE ${SectionEntry.TABLE_NAME} (" +
            "${SectionEntry.TABLE_ID} INTEGER PRIMARY KEY," +
            "${SectionEntry.SECTION_TITLE}," +
            "${SectionEntry.SECTION_DESCRIPTION}," +
            "${SectionEntry.SECTION_ID} TEXT UNIQUE" +
            ")"

    private val SQL_DELETE_ENTRY_SECTIONSVALUES = "DROP TABLE IF EXISTS ${SectionsValues.TABLE_NAME}"
    private val SQL_DELETE_ENTRY_SECTION = "DROP TABLE IF EXISTS ${SectionEntry.TABLE_NAME}"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES_SECTIONSVALUES)
        db.execSQL(SQL_CREATE_ENTRIES_SECTION)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRY_SECTIONSVALUES)
        db.execSQL(SQL_DELETE_ENTRY_SECTION)
        onCreate(db)
    }
}