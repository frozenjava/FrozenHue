package net.frozendevelopment.frozenhue.di

import android.content.Context
import android.net.nsd.NsdManager
import net.frozendevelopment.bridgeio.services.auth.BridgeAuthService
import net.frozendevelopment.bridgeio.services.discovery.BridgeNsdService
import net.frozendevelopment.bridgeio.services.polling.LightPollingService
import net.frozendevelopment.bridgeio.services.polling.RoomPollingService
import net.frozendevelopment.frozenhue.modules.setupbridge.SetupBridgeViewModel
import net.frozendevelopment.frozenhue.modules.setupbridge.auth.BridgeAuthViewModel
import net.frozendevelopment.frozenhue.servicehandlers.BridgeSetupHandler
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val systemServicesModue = module {
    factory { androidApplication().getSystemService(Context.NSD_SERVICE) as NsdManager }
}

val bridgeServicesModule = module {
    single { BridgeNsdService.getInstance(get()) }
    single { BridgeAuthService.getInstance() }
    single { LightPollingService.getInstance() }
    single { RoomPollingService.getInstance() }
}

val serviceHandlersModule = module {
    single { BridgeSetupHandler(get(), get()) }
}

val viewModelsModule = module {
    viewModel { SetupBridgeViewModel(get()) }
    viewModel { BridgeAuthViewModel(get()) }
}
