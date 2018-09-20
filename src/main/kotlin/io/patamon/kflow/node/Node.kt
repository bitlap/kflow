package io.patamon.kflow.node

import io.patamon.kflow.core.ExecuteContext

/**
 * Desc:
 *
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2018/9/12
 */
interface Node {

    val name: String
    var type: NodeType

    fun addNext(node: Node)
    fun addPrev(node: Node)

    fun next(): Collection<Node>
    fun prev(): Collection<Node>

    fun hasNext(): Boolean

    fun execute(context: ExecuteContext)

    fun checkCycle(): Boolean
}