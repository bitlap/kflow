package io.patamon.kflow.node

import io.patamon.kflow.core.ExecuteContext
import io.patamon.kflow.node.NodeType.FORK
import io.patamon.kflow.node.NodeType.FORK_JOIN
import io.patamon.kflow.node.NodeType.JOIN
import io.patamon.kflow.node.NodeType.START
import io.patamon.kflow.node.WalkStatus.INIT
import io.patamon.kflow.node.WalkStatus.VISITED
import io.patamon.kflow.utils.moreThanOne
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.isActive
import kotlinx.coroutines.experimental.launch

/**
 * Base abstract node
 */
abstract class BaseNode : Node {

    /**
     * prev nodes and next nodes
     */
    protected val prevNodes = mutableSetOf<Node>()
    protected val nextNodes = mutableSetOf<Node>()

    /**
     * add next node
     */
    override fun addNext(node: Node) {
        this.nextNodes.add(node)
        when {
            this.nextNodes.size > 1 && this.prevNodes.size > 1 -> this.type = FORK_JOIN
            this.nextNodes.size > 1 -> this.type = FORK
        }
    }

    /**
     * add prev node
     */
    override fun addPrev(node: Node) {
        this.prevNodes.add(node)
        when {
            this.nextNodes.size > 1 && this.prevNodes.size > 1 -> this.type = FORK_JOIN
            this.prevNodes.size > 1 -> this.type = JOIN
        }
    }

    override fun next(): Collection<Node> = nextNodes

    override fun prev(): Collection<Node> = prevNodes

    override fun hasNext(): Boolean = this.nextNodes.isNotEmpty()

    /**
     * check flow if has circular dependencies, using DFS(Depth First Search)
     */
    override fun checkCycle(): Boolean {
        return hasCycle(DAGNode(this, INIT), mutableListOf(), mutableMapOf())
    }

    private fun hasCycle(dagNode: DAGNode, walking: MutableList<String>, visited: MutableMap<String, DAGNode>): Boolean {
        if (walking.contains(dagNode.node.name)) {
            return true
        }
        walking.add(dagNode.node.name)
        for (it in dagNode.node.next()) {
            if ((visited[it.name] == null || visited[it.name]!!.status == INIT)
                    // if cycle, return true
                    && hasCycle(DAGNode(it, INIT), walking, visited)) {
                return true
            }
        }
        // Remove current node
        walking.remove(dagNode.node.name)
        // And add to visited map
        dagNode.status = VISITED
        visited[dagNode.node.name] = dagNode

        return false
    }

    /**
     * execute next nodes
     */
    protected fun executeNextNodes(context: ExecuteContext) {
        this.nextNodes.forEach {
            if (it.prev().moreThanOne()) {
                context.initJoinLocks(it, it.name, it.prev().size)
            }
            // execute node
            // if next nodes size is 1, execute synchronously
            executeNode(context, it, this.nextNodes.size != 1 || this.type == START)
        }
    }

    // execute node
    private fun executeNode(context: ExecuteContext, node: Node, async: Boolean) {
        // execute function
        val exec = {
            if (context.isActive()) {
                val result = node.execute(context)
                if (result.hasError()) {
                    context.release(result.exception!!)
                }
            }
        }

        // asynchronously
        if (async) {
            GlobalScope.launch {
                if (isActive) {
                    exec()
                }
            }
        }
        // synchronously
        else {
            exec()
        }
    }
}

/**
 * Node type
 */
enum class NodeType {
    /**
     * START, no prev
     */
    START,

    /**
     * END, no next
     */
    END,

    /**
     * Common task node
     */
    TASK,

    /**
     * TASK node with multiple next nodes
     */
    FORK,

    /**
     * TASK node with multiple prev nodes
     */
    JOIN,

    /**
     * TASK node with multiple prev nodes, multiple next nodes
     */
    FORK_JOIN,
}

/**
 * Simple dag node object, using by checking circular dependencies
 */
data class DAGNode(val node: Node, var status: WalkStatus)

/**
 * Checking circular dependencies status
 */
enum class WalkStatus {

    /**
     * Current node is initial
     */
    INIT,

    /**
     * Current node is visiting
     */
    VISITING,

    /**
     * Current node has been visited
     */
    VISITED,
}
