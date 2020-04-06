package net.frozendevelopment.frozenhue.modules.lights.views

import androidx.core.view.isVisible
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import net.frozendevelopment.frozenhue.R
import net.frozendevelopment.frozenhue.modules.lights.LightState
import kotlinx.android.synthetic.main.light_cell.view.*

class LightCellItem(private val light: LightState, private val toggleLight: (LightState) -> Unit) : Item() {

    override fun getLayout(): Int = R.layout.light_cell

    override fun bind(viewHolder: GroupieViewHolder, position: Int) = with(viewHolder.itemView) {
        light_list_cell_title_text.text = light.name
        light_cell_switch.isChecked = light.on // .setChecked(light.on, false)
        light_cell_switch.isEnabled = light.reachable
        light_cell_fader.isEnabled = light.reachable
        light_cell_expander.isEnabled = light.reachable
        light_cell_helper_text.isVisible = !light.reachable
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
