package io.patamon.kflow.core

import io.patamon.kflow.node.End
import io.patamon.kflow.node.Node
import io.patamon.kflow.node.NodeType.END
import io.patamon.kflow.node.NodeType.START
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
            // if node has no body, use default empty body
            val fromNode = nodeMap[from] ?: nodeMap.add(from, TaskNode.empty(from))
            val toNode = nodeMap[to] ?: nodeMap.add(to, TaskNode.empty(to))
            fromNode.addNext(toNode)
            toNode.addPrev(fromNode)
        }
        // check `none prev` and `none next`
        nodeMap.values.forEach {
            when {
                // [start] and [end] node check
                it.type == START -> if (it.next().isEmpty()) throw KFlowException("[${it.name}] must have next nodes.")
                it.type == END -> if (it.prev().isEmpty()) throw KFlowException("[${it.name}] must have prev nodes.")
                // [task] node check
                it.prev().isEmpty() -> throw KFlowException("[${it.name}] must have prev nodes.")
                it.next().isEmpty() -> throw KFlowException("[${it.name}] must have next nodes.")
            }
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

    /**
     * add line relationship
     */
    private fun addLinePair(pair: Pair<String, String>) {
        if (lines.contains(pair)) {
            throw KFlowException("[${pair.first}] to [${pair.second}] relationship already exists.")
        }
        lines.add(pair)
    }


    // =============================== Enhance function ============================== //
    infix fun Node.to(node: String) {
        addLinePair(Pair(this.name, node))
    }

    infix fun String.to(node: Node) {
        addLinePair(Pair(this, node.name))
    }

    infix fun String.to(node: String) {
        addLinePair(Pair(this, node))
    }

    infix fun Node.to(node: Node) {
        addLinePair(Pair(this.name, node.name))
    }

    operator fun String.invoke(init: NodeContext.() -> Unit): Node {
        return nodeMap.add(this, TaskNode(this, NodeContext().apply(init)))
    }

}