package net.frozendevelopment.frozenhue

import android.app.Application
import net.frozendevelopment.cache.setupLocalCaching
import net.frozendevelopment.frozenhue.di.bridgeServicesModule
import net.frozendevelopment.frozenhue.di.serviceHandlersModule
import net.frozendevelopment.frozenhue.di.systemServicesModue
import net.frozendevelopment.frozenhue.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FrozenHueApp : Application() {

    override fun onCreate() {
        super.onCreate()
        setupKoin()
        setupLocalCaching(this)
    }

    private fun setupKoin() = startKoin {
        androidContext(this@FrozenHueApp)
        modules(
            listOf(
                systemServicesModue,
                bridgeServicesModule,
                serviceHandlersModule,
                viewModelsModule
            )
        )
    }
}
