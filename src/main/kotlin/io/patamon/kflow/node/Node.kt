package io.patamon.kflow.node

import io.patamon.kflow.core.ExecuteContext
import io.patamon.kflow.core.ExecuteResult

/**
 * Node in one flow
 */
interface Node {

    /**
     * node name
     */
    val name: String

    /**
     * node type
     */
    var type: NodeType

    /**
     * add next/prev node
     */
    fun addNext(node: Node)
    fun addPrev(node: Node)

    /**
     * get next/prev nodes
     */
    fun next(): Collection<Node>
    fun prev(): Collection<Node>

    /**
     * if has next nodes
     */
    fun hasNext(): Boolean

    /**
     * execute current node
     */
    fun execute(context: ExecuteContext): ExecuteResult

    /**
     * check whether one node (and next nodes) have cycle
     */
    fun checkCycle(): Boolean
}