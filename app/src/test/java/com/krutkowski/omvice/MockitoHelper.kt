package com.krutkowski.omvice.data.streams

import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.mockito.Mockito

/**
 * Created by Konrad Rutkowski on 21/07/2022
 */
fun <T> any(): T {
    Mockito.any<T>()
    return uninitialized()
}

private fun <T> uninitialized(): T = null as T

fun <T : Any> eq(value: T): T = Mockito.eq(value) ?: value

fun <T> argThat(matcher: (T) -> Boolean): T {
    return Mockito.argThat(matcher) ?: uninitialized()
}

fun <T> compare(matcher: (T) -> Boolean): Matcher<T> {
    return object : TypeSafeMatcher<T>() {
        override fun describeTo(description: Description?) {
        }

        override fun matchesSafely(item: T): Boolean {
            return matcher(item)
        }
    }
}