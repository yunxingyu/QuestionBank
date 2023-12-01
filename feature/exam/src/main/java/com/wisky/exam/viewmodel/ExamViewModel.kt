package com.wisky.exam.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.wisky.common.network.DispatcherProvider
import com.wisky.exam.repository.ExamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class ExamViewModel@Inject constructor(
    application: Application,
    private val examRepository: ExamRepository,
//    private val trashNote: TrashNoteRepo,
    private val dispatcherProvider: DispatcherProvider,
//    private val alarmScheduler: AlarmScheduler,
) : AndroidViewModel(application)
