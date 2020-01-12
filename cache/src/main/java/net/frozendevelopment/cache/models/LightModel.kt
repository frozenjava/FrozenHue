package net.frozendevelopment.cache.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class LightModel : RealmObject() {
    @PrimaryKey
    var id: Int = -1
    var state: LightStateModel? = null
    var name: String? = null
    var type: String? = null
    var modelId: String? = null
    var uniqueId: String? = null
    var productId: String? = null
    var productName: String? = null
    var manufacturer: String? = null
    var swVersion: String? = null
    var swConfig: String? = null
}