package net.frozendevelopment.frozenhue.modules.setupbridge.discovery

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import kotlinx.android.synthetic.main.bridge_discovery_layout.*
import net.frozendevelopment.frozenhue.R
import net.frozendevelopment.frozenhue.extensions.applyText
import net.frozendevelopment.frozenhue.extensions.observeLiveEvents
import net.frozendevelopment.frozenhue.extensions.onTextChanged
import net.frozendevelopment.frozenhue.modules.setupbridge.BridgeDiscoveryState
import net.frozendevelopment.frozenhue.modules.setupbridge.BridgeSetupViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class BridgeDiscoveryFragment : Fragment(R.layout.bridge_discovery_layout) {

    private val viewModel: BridgeSetupViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeLiveEvents(
            viewModel.liveState.map { it.discoveryState }.distinctUntilChanged(),
            ::applyStateToView
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bridge_setup_host.onTextChanged { viewModel.discoveryHost = it }

        bridge_setup_connect.setOnClickListener {
            viewModel.validateHostAndContinue()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.startDiscovery()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopDiscovery()
    }

    private fun applyStateToView(state: BridgeDiscoveryState) {
        view ?: return

        bridge_setup_host.applyText(state.host)
        bridge_setup_connect.isEnabled = !state.host.isNullOrBlank()
        bridge_setup_discovery_throbber.isVisible = state.discovering
        bridge_setup_discovery_label.text = state.discoveryLabel
    }
}
