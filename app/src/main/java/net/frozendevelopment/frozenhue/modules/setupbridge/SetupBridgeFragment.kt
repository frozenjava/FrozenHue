package net.frozendevelopment.frozenhue.modules.setupbridge

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_setup_bridge.*
import net.frozendevelopment.frozenhue.R
import net.frozendevelopment.frozenhue.extensions.applyText
import net.frozendevelopment.frozenhue.extensions.navigateOnClick
import net.frozendevelopment.frozenhue.extensions.observeLiveEvents
import net.frozendevelopment.frozenhue.extensions.onTextChanged
import org.koin.androidx.viewmodel.ext.android.viewModel

class SetupBridgeFragment : Fragment(R.layout.fragment_setup_bridge) {

    private val viewModel: SetupBridgeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveEvents(viewModel.state, ::applyStateToView)

        bridge_setup_host.onTextChanged { viewModel.host = it }

        bridge_setup_connect.navigateOnClick(R.id.action_setupBridgeFragment_to_bridgeAuthFragment)
    }

    private fun applyStateToView(state: SetupBridgeState) {
        bridge_setup_label.text = state.label
        bridge_setup_label_throbber.isVisible = state.discovering
        bridge_setup_host.applyText(state.host)
        bridge_setup_connect.isEnabled = !state.host.isNullOrBlank()
    }
}
