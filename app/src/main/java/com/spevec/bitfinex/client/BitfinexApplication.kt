package com.spevec.bitfinex.client

import android.app.Application
import com.spevec.bitfinex.client.di.AppModule
import com.spevec.bitfinex.client.di.ThreadingModule
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.Module

class BitfinexApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, modules)
    }

    private val modules: List<Module> = listOf(
        AppModule,
        ThreadingModule
    )
}