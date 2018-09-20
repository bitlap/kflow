package io.patamon.kflow

import io.patamon.kflow.core.FlowContext

/**
 * flow main method
 */
fun flow(init: FlowContext.() -> Unit): Flow {
    return Flow(FlowContext().initialize(init))
}

/**
 * Flow Class, create by [FlowContext]
 */
class Flow(private val context: FlowContext) {

    /**
     * execute current flow
     */
    fun execute() {
        context.exec()
    }

    /**
     * flow start node: [FlowContext.start]
     */
    fun getStartNode() = context.start

    /**
     * flow end node: [FlowContext.end]
     */
    fun getEndNode() = context.end
}
