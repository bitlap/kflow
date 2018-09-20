package io.patamon.kflow.node

import io.patamon.kflow.core.ExecuteContext
import io.patamon.kflow.core.NodeContext

/**
 * Desc:
 *
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2018/9/13
 */
class TaskNode(
        override val name: String,
        private val nodeContext: NodeContext
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
        nodeContext.handler.invoke()

        // 3. execute next nodes
        nextNodes.forEach {
            if (it.prev().size > 1) {
                context.initJoinLocks(this, it.name, it.prev().size)
            }
            executeAsync {
                it.execute(context)
            }
        }
    }

}