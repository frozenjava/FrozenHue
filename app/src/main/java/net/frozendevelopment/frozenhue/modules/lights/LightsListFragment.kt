package net.frozendevelopment.frozenhue.modules.lights

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import com.xwray.groupie.groupiex.plusAssign
import kotlinx.android.synthetic.main.lights_list_layout.*
import net.frozendevelopment.frozenhue.R
import net.frozendevelopment.frozenhue.extensions.observeLiveEvents
import net.frozendevelopment.frozenhue.modules.lights.views.LightCellItem
import net.frozendevelopment.frozenhue.modules.lights.views.LightsSectionHeader
import net.frozendevelopment.frozenhue.views.sticky.StickyAdapterHandler
import net.frozendevelopment.frozenhue.views.sticky.StickyLinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class LightsListFragment : Fragment(R.layout.lights_list_layout) {

    private val viewModel: LightsListViewModel by viewModel()

    private val adapter = StickyAdapterHandler<GroupieViewHolder>()
    private lateinit var stickyLayoutManager: StickyLinearLayoutManager<StickyAdapterHandler<GroupieViewHolder>>
    private val lightSection = Section()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)
        observeLiveEvents(viewModel.liveState, ::applyStateToView)

        lightSection.apply {
            setHeader(LightsSectionHeader("Lights"))
            addAll(viewModel.state.lights.map { LightCellItem(it, { _ -> }) })
        }

        adapter += lightSection
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stickyLayoutManager = StickyLinearLayoutManager(this.context!!)

        lights_list_recycler.adapter = adapter
        lights_list_recycler.layoutManager = stickyLayoutManager
    }

    private fun applyStateToView(state: LightsListState) {
        view ?: return

        lightSection.update(state.lights.map { LightCellItem(it, { _ -> }) })
    }
}
