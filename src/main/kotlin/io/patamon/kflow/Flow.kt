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
    return Flow(FlowContext().initialize(init))
}

class Flow(private val context: FlowContext) {

    fun execute() {
        context.exec()
    }

}
