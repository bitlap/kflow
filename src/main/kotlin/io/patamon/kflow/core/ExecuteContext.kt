package io.patamon.kflow.core

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicInteger

/**
 * When one flow is executing, the only [ExecuteContext] will be through the whole process.
 */
class ExecuteContext(
        flowData: Map<String, Any?> = mutableMapOf(),
        private val mainThread: Thread = Thread.currentThread()
) {

    private val mainLatch = CountDownLatch(1)
    private val joinLocks = ConcurrentHashMap<String, AtomicInteger>()
    internal val flowData = ConcurrentHashMap<String, Any?>()

    init {
        this.flowData.putAll(flowData)
    }

    /**
     * Block current execute thread
     */
    fun await() {
        mainLatch.await()
    }

    /**
     * Release current execute thread
     */
    fun release() {
        mainLatch.countDown()
    }

    /**
     * Init join node locks
     */
    fun initJoinLocks(lock: Any, name: String, joinNum: Int) {
       synchronized(lock) {
           if (joinLocks.containsKey(name)) {
               return
           }
           joinLocks[name] = AtomicInteger(joinNum)
       }
    }

    /**
     * Count down join node locks
     */
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