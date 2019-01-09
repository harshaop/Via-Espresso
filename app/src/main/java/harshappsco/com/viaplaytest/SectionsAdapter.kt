package harshappsco.com.viaplaytest

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.sections_row.view.*

class SectionsAdapter(private val sections: Array<Sections>) : RecyclerView.Adapter<CustomViewHolder>() {

    //number of items in sections
    override fun getItemCount(): Int {
        return sections.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.sections_row, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        holder.view.tv_section_row_title.text = sections[position].title
        holder.title = sections[position].title
        holder.sectionDescription = sections[position].name
        holder.href = sections[position].href
        holder.id = sections[position].id

    }
}

class CustomViewHolder(val view: View, var title: String ="Title Bar"  , var sectionDescription:String = "Section Description", var href :String ="", var id:String = "") : RecyclerView.ViewHolder(view) {
    companion object {
        const val SECTIONS_TITLE_KEY = "SECTIONS_TITLE"
        const val SECTION_DESCRIPTION_KEY = "DESCRIPTOIN_KEY"
        const val SECTION_HREF_KEY ="HREF_KEY"
        const val SECTION_ID_KEY ="ID_KEY"
    }

    init {
        view.setOnClickListener {
            val intent = Intent(view.context, SectionActivity::class.java)

            intent.putExtra(SECTIONS_TITLE_KEY, title)
            intent.putExtra(SECTION_DESCRIPTION_KEY, sectionDescription)
            intent.putExtra(SECTION_HREF_KEY,href)
            intent.putExtra(SECTION_ID_KEY,id)
            view.context.startActivity(intent)
        }
    }
}