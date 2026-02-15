package com.example.coroutinefundamentals.utils

sealed class ErrorHandling<out T>{
    // ✅ data class - carries data
    data class Success<T>(val data : T) : ErrorHandling<T>()

    // ✅ data class - carries error message
    data class Error(val message : String) : ErrorHandling<Nothing>()

    // ✅ object - stateless, singleton
    object Loading : ErrorHandling<Nothing>()
}

