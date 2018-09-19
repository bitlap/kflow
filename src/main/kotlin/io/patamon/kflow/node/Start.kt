package io.patamon.kflow.node

import io.patamon.kflow.core.ExecuteContext

/**
 * Desc:
 *
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2018/9/12
 */
class Start(
        override val name: String = "__START__"
) : BaseNode() {

    override fun execute(context: ExecuteContext) {
        nextNodes.forEach {
            if (it.prev().size > 1) {
                context.initJoinLocks(it.name, it.prev().size)
            }
            executeAsync {
                it.execute(context)
            }
        }
    }

}