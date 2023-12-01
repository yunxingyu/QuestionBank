package com.wisky.exam.repository

import android.app.Application
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExamRepository @Inject constructor(application: Application, mRetrofit: Retrofit)