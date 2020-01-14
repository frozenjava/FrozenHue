package net.frozendevelopment.cache.models

import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class BridgeModel() : RealmModel {
    @PrimaryKey
    var id: Int = -1
    var name: String? = null
    var host: String? = null
    var token: String? = null
    var favorite: Boolean = false

    constructor(name: String, host: String, token: String?) : this() {
        this.name = name
        this.host = host
        this.token = token
        this.favorite = false
    }
}
