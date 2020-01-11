package net.frozendevelopment.frozenhue.modules.setupbridge.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_bridge_auth.*
import net.frozendevelopment.frozenhue.R
import net.frozendevelopment.frozenhue.extensions.observeLiveEvents
import org.koin.androidx.viewmodel.ext.android.viewModel

class BridgeAuthFragment : Fragment(R.layout.fragment_bridge_auth) {

    private val viewModel: BridgeAuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveEvents(viewModel.state, ::applyStateToView)
    }

    private fun applyStateToView(state: BridgeAuthState) {
        bridge_auth_label.text = state.label
    }
}
