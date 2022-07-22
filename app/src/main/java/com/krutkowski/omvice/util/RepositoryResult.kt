package com.krutkowski.omvice.util

/**
 * Created by Konrad Rutkowski on 21/07/2022
 */
sealed class RepositoryResult<out T : Any> {
    class Success<out T : Any>(val data: T) : RepositoryResult<T>()
    class Error(val error: String) : RepositoryResult<Nothing>()
}