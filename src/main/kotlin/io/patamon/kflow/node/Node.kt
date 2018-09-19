package io.patamon.kflow.node

/**
 * Desc:
 *
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2018/9/12
 */
interface Node {

    val name: String

    fun addNext(node: Node)
    fun addPrev(node: Node)

    fun next(): Collection<Node>

    fun hasNext(): Boolean

    fun execute()
}