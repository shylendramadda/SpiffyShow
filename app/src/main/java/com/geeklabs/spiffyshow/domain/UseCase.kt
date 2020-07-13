package com.geeklabs.spiffyshow.domain

abstract class UseCase<in P, R> {

    @Throws(RuntimeException::class)
    abstract fun execute(parameters: P): R
}

data class Quadruple<out A, out B, out C, out D>(
        val first: A,
        val second: B,
        val third: C,
        val fourth: D
)