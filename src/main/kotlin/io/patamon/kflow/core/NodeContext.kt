package io.patamon.kflow.core

/**
 * Desc:
 *
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2018/9/12
 */
class NodeContext {

    internal val handlerContext = HandlerContext()

    fun handler(handler: () -> Unit = { }) {
        handlerContext.handler = handler
    }

}

class HandlerContext {

    internal var handler: () -> Unit = { }

    internal fun handle() {
        this.handler.invoke()
    }
}