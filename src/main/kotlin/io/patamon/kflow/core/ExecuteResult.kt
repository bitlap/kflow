package io.patamon.kflow.core

/**
 * Desc: handler execute result
 */
data class ExecuteResult(
        var result: Any,
        var exception: Exception? = null
) {

    /**
     * handle with error
     */
    fun hasError() = exception != null

    // static
    companion object {
        val IGNORE = ExecuteResult("IGNORE")
        val OK = ExecuteResult("OK")

        /**
         * static method with exception
         */
        fun withError(e: Exception) = ExecuteResult("ERROR", e)
    }
}