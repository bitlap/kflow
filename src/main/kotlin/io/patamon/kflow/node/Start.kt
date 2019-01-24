package io.patamon.kflow.node

import io.patamon.kflow.core.ExecuteContext
import io.patamon.kflow.core.ExecuteResult
import io.patamon.kflow.core.KFlowException
import io.patamon.kflow.core.NodeContext
import io.patamon.kflow.node.NodeType.START

/**
 * Built-in start node
 */
class Start(
        override val name: String = "__START__",
        internal var nodeContext: NodeContext = NodeContext(),
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

    override fun execute(context: ExecuteContext): ExecuteResult {
        // 1. execute start
        this.nodeContext.handler.invoke(context.flowData)

        // 2. execute next nodes
        executeNextNodes(context)

        // return ok
        return ExecuteResult.OK
    }

}