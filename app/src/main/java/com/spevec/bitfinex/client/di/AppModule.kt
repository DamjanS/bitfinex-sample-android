package com.spevec.bitfinex.client.di

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.spevec.bitfinex.client.data.api.mapper.RequestApiMapper
import com.spevec.bitfinex.client.data.api.mapper.ResponseApiMapper
import com.spevec.bitfinex.client.data.repository.BitfinexRepository
import com.spevec.bitfinex.client.data.repository.BitfinexRepositoryImpl
import com.spevec.bitfinex.client.data.websocket.WebSocketConnectionManager
import com.spevec.bitfinex.client.data.websocket.okhttp.OkHttpConnectionCreator
import com.spevec.bitfinex.client.data.websocket.okhttp.factory.OkHttpWebSocketFactory
import com.spevec.bitfinex.client.network.ConnectivityMonitor
import com.spevec.bitfinex.client.routing.Router
import com.spevec.bitfinex.client.routing.RouterImpl
import com.spevec.bitfinex.client.ui.viewmodel.MainViewModel
import com.spevec.bitfinex.client.usecase.QueryOrderBookUseCase
import com.spevec.bitfinex.client.usecase.QueryTickerUseCase
import com.spevec.bitfinex.client.usecase.ShutdownConnectionUseCase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

const val BITFINEX_URL = "wss://api-pub.bitfinex.com/ws/2"

val AppModule = module {

    // router
    factory<Router> {
        val activity: AppCompatActivity = it[0]
        val fragmentManager: FragmentManager = activity.supportFragmentManager
        RouterImpl(activity, fragmentManager)
    }

    // use case
    single { QueryOrderBookUseCase(get()) }
    single { QueryTickerUseCase(get()) }
    single { ShutdownConnectionUseCase(get()) }

    // webSocket
    single { OkHttpConnectionCreator(BITFINEX_URL, get()) }
    single { OkHttpWebSocketFactory(get()) }
    single { WebSocketConnectionManager(get(), get(), get(), get(), get(BACKGROUND_SCHEDULER)) }
    single<BitfinexRepository> { BitfinexRepositoryImpl(get(), get()) }

    // mappers
    single { RequestApiMapper(get()) }
    single { ResponseApiMapper(get()) }

    // view model
    viewModel { MainViewModel(get(), get(), get(BACKGROUND_SCHEDULER), get(MAIN_SCHEDULER)) }

    // connectivity
    single { ConnectivityMonitor() }

    single {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }
}