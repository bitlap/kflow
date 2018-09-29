package io.patamon.kflow.node

import io.patamon.kflow.core.ExecuteContext
import io.patamon.kflow.core.NodeContext
import io.patamon.kflow.node.NodeType.TASK
import io.patamon.kflow.utils.moreThanOne

/**
 * Task node defined by user
 */
class TaskNode(
        override val name: String,
        private val nodeContext: NodeContext,
        override var type: NodeType = TASK
) : BaseNode() {

    companion object {
        fun empty(name: String) = TaskNode(name, NodeContext())
    }

    override fun execute(context: ExecuteContext) {
        // 1. check current join nodes
        if (context.countDownJoinLocks(name)) {
            return
        }
        // 2. execute node
        nodeContext.handler.invoke(context.flowData)

        // 3. execute next nodes
        nextNodes.forEach {
            if (it.prev().moreThanOne()) {
                context.initJoinLocks(this, it.name, it.prev().size)
            }
            executeAsync {
                it.execute(context)
            }
        }
    }

}