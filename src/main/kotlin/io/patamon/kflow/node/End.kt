package io.patamon.kflow.node

import io.patamon.kflow.core.ExecuteContext

/**
 * Desc:
 *
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2018/9/12
 */
class End(
        override val name: String = "__END__"
) : BaseNode() {

    override fun execute(context: ExecuteContext) {
        // 1. check current join nodes
        if (context.countDownJoinLocks(name)) {
            return
        }

        // 2. release main thread
        context.release()
    }

}