package com.krutkowski.omvice

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Created by Konrad Rutkowski on 21/07/2022
 */
class OmviceApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@OmviceApp)
            modules(KoinModules().getAllModules())
        }
    }
}