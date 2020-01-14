package net.frozendevelopment.frozenhue.modules.lights

import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.lights_list_layout.*
import net.frozendevelopment.frozenhue.extensions.observeLiveEvents
import org.koin.androidx.viewmodel.ext.android.viewModel

class LightsListFragment : Fragment() {

    private val viewModel: LightsListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeLiveEvents(viewModel.liveState, ::applyStateToView)
    }

    private fun applyStateToView(state: LightsListState) {
        lights_list_recycler
    }
}