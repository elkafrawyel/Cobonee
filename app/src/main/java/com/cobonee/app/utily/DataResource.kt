package com.cobonee.app.utily

sealed class DataResource<out T : Any> {
    data class Success<out T : Any>(val data: T) : DataResource<T>()
    data class Error(val exception: Exception) : DataResource<Nothing>()
}