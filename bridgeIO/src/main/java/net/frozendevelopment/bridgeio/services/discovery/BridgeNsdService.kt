package net.frozendevelopment.bridgeio.services.discovery

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.frozendevelopment.bridgeio.services.BridgeService
import net.frozendevelopment.bridgeio.services.BridgeServiceResult

class BridgeNsdService private constructor(private val nsdManager: NsdManager): BridgeService<DiscoveryPayload> {
    private var isDiscovering: Boolean = false
    private val nsdEventChannel: Channel<NsdEvent> = Channel()
    private val discoverer = NsdDiscoverer(nsdEventChannel)

    override fun getRunningStatus(): Boolean = isDiscovering

    override fun start(): Flow<BridgeServiceResult<DiscoveryPayload>> = flow {
        nsdManager.discoverServices("_hue._tcp", NsdManager.PROTOCOL_DNS_SD, discoverer)

        for (event in nsdEventChannel) {
            when (event.event) {
                DiscoveryEvent.STARTED_DISCOVERY -> { }
                DiscoveryEvent.STOPPED_DISCOVERY -> { }
                DiscoveryEvent.DISCOVERY_FAILED_TO_START -> {
                    emit(BridgeServiceResult(error = Exception("Failed to start service discovery.")))
                    this@BridgeNsdService.stop()
                }
                DiscoveryEvent.DISCOVERY_FAILED_TO_END -> { }
                DiscoveryEvent.SERVICE_RESOLVE_FAILED -> emit(
                    BridgeServiceResult(
                        error = Exception(
                            "Failed to resolve service with error code ${event.errorCode}"
                        )
                    )
                )
                DiscoveryEvent.SERVICE_RESOLVE_SUCCESS -> {
                    emit(
                        BridgeServiceResult(
                            data = DiscoveryPayload(
                                host = event.serviceInfo!!.host.hostAddress,
                                port = event.serviceInfo.port
                            )
                        )
                    )
                }
                DiscoveryEvent.SERVICE_FOUND -> {
                    this@BridgeNsdService.nsdManager.resolveService(
                        event.serviceInfo,
                        NsdResolver(nsdEventChannel)
                    )
                }
                DiscoveryEvent.SERVICE_LOST -> {
                }
            }
        }
    }

    override fun stop() {
        try {
            isDiscovering = false
            nsdManager.stopServiceDiscovery(discoverer)
        } catch (ignore: java.lang.Exception) { }
    }

    companion object {
        private var instance: BridgeNsdService? = null

        fun getInstance(nsdManager: NsdManager): BridgeNsdService {
            if (instance != null) {
                return instance!!
            }

            instance = BridgeNsdService(nsdManager)
            return instance!!
        }
    }

}

private class NsdEvent(
    val event: DiscoveryEvent,
    val errorCode: Int?,
    val serviceInfo: NsdServiceInfo?
)

private class NsdDiscoverer(private val nsdEventChannel: Channel<NsdEvent>): NsdManager.DiscoveryListener {
    override fun onServiceFound(serviceInfo: NsdServiceInfo?) {
        nsdEventChannel.offer(NsdEvent(DiscoveryEvent.SERVICE_FOUND, null, serviceInfo))
    }

    override fun onStopDiscoveryFailed(serviceType: String?, errorCode: Int) {
        nsdEventChannel.offer(NsdEvent(DiscoveryEvent.DISCOVERY_FAILED_TO_END, errorCode, null))
    }

    override fun onStartDiscoveryFailed(serviceType: String?, errorCode: Int) {
        nsdEventChannel.offer(NsdEvent(DiscoveryEvent.DISCOVERY_FAILED_TO_START, errorCode, null))
    }

    override fun onDiscoveryStarted(serviceType: String?) {
        nsdEventChannel.offer(NsdEvent(DiscoveryEvent.STARTED_DISCOVERY, null, null))
    }

    override fun onDiscoveryStopped(serviceType: String?) {
        nsdEventChannel.offer(NsdEvent(DiscoveryEvent.STOPPED_DISCOVERY, null, null))
    }

    override fun onServiceLost(serviceInfo: NsdServiceInfo?) {
        nsdEventChannel.offer(NsdEvent(DiscoveryEvent.SERVICE_LOST, null, serviceInfo))
    }
}

private class NsdResolver(private val nsdEventChannel: Channel<NsdEvent>): NsdManager.ResolveListener {

    override fun onResolveFailed(serviceInfo: NsdServiceInfo?, errorCode: Int) {
        nsdEventChannel.offer(NsdEvent(DiscoveryEvent.SERVICE_RESOLVE_FAILED, errorCode, null))
    }

    override fun onServiceResolved(serviceInfo: NsdServiceInfo?) {
        nsdEventChannel.offer(NsdEvent(DiscoveryEvent.SERVICE_RESOLVE_SUCCESS, null, serviceInfo))
    }

}
