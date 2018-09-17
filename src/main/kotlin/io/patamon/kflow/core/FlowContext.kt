package io.patamon.kflow.core

import io.patamon.kflow.node.End
import io.patamon.kflow.node.Node
import io.patamon.kflow.node.Start
import io.patamon.kflow.node.TaskNode

/**
 * Desc:
 *
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2018/9/12
 */
open class FlowContext {

    /**
     * 存放 ("nodeName" -> "nodeName") 关系
     */
    private val lines = mutableListOf<Pair<String, String>>()

    val start = Start()
    val end = End()

    infix fun Node.to(node: String) {
        lines.add(Pair(this.name, node))
    }

    infix fun String.to(node: Node) {
        lines.add(Pair(this, node.name))
    }

    infix fun String.to(node: String) {
        lines.add(Pair(this, node))
    }

    operator fun String.invoke(init: NodeContext.() -> Unit): Node {
        return TaskNode(this, NodeContext().apply(init))
    }

}