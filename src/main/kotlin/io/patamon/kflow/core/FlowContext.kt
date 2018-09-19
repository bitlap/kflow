package io.patamon.kflow.core

import io.patamon.kflow.node.End
import io.patamon.kflow.node.Node
import io.patamon.kflow.node.Start
import io.patamon.kflow.node.TaskNode
import io.patamon.kflow.utils.add

/**
 * Desc:
 *
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2018/9/12
 */
open class FlowContext {

    val start = Start()
    val end = End()

    /**
     * 存放 "nodeName" -> "nodeName" 连线关系
     */
    private val lines = mutableMapOf<String, String>()

    /**
     * 存放节点名称对应的节点对象
     */
    private val nodeMap = mutableMapOf<String, Node>(
            Pair(start.name, start),
            Pair(end.name, end)
    )

    infix fun Node.to(node: String) {
        lines[this.name] = node
    }

    infix fun String.to(node: Node) {
        lines[this] = node.name
    }

    infix fun String.to(node: String) {
        lines[this] = node
    }

    operator fun String.invoke(init: NodeContext.() -> Unit): Node {
        return nodeMap.add(this, TaskNode(this, NodeContext().apply(init)))
    }

    internal fun initialize(init: FlowContext.() -> Unit): FlowContext {
        this.apply(init)
        // init flow
        lines.forEach { from, to ->
            val fromNode = nodeMap[from] ?: nodeMap.add(from, TaskNode.empty(from))
            val toNode = nodeMap[to] ?: nodeMap.add(to, TaskNode.empty(to))
            fromNode.addNext(toNode)
            toNode.addPrev(fromNode)
        }
        // TODO: check cycle
        return this
    }

    internal fun exec() {
        exec(start)
    }

    private fun exec(node: Node) {
        node.execute()
        if (node.hasNext()) {
            node.next().forEach(::exec)
        }
    }
}