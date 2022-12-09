package com.code.tusome.di

import com.code.tusome.ui.viewmodels.AssignmentViewModel
import com.code.tusome.ui.viewmodels.MainViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RoomModule::class])
interface RoomComponent {
    fun injectMainViewModel(mainViewModel: MainViewModel)
    fun injectAssignmentViewModel(assignmentViewModel: AssignmentViewModel)
}