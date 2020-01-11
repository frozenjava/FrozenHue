package net.frozendevelopment.bridgeio.dtos

import com.google.gson.annotations.SerializedName

data class GroupActionDTO(
    val on: Boolean,
    @SerializedName("bri") val brightness: Int,
    val hue: Int,
    @SerializedName("sat") val saturation: Int,
    val effect: String?,
    val xy: List<Double>,
    @SerializedName("ct") val colorTemp: Int,
    val alert: String?,
    @SerializedName("colormode") val colorMode: String
)
