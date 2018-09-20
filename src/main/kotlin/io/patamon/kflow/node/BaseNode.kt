package io.patamon.kflow.node

import io.patamon.kflow.node.NodeType.FORK
import io.patamon.kflow.node.NodeType.FORK_JOIN
import io.patamon.kflow.node.NodeType.JOIN
import io.patamon.kflow.node.WalkStatus.INIT
import io.patamon.kflow.node.WalkStatus.VISITED
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch

/**
 * Desc:
 *
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2018/9/17
 */
abstract class BaseNode : Node {


    protected val nextNodes = mutableSetOf<Node>()
    protected val prevNodes = mutableSetOf<Node>()

    override fun addNext(node: Node) {
        this.nextNodes.add(node)
        when {
            this.nextNodes.size > 1 && this.prevNodes.size > 1 -> this.type = FORK_JOIN
            this.nextNodes.size > 1 -> this.type = FORK
        }
    }

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
                    && hasCycle(DAGNode(it, INIT), walking, visited)) {
                return true
            }
        }
        dagNode.status = VISITED
        visited[dagNode.node.name] = dagNode
        walking.remove(dagNode.node.name)
        return false
    }

    protected fun executeAsync(func: () -> Unit) {
        GlobalScope.launch {
            func.invoke()
        }
    }
}

enum class NodeType {
    START, END, FORK, JOIN, FORK_JOIN, TASK
}

data class DAGNode(val node: Node, var status: WalkStatus)

enum class WalkStatus {
    INIT, VISITING, VISITED
}
