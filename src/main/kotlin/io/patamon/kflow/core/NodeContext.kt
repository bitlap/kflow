package io.patamon.kflow.core

/**
 * [io.patamon.kflow.node.Node] context
 */
class NodeContext {

    /**
     * Actual execute method
     */
    internal var handler: () -> Unit = { }

    /**
     * Set current context handler
     */
    fun handler(handler: () -> Unit = { }) {
        this.handler = handler
    }

}
