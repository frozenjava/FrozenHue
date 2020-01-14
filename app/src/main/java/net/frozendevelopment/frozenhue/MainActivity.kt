package net.frozendevelopment.frozenhue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import net.frozendevelopment.bridgeio.BridgeContext
import net.frozendevelopment.cache.stores.BridgeStore

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.nav_host_fragment)

        loadBridge()
    }

    private fun loadBridge() {
        val bridgeStore = BridgeStore()

        val bridge = bridgeStore.getDefaultBridge()

        if (bridge == null) {
            navController.navigate(R.id.navigation_setup_bridge_dialog)
        } else {
            BridgeContext.host = bridge.host
            BridgeContext.token = bridge.token
        }
    }
}
