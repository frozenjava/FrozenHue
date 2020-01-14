package net.frozendevelopment.cache.stores

import io.realm.Realm
import net.frozendevelopment.cache.models.BridgeModel

class BridgeStore {

    fun getDefaultBridge(): BridgeModel? {
        val realm = Realm.getDefaultInstance()

        val favorite = realm.where(BridgeModel::class.java)
            .equalTo("favorite", true)
            .findFirst()

        if (favorite != null) {
            return favorite
        }

        return realm.where(BridgeModel::class.java).findFirst()
    }
}