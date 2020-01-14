package net.frozendevelopment.frozenhue.modules.lights.views

import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import net.frozendevelopment.cache.models.LightModel
import net.frozendevelopment.frozenhue.R

class LightCellItem(private val light: LightModel, private val toggleLight: (LightModel) -> Unit) : Item() {

    override fun getLayout(): Int = R.layout.light_list_cell

    override fun bind(viewHolder: GroupieViewHolder, position: Int) = with(viewHolder) {
//        light_cell_title_text.text = light.name ?: "Unknown"
//        light_cell_switch.isChecked = light.on // .setChecked(light.on, false)
//        light_cell_switch.isEnabled = light.reachable
//        light_cell_fader.isEnabled = light.reachable
//        light_cell_expander.isEnabled = light.reachable
//        light_cell_helper_text.isVisible = !light.reachable
//        light_cell_fader.setBackgroundColor(XYZtoRGB(light.x, light.y, light.z).toInt())
    }

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        val otherModel = other as? LightCellItem ?: return false

        if (viewType != otherModel.viewType) {
            return false
        }

        return this.light.id == otherModel.light.id
    }

    override fun hasSameContentAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is LightCellItem) return false

        return this.light == other.light
    }
}