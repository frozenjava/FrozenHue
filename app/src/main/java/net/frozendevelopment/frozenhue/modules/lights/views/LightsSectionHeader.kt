package net.frozendevelopment.frozenhue.modules.lights.views

import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.lights_list_section_header.view.*
import net.frozendevelopment.frozenhue.R
import net.frozendevelopment.frozenhue.views.sticky.StickyHeaderType

class LightsSectionHeader(private val title: String) : Item(), StickyHeaderType {

    override fun getLayout(): Int = R.layout.lights_list_section_header

    override fun bind(viewHolder: GroupieViewHolder, position: Int) = with(viewHolder.itemView) {
        light_section_title.text = title
    }
}