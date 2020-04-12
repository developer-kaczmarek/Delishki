package io.github.kaczmarek.delishki.di.components

import dagger.Subcomponent
import io.github.kaczmarek.delishki.di.modules.MainModule
import io.github.kaczmarek.delishki.di.scopes.MainScope
import io.github.kaczmarek.delishki.presentation.main.MainPresenter

@MainScope
@Subcomponent(modules = [MainModule::class])
interface MainSubcomponent {
    fun inject(presenter: MainPresenter)
}