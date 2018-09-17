package io.patamon.kflow.node

/**
 * Desc:
 *
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2018/9/17
 */
abstract class BaseNode : Node {


    protected val nextNodes = mutableListOf<Node>()
    protected val prevNodes = mutableListOf<Node>()




}

enum class NodeType {

}