package com.ekochkov.santehnikatestapp.di

import com.ekochkov.santehnikatestapp.di.modules.RemoteModule
import com.ekochkov.santehnikatestapp.viewModel.HomeFragmentViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RemoteModule::class])
interface AppComponent {
    fun inject(homeFragmentViewModel: HomeFragmentViewModel)
}