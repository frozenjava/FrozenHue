package net.frozendevelopment.frozenhue.modules.lights

import net.frozendevelopment.cache.models.LightModel

data class LightsListState(
    val lights: List<LightModel> = emptyList()
)
