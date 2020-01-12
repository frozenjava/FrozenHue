package net.frozendevelopment.cache.stores

import io.realm.Realm
import net.frozendevelopment.cache.models.LightModel

class LightStore {

    fun persistLights(lights: List<LightModel>) {
        val realm = Realm.getDefaultInstance()

        realm.executeTransactionAsync {
            it.insertOrUpdate(lights)
        }
    }
}
