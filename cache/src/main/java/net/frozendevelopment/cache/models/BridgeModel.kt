package net.frozendevelopment.cache.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class BridgeModel() : RealmObject() {
    @PrimaryKey
    var id: Int = -1
    var host: String? = null
    var token: String? = null

    constructor(host: String, token: String?) : this() {
        this.host = host
        this.token = token
    }
}
