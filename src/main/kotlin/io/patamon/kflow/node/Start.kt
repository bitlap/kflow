package io.patamon.kflow.node

import io.patamon.kflow.core.ExecuteContext
import io.patamon.kflow.core.KFlowException
import io.patamon.kflow.node.NodeType.START
import io.patamon.kflow.utils.moreThanOne

/**
 * Built-in start node
 */
class Start(
        override val name: String = "__START__",
        override var type: NodeType = START
) : BaseNode() {

    /**
     * just add next
     */
    override fun addNext(node: Node) {
        this.nextNodes.add(node)
    }

    /**
     * Start node should not have prev node
     */
    override fun addPrev(node: Node) {
        throw KFlowException("start node should not have prev node of [${node.name}].")
    }

    override fun execute(context: ExecuteContext) {
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