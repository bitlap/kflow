package io.patamon.kflow.core

/**
 * Desc: KFlow exception
 */
class KFlowException: RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(cause: Throwable) : super(cause)
}
