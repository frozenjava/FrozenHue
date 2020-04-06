package net.frozendevelopment.frozenhue.di

import android.content.Context
import android.net.nsd.NsdManager
import net.frozendevelopment.bridgeio.services.link.BridgeLinkService
import net.frozendevelopment.bridgeio.services.discovery.BridgeNsdService
import net.frozendevelopment.bridgeio.services.link.BridgeLinkServiceType
import net.frozendevelopment.bridgeio.services.polling.LightPollingService
import net.frozendevelopment.bridgeio.services.polling.LightPollingServiceType
import net.frozendevelopment.bridgeio.services.polling.RoomPollingService
import net.frozendevelopment.cache.stores.LightStore
import net.frozendevelopment.frozenhue.modules.lights.LightsListViewModel
import net.frozendevelopment.frozenhue.modules.setupbridge.BridgeSetupViewModel
import net.frozendevelopment.frozenhue.servicehandlers.BridgeSetupHandler
import net.frozendevelopment.frozenhue.servicehandlers.LightPollingServiceHandler
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val systemServicesModue = module {
    factory { androidApplication().getSystemService(Context.NSD_SERVICE) as NsdManager }
}

val bridgeServicesModule = module {
    single { BridgeNsdService.getInstance(get()) }
    single { BridgeLinkService.getInstance() as BridgeLinkServiceType }
    single { LightPollingService.getInstance() as LightPollingServiceType }
    single { RoomPollingService.getInstance() }
}

val storesModule = module {
    single { LightStore() }
}

val serviceHandlersModule = module {
    single { BridgeSetupHandler(get(), get()) }
    single { LightPollingServiceHandler(get(), get()) }
}

val viewModelsModule = module {
    viewModel { BridgeSetupViewModel(get()) }
    viewModel { LightsListViewModel(get()) }
}
