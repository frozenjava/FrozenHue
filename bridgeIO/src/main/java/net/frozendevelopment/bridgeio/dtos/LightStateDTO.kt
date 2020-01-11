package net.frozendevelopment.bridgeio.dtos

import com.google.gson.annotations.SerializedName

data class LightStateDTO(
    val on: Boolean,
    @SerializedName("bri") val brightness: Int,
    val hue: Int?,
    @SerializedName("sat") val saturation: Int?,
    val effect: String?,
    val xy: List<Double>?,
    val z: Double?,
    @SerializedName("ct") val colorTemp: Int?,
    val alert: String?,
    val colorMode: String?,
    val mode: String?,
    val reachable: Boolean,
    @SerializedName("transitiontime") val transitionTime: Int?
)
