package net.frozendevelopment.frozenhue.servicehandlers

import kotlinx.coroutines.flow.collect
import net.frozendevelopment.bridgeio.dtos.LightDTO
import net.frozendevelopment.bridgeio.services.BridgeService
import net.frozendevelopment.cache.models.LightModel
import net.frozendevelopment.cache.stores.LightStore
import net.frozendevelopment.frozenhue.extensions.loadFromDTO

class LightPollingServiceHandler(
    private val lightPoller: BridgeService<Map<Int, LightDTO>>,
    private val lightStore: LightStore
) {

    val isRunning = lightPoller.getRunningStatus()

    suspend fun start(onError: (Exception) -> Unit) = lightPoller.start().collect { result ->
        if (result.error != null) {
            onError(result.error!!)
        }

        val lights = result.data
            ?.map { it.value.copy(id = it.key) }
            ?.map { LightModel().loadFromDTO(it) }
            ?: emptyList()

        lightStore.persistLights(lights)
    }

    fun stop() = lightPoller.stop()
}
