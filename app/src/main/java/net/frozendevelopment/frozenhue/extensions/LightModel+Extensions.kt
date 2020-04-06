package net.frozendevelopment.frozenhue.extensions

import net.frozendevelopment.bridgeio.dtos.LightDTO
import net.frozendevelopment.cache.models.LightModel
import net.frozendevelopment.cache.models.LightStateModel
import net.frozendevelopment.frozenhue.modules.lights.LightState

fun LightModel.loadFromDTO(dto: LightDTO): LightModel {
    this.id = dto.id
    this.state = LightStateModel().loadFromDTO(dto.state)
    this.name = dto.name
    this.type = dto.type
    this.modelId = dto.modelId
    this.uniqueId = dto.uniqueId
    this.productId = dto.productId
    this.productName = dto.productName
    this.manufacturer = dto.manufacturer
    this.swVersion = dto.swVersion
    this.swConfig = dto.swConfig
    return this
}

fun LightModel.toDTO(): LightDTO = LightDTO(
    id = this.id,
    state = this.state!!.toDTO(),
    name = this.name,
    type = this.type,
    modelId = this.modelId,
    uniqueId = this.uniqueId,
    productId = this.productId,
    productName = this.productName,
    manufacturer = this.manufacturer,
    swVersion = this.swVersion,
    swConfig = this.swConfig
)

fun LightModel.toLightState(): LightState = LightState(
    id = this.id,
    name = this.name ?: "Unknown",
    on = this.state?.on ?: false,
    reachable = this.state?.reachable ?: false,
    brightness = this.state?.brightness ?: 0
)