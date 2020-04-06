package net.frozendevelopment.frozenhue.servicehandlers

import android.util.Log
import kotlinx.coroutines.flow.collect
import net.frozendevelopment.bridgeio.BridgeContext
import net.frozendevelopment.bridgeio.services.discovery.BridgeNsdService
import net.frozendevelopment.bridgeio.services.discovery.DiscoveryPayload
import net.frozendevelopment.bridgeio.services.link.BridgeLinkServiceType

class BridgeSetupHandler(
    private val discoveryService: BridgeNsdService,
    private val linkService: BridgeLinkServiceType
) {

    val isDiscovering: Boolean
        get() = discoveryService.getRunningStatus()

    val isLinking: Boolean
        get() = linkService.getRunningStatus()

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

    fun stopDiscovery() = discoveryService.stop()

    suspend fun link(onError: (Exception) -> Unit, onSuccess: (String) -> Unit) = linkService.start().collect { result ->
        if (result.error != null) {
            onError(result.error!!)
            Log.d("HOST_DISCOVERY", "ERROR: ${result.error!!.localizedMessage}")
        }

        if (result.data != null) {
            onSuccess(result.data!!)
        }
    }

    fun stopLinking() = linkService.stop()
}
