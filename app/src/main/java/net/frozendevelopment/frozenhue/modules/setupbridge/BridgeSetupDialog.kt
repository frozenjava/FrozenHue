package net.frozendevelopment.frozenhue.modules.setupbridge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bridge_setup_layout.*
import net.frozendevelopment.frozenhue.R
import net.frozendevelopment.frozenhue.extensions.observeLiveEvents
import net.frozendevelopment.frozenhue.modules.setupbridge.discovery.BridgeDiscoveryFragment
import net.frozendevelopment.frozenhue.modules.setupbridge.linking.BridgeLinkFragment
import net.frozendevelopment.frozenhue.modules.setupbridge.save.BridgeSaveFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class BridgeSetupDialog : BottomSheetDialogFragment() {

    private val viewModel: BridgeSetupViewModel by sharedViewModel()
    private lateinit var pagerAdapter: SetupAdapter
    private val fragments: MutableList<Fragment> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false

        fragments.addAll(listOf(
            BridgeDiscoveryFragment(),
            BridgeLinkFragment(),
            BridgeSaveFragment()
        ))
        pagerAdapter = SetupAdapter(childFragmentManager)

        observeLiveEvents(
            viewModel.liveState.map { it.stage }.distinctUntilChanged(),
            ::applyStateToView
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bridge_setup_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bridge_setup_pager.adapter = pagerAdapter
    }

    private fun applyStateToView(stage: BridgeSetupStage) {
        view ?: return

        val stageFragmentIndex = indexForStage(stage)
        if (bridge_setup_pager.currentItem == indexForStage(stage)) {
            return
        }

        when (stage) {
            BridgeSetupStage.DISCOVERY -> bridge_setup_pager.setCurrentItem(stageFragmentIndex, true)
            BridgeSetupStage.LINKING -> bridge_setup_pager.setCurrentItem(stageFragmentIndex, true)
            BridgeSetupStage.SAVE -> bridge_setup_pager.setCurrentItem(stageFragmentIndex, true)
            BridgeSetupStage.DONE -> this.dismiss()
        }
    }

    private fun indexForStage(stage: BridgeSetupStage): Int {
        return when (stage) {
            BridgeSetupStage.DISCOVERY -> fragments.indexOfFirst { it is BridgeDiscoveryFragment }
            BridgeSetupStage.LINKING -> fragments.indexOfFirst { it is BridgeLinkFragment }
            BridgeSetupStage.SAVE -> fragments.indexOfFirst { it is BridgeSaveFragment }
            BridgeSetupStage.DONE -> 0
        }
    }

    private inner class SetupAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment = fragments[position]

        override fun getCount(): Int = fragments.size
    }
}
