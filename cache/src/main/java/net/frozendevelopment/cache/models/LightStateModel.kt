package net.frozendevelopment.cache.models

import io.realm.RealmList
import io.realm.RealmObject

open class LightStateModel : RealmObject() {

    var on: Boolean = false
    var brightness: Int = -1
    var hue: Int? = null
    var saturation: Int? = null
    var effect: String? = null
    var xy: RealmList<Double>? = null
    var z: Double? = null
    var colorTemp: Int? = null
    var alert: String? = null
    var colorMode: String? = null
    var mode: String? = null
    var reachable: Boolean = false
    var transitionTime: Int? = null
}