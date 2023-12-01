package com.wisky.tiku.repository

import android.app.Application
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TikuRepository @Inject constructor(application: Application, mRetrofit: Retrofit)