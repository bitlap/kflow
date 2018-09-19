package io.patamon.kflow.node

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

    override fun execute() {
        nodeContext.handler.invoke()
    }

}