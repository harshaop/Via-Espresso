package harshappsco.com.viaplaytest.database

import android.provider.BaseColumns

const val DATABASE_NAME_SECTIONS = "sections.db"
const val DATABASE_VERSION_SECTIONS = 10

object SectionsValues : BaseColumns{
    const val TABLE_NAME = "sections"
    const val TABLE_ID = "_ID"
    const val SECTION_TITLE = "title"
    const val SECTION_DESCRIPTION = "description"
    const val SECTION_HREF = "href"
    const val SECTION_ID = "id"
}

const val DATABASE_NAME_SECTION = "sectionTitleDescription.db"
const val DATABASE_VERSION_SECTION = 10

object SectionEntry : BaseColumns{
    const val TABLE_NAME = "sectionTitleDescription"
    const val TABLE_ID = "_ID"
    const val SECTION_TITLE = "title"
    const val SECTION_DESCRIPTION = "description"
    const val SECTION_ID = "id"
}