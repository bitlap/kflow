package io.patamon.kflow.core

import io.patamon.kflow.node.End
import io.patamon.kflow.node.Node
import io.patamon.kflow.node.Start
import io.patamon.kflow.node.TaskNode
import io.patamon.kflow.utils.add

/**
 * A flow instance context
 */
open class FlowContext {

    /**
     * [start] and [end] node init
     */
    val start = Start()
    val end = End()

    /**
     * ("nodeName" -> "nodeName") relations
     */
    private val lines = mutableListOf<Pair<String, String>>()

    /**
     * "node" -> [Node]
     */
    private val nodeMap = mutableMapOf<String, Node>(
            Pair(this.start.name, this.start),
            Pair(this.end.name, this.end)
    )


    internal fun initialize(init: FlowContext.() -> Unit): FlowContext {
        this.apply(init)
        // init flow
        lines.forEach { (from, to) ->
            val fromNode = nodeMap[from] ?: nodeMap.add(from, TaskNode.empty(from))
            val toNode = nodeMap[to] ?: nodeMap.add(to, TaskNode.empty(to))
            fromNode.addNext(toNode)
            toNode.addPrev(fromNode)
        }
        // check cycle
        if (start.checkCycle()) {
            throw KFlowException("The flow has cycle, please check it !!!")
        }
        return this
    }

    /**
     * execute current flow
     */
    internal fun exec(flowData: Map<String, Any?>) {
        exec(this.start, flowData)
    }

    private fun exec(node: Node, flowData: Map<String, Any?>) {
        val context = ExecuteContext(flowData)
        node.execute(context)
        context.await()
    }

    infix fun Node.to(node: String) {
        lines.add(Pair(this.name, node))
    }

    infix fun String.to(node: Node) {
        lines.add(Pair(this, node.name))
    }

    infix fun String.to(node: String) {
        lines.add(Pair(this, node))
    }

    infix fun Node.to(node: Node) {
        lines.add(Pair(this.name, node.name))
    }

    operator fun String.invoke(init: NodeContext.() -> Unit): Node {
        return nodeMap.add(this, TaskNode(this, NodeContext().apply(init)))
    }

}