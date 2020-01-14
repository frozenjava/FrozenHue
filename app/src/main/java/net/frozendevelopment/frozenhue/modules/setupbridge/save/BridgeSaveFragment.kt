package net.frozendevelopment.frozenhue.modules.setupbridge.save

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import kotlinx.android.synthetic.main.bridge_save_layout.*
import net.frozendevelopment.frozenhue.R
import net.frozendevelopment.frozenhue.extensions.observeLiveEvents
import net.frozendevelopment.frozenhue.extensions.onTextChanged
import net.frozendevelopment.frozenhue.modules.setupbridge.BridgeSaveState
import net.frozendevelopment.frozenhue.modules.setupbridge.BridgeSetupViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class BridgeSaveFragment : Fragment(R.layout.bridge_save_layout) {

    private val viewModel: BridgeSetupViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeLiveEvents(
            viewModel.liveState.map { it.saveState }.distinctUntilChanged(),
            ::applyStateToView
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bridge_save_name.onTextChanged { viewModel.bridgeName = it }

        bridge_save_button.setOnClickListener {
            viewModel.save()
        }
    }

    private fun applyStateToView(state: BridgeSaveState) {
    }
}