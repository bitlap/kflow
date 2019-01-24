package io.patamon.kflow.node

import io.patamon.kflow.core.ExecuteContext
import io.patamon.kflow.core.ExecuteResult
import io.patamon.kflow.core.KFlowException
import io.patamon.kflow.core.NodeContext
import io.patamon.kflow.node.NodeType.END

/**
 * Built-in end node
 */
class End(
        override val name: String = "__END__",
        internal var nodeContext: NodeContext = NodeContext(),
        override var type: NodeType = END
) : BaseNode() {

    /**
     * end node should not have next node
     */
    override fun addNext(node: Node) {
        throw KFlowException("end node should not have next node of [${node.name}].")
    }

    /**
     * just add prev
     */
    override fun addPrev(node: Node) {
        this.prevNodes.add(node)
    }

    override fun execute(context: ExecuteContext): ExecuteResult {
        // 1. check current join nodes
        if (context.countDownJoinLocks(name)) {
            return ExecuteResult.IGNORE
        }

        // 2. release main thread
        context.release()

        // 3. execute end
        this.nodeContext.handler.invoke(context.flowData)

        // return ok
        return ExecuteResult.OK
    }

}