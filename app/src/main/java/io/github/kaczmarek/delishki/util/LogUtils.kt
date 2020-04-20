package io.github.kaczmarek.delishki.util

import android.util.Log
import io.github.kaczmarek.delishki.BuildConfig

private const val defaultMessage = "Undefined error"

fun Throwable.logError(tag: String, message: String?) {
    if (BuildConfig.DEBUG) {
        Log.e(tag, message ?: defaultMessage, this)
    }
}

fun logError(tag: String, message: String?) {
    if (BuildConfig.DEBUG) {
        Log.e(tag, message ?: defaultMessage)
    }
}

fun logDebug(tag: String, message: String?) {
    if (BuildConfig.DEBUG) {
        Log.d(tag, message ?: defaultMessage)
    }
}