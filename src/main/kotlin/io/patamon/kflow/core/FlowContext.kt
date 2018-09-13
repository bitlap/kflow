package io.patamon.kflow.core

import io.patamon.kflow.node.End
import io.patamon.kflow.node.Node
import io.patamon.kflow.node.Start

/**
 * Desc:
 *
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2018/9/12
 */
open class FlowContext {

    private val nodeContext = NodeContext()
    val start = Start()
    val end = End()

    infix fun Node.to(node: String) {

    }

    infix fun String.to(node: Node) {

    }

    infix fun String.to(node: String) {

    }

    operator fun String.invoke(init: NodeContext.() -> Unit) {
        nodeContext.apply(init)
    }

}