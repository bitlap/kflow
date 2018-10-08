package io.patamon.kflow.node

import io.patamon.kflow.core.ExecuteContext
import io.patamon.kflow.core.ExecuteResult
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

    override fun execute(context: ExecuteContext): ExecuteResult {
        // 1. check current join nodes
        if (context.countDownJoinLocks(name)) {
            return ExecuteResult.IGNORE
        }

        // 2. execute node
        try {
            nodeContext.handler.invoke(context.flowData)
        } catch (e: Exception) {
            // return error
            return ExecuteResult.withError(e)
        }

        // 3. execute next nodes
        // TODO: if size is one, execute in current thread?
        nextNodes.forEach {
            if (it.prev().moreThanOne()) {
                context.initJoinLocks(it, it.name, it.prev().size)
            }
            // execute node
            executeAsync(context, it)
        }

        // return ok
        return ExecuteResult.OK
    }

}