package com.ibrahim.core

import com.ibrahim.core.Failure.FeatureFailure

/**
 * Base Class for handling errors/failures/exceptions.
 * Every feature specific failure should extend [FeatureFailure] class.
 */
sealed class Failure {
    data class NetworkConnection(val throwable: Throwable) : Failure()
    data class ServerError(
        val code: Int? = null,
        val type: String? = null,
        val info: String? = null
    ) : Failure()

    /** * Extend this class for feature specific failures.*/
    abstract class FeatureFailure : Failure()
}
