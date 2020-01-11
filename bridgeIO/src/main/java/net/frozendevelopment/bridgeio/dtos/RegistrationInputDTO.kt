package net.frozendevelopment.bridgeio.dtos

import com.google.gson.annotations.SerializedName

data class RegistrationInputDTO(
    @SerializedName("devicetype")
    val deviceType: String = "frozen_hue"
)
