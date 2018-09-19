package io.patamon.kflow.core

/**
 * Desc:
 *
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2018/9/12
 */
class NodeContext {

    internal lateinit var handler: () -> Unit

    fun handler(handler: () -> Unit = { }) {
        this.handler = handler
    }

}
