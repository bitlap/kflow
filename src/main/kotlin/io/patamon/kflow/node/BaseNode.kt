package io.patamon.kflow.node

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
    }

    override fun addPrev(node: Node) {
        this.prevNodes.add(node)
    }

    override fun next(): Collection<Node> = nextNodes

    override fun hasNext(): Boolean = this.nextNodes.isNotEmpty()

    override fun execute() {
        // default
    }
}

enum class NodeType {

}