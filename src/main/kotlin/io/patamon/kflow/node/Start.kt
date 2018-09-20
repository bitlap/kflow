package io.patamon.kflow.node

import io.patamon.kflow.core.ExecuteContext
import io.patamon.kflow.node.NodeType.FORK_JOIN
import io.patamon.kflow.node.NodeType.JOIN
import io.patamon.kflow.node.NodeType.START

/**
 * Built-in start node
 */
class Start(
        override val name: String = "__START__",
        override var type: NodeType = START
) : BaseNode() {

    override fun execute(context: ExecuteContext) {
        nextNodes.forEach {
            if (it.type == JOIN || it.type == FORK_JOIN) {
                context.initJoinLocks(this, it.name, it.prev().size)
            }
            executeAsync {
                it.execute(context)
            }
        }
    }

}