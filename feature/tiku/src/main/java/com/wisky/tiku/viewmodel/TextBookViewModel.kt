package com.wisky.tiku.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.wisky.common.network.DispatcherProvider
import com.wisky.tiku.repository.TikuRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TextBookViewModel@Inject constructor(
    application: Application,
    private val tikuRepository: TikuRepository,
//    private val trashNote: TrashNoteRepo,
    private val dispatcherProvider: DispatcherProvider,
//    private val alarmScheduler: AlarmScheduler,
) : AndroidViewModel(application)
