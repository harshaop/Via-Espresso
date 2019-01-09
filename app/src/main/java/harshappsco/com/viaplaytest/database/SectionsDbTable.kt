package harshappsco.com.viaplaytest.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import harshappsco.com.viaplaytest.Sections
import harshappsco.com.viaplaytest.TitleDescription

class SectionsDbTable(context: Context) {
    private val TAG = SectionsDbTable::class.java.simpleName
    private val dbHelper = SectionsValuesDb(context)

    fun storeSections(sections: Sections) {
        val db = dbHelper.writableDatabase
        val values = ContentValues()

        with(values) {
            put(SectionsValues.SECTION_TITLE, sections.title)
            put(SectionsValues.SECTION_DESCRIPTION, sections.name)
            put(SectionsValues.SECTION_HREF, sections.href)
            put(SectionsValues.SECTION_ID, sections.id)
        }
        val id = db.transaction {
            insert(SectionsValues.TABLE_NAME, null, values)
        }
        Log.d(TAG, "Stored sections list to the DB Table")
        return id
    }

    fun readSections(): List<Sections> {
        val sectionsList = mutableListOf<Sections>()
        val columns = arrayOf(
            SectionsValues.TABLE_ID,
            SectionsValues.SECTION_TITLE,
            SectionsValues.SECTION_DESCRIPTION,
            SectionsValues.SECTION_HREF,
            SectionsValues.SECTION_ID
        )

        val order = "${SectionsValues.TABLE_ID} ASC"
        val db = dbHelper.readableDatabase


        val cursor = db.query(SectionsValues.TABLE_NAME, columns, null, null, null, null, order)

        while (cursor.moveToNext()) {
            val title = cursor.getString(cursor.getColumnIndex(SectionsValues.SECTION_TITLE))
            val desc = cursor.getString(cursor.getColumnIndex(SectionsValues.SECTION_DESCRIPTION))
            val href = cursor.getString(cursor.getColumnIndex(SectionsValues.SECTION_HREF))
            val id = cursor.getString(cursor.getColumnIndex(SectionsValues.SECTION_ID))
            sectionsList.add(Sections(title, desc, href, id))
        }
        cursor.close()


        return sectionsList
    }

    fun deleteTableSections() {
        val db = dbHelper.writableDatabase
        db.delete(SectionsValues.TABLE_NAME, null, null)
        db.close()
    }

    fun storeSectionEntry(sectionEntry: TitleDescription) {
        val db = dbHelper.writableDatabase
        val values = ContentValues()

        with(values) {
            put(SectionEntry.SECTION_TITLE, sectionEntry.title)
            put(SectionEntry.SECTION_DESCRIPTION, sectionEntry.description)
            put(SectionEntry.SECTION_ID, sectionEntry.sectionId)
        }
        val id = db.transaction {
            insert(SectionEntry.TABLE_NAME, null, values)
        }
        Log.d(TAG + " SectionEntry", "Stored section Entried list to the DB Table")
        return id
    }

    fun readSectionEntry(selection: String? = null, selectionArgs: Array<String>? = null): TitleDescription {
        var tD = TitleDescription("", "", "")
        val columns = arrayOf(
            SectionEntry.SECTION_ID,
            SectionEntry.SECTION_TITLE,
            SectionEntry.SECTION_DESCRIPTION
        )

        val order = "${SectionEntry.TABLE_ID} ASC"
        val db = dbHelper.readableDatabase


        val cursor = db.query(SectionEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, order)

        while (cursor.moveToNext()) {
            val title = cursor.getString(cursor.getColumnIndex(SectionEntry.SECTION_TITLE))
            val desc = cursor.getString(cursor.getColumnIndex(SectionEntry.SECTION_DESCRIPTION))
            val id = cursor.getString(cursor.getColumnIndex(SectionEntry.SECTION_ID))
            tD = (TitleDescription(title, desc, id))
        }
        cursor.close()

        return tD
    }

    fun deleteTableSectionEntry(whereClause: String? = null, whereArgs: Array<String>? = null) {
        val db = dbHelper.writableDatabase
        db.delete(SectionEntry.TABLE_NAME, whereClause, whereArgs)
        db.close()
    }
}

private inline fun <T> SQLiteDatabase.transaction(function: SQLiteDatabase.() -> T) {
    beginTransaction()
    val result = try {
        function()
        setTransactionSuccessful()
    } finally {
        endTransaction()
    }
    close()
    return result
}