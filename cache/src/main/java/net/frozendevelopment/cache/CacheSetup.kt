package net.frozendevelopment.cache

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

fun setupLocalCaching(application: Application) {
    Realm.init(application)

    val realmConfig = RealmConfiguration.Builder()
        .deleteRealmIfMigrationNeeded()
        .build()

    Realm.setDefaultConfiguration(realmConfig)
}