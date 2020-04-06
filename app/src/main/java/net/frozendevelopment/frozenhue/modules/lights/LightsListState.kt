package net.frozendevelopment.frozenhue.modules.lights

data class LightsListState(
    val lights: List<LightState> = emptyList()
)

data class LightState(
    val id: Int,
    val name: String,
    val on: Boolean,
    val reachable: Boolean,
    val brightness: Int
)
