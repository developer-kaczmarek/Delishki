package io.github.kaczmarek.delishki.presentation.base

import io.github.kaczmarek.delishki.ui.base.BaseView
import moxy.MvpPresenter

abstract class BasePresenter<View: BaseView> : MvpPresenter<View>()