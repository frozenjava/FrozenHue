package net.frozendevelopment.frozenhue.extensions

import io.realm.RealmList
import net.frozendevelopment.bridgeio.dtos.LightStateDTO
import net.frozendevelopment.cache.models.LightStateModel

fun LightStateModel.loadFromDTO(dto: LightStateDTO): LightStateModel {
    this.on = dto.on
    this.brightness = dto.brightness
    this.hue = dto.hue
    this.saturation = dto.saturation
    this.effect = dto.effect
    this.xy = RealmList<Double>().fromList(dto.xy!!)
    this.z = dto.z
    this.colorTemp = dto.colorTemp
    this.alert = dto.alert
    this.colorMode = dto.colorMode
    this.mode = dto.mode
    this.reachable = dto.reachable
    this.transitionTime = dto.transitionTime
    return this
}

fun LightStateModel.toDTO(): LightStateDTO = LightStateDTO(
    on = this.on,
    brightness = this.brightness,
    hue = this.hue,
    saturation = this.saturation,
    effect = this.effect,
    xy = this.xy,
    z = this.z,
    colorTemp = this.colorTemp,
    alert = this.alert,
    colorMode = this.colorMode,
    mode = this.mode,
    reachable = this.reachable,
    transitionTime = this.transitionTime
)
