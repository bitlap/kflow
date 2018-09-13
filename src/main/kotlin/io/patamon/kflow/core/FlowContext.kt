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

    val start = Start()
    val end = End()

    infix fun Node.to(node: String) {

    }

    infix fun String.to(node: Node) {

    }

    infix fun String.to(node: String) {

    }

    operator fun String.invoke(init: NodeContext.() -> Unit): Node {
        return TaskNode(NodeContext().apply(init))
    }

}