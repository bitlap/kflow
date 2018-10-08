package io.patamon.kflow.core

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicInteger

/**
 * When one flow is executing, the only [ExecuteContext] will be through the whole process.
 */
class ExecuteContext(
        flowData: Map<String, Any?> = mutableMapOf()
) {

    /**
     * main thread [CountDownLatch]
     */
    private val mainLatch = CountDownLatch(1)

    /**
     * join node locks
     */
    private val joinLocks = ConcurrentHashMap<String, AtomicInteger>()

    /**
     * when handler get exception, it will not be null
     */
    private var handlerException: Exception? = null

    /**
     * handler execute flow data
     */
    internal val flowData = ConcurrentHashMap<String, Any?>()

    // init code
    init {
        this.flowData.putAll(flowData)
    }

    /**
     * Block current execute thread
     */
    fun await() {
        mainLatch.await()
        if (handlerException != null) {
            throw KFlowException(handlerException!!)
        }
    }

    /**
     * release if with exception
     */
    fun release(e: Exception? = null) {
        this.handlerException = e
        mainLatch.countDown()
    }

    /**
     * current execute context is active
     */
    fun isActive() = this.handlerException == null

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