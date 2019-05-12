package com.cobonee.app.utily

sealed class MyUiStates {
    object Loading : MyUiStates()
    object Success : MyUiStates()
    data class Error(val message: String) : MyUiStates()
    object NoConnection : MyUiStates()
}