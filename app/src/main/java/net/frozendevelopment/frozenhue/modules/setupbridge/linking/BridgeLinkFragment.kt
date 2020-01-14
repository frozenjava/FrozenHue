package net.frozendevelopment.frozenhue.modules.setupbridge.linking

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import kotlinx.android.synthetic.main.bridge_link_layout.*
import net.frozendevelopment.frozenhue.R
import net.frozendevelopment.frozenhue.extensions.observeLiveEvents
import net.frozendevelopment.frozenhue.modules.setupbridge.BridgeLinkState
import net.frozendevelopment.frozenhue.modules.setupbridge.BridgeSetupViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class BridgeLinkFragment : Fragment(R.layout.bridge_link_layout) {

    private val viewModel: BridgeSetupViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeLiveEvents(
            viewModel.liveState.map { it.linkState }.distinctUntilChanged(),
            ::applyStateToView
        )
    }

    override fun onResume() {
        super.onResume()
        viewModel.startLinking()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopLinking()
    }

    private fun applyStateToView(state: BridgeLinkState) {
        view ?: return

        bridge_pairing_label.text = state.label
    }
}