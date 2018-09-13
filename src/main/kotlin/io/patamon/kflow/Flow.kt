package io.patamon.kflow

import io.patamon.kflow.core.FlowContext

/**
 * Desc:
 *
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2018/9/12
 */
fun flow(init: FlowContext.() -> Unit): Flow {
    val context = FlowContext().apply(init)
    return Flow(context)
}

class Flow(context: FlowContext) {





}


fun main(args: Array<String>) {
    val flow = flow {
        start to "node1"
        "node1" to end

        "node1" {
            handler {
                println("node1 handle")
            }
        }
    }



}