package net.frozendevelopment.frozenhue.modules.setupbridge.linking

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bridge_link_next_button.setOnClickListener {
            viewModel.goToNext()
        }
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

        bridge_link_label.text = state.label
        bridge_link_throbber.isVisible = state.pairing
        bridge_link_next_button.isVisible = !state.pairing && !state.token.isNullOrBlank()
    }
}