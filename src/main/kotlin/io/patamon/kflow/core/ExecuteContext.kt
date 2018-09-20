package io.patamon.kflow.core

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicInteger

/**
 * Desc: 执行上下文
 *
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2018/9/19
 */
class ExecuteContext {

    private val mainLatch = CountDownLatch(1)
    private val joinLocks = ConcurrentHashMap<String, AtomicInteger>()

    fun await() {
        mainLatch.await()
    }

    fun release() {
        mainLatch.countDown()
    }

    fun initJoinLocks(lock: Any, name: String, joinNum: Int) {
       synchronized(lock) {
           if (joinLocks.containsKey(name)) {
               return
           }
           joinLocks[name] = AtomicInteger(joinNum)
       }
    }

    fun countDownJoinLocks(name: String): Boolean {
        if (!joinLocks.containsKey(name)) {
            return false
        }
        if (joinLocks[name]!!.get() == 0) {
            joinLocks.remove(name)
            return false
        }
        return joinLocks[name]!!.decrementAndGet() > 0
    }

}