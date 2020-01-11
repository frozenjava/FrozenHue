package net.frozendevelopment.frozenhue.servicehandlers

import android.util.Log
import kotlinx.coroutines.flow.collect
import net.frozendevelopment.bridgeio.BridgeContext
import net.frozendevelopment.bridgeio.services.auth.BridgeAuthService
import net.frozendevelopment.bridgeio.services.discovery.BridgeNsdService
import net.frozendevelopment.bridgeio.services.discovery.DiscoveryPayload
import net.frozendevelopment.cache.models.BridgeModel

class BridgeSetupHandler(
    private val discoveryService: BridgeNsdService,
    private val authService: BridgeAuthService) {

    val isDiscovering: Boolean = discoveryService.getRunningStatus()

    fun stopDiscovery() = discoveryService.stop()

    suspend fun discover(onError: (Exception) -> Unit, onHostDiscovered: (DiscoveryPayload) -> Unit) = discoveryService.start().collect { result ->
        if (result.error != null) {
            onError(result.error!!)
            Log.d("HOST_DISCOVERY", "ERROR: ${result.error!!.localizedMessage}")
        }

        if (result.data != null) {
            onHostDiscovered(result.data!!)
            Log.d("HOST_DISCOVERY", "HOST: ${result.data!!.host} | PORT: ${result.data!!.port}")
            BridgeContext.host = result.data!!.host
        }
    }

    suspend fun authenticate(onError: (Exception) -> Unit, onSuccess: () -> Unit) = authService.start().collect { result ->
        if (result.error != null) {
            onError(result.error!!)
            Log.d("HOST_DISCOVERY", "ERROR: ${result.error!!.localizedMessage}")
        }

        if (result.data != null) {
            onSuccess()
        }
    }

    fun stopAuthenticating() = authService.stop()

}

enum class BridgeSetupState {
    IDLE,
    DISCOVERING,
    AUTHENTICATING,
}
