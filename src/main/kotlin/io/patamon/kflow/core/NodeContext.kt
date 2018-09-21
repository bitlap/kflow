package io.patamon.kflow.core

/**
 * [io.patamon.kflow.node.Node] context
 */
class NodeContext {

    /**
     * Actual execute method
     */
    internal var handler: (flowData: MutableMap<String, Any?>) -> Unit = { }

    /**
     * Set current context handler
     */
    fun handler(handler: (flowData: MutableMap<String, Any?>) -> Unit = { }) {
        this.handler = handler
    }

}
