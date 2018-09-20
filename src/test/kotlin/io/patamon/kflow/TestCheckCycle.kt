package io.patamon.kflow

import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Desc: Check DAG cycle
 */
class TestCheckCycle {

    /**
     * 无环
     * start ---> node1 ---> end
     *   |___________________↑
     */
    @Test
    fun test_0() {
        val f = flow {
            start to "node1"
            "node1" to end

            start to end
        }
        assertFalse(f.getStartNode().checkCycle())
    }

    /**
     * 无环
     * start ---> node1 ---> node2 ---> node3 ---> end
     *                         |___________________↑
     */
    @Test
    fun test_1() {
        val f = flow {
            start to "node1"
            "node1" to "node2"
            "node2" to "node3"
            "node3" to end

            "node2" to end
        }
        assertFalse(f.getStartNode().checkCycle())
    }

    /**
     * 有环
     *              ↓ˉˉˉˉˉˉˉˉˉˉˉˉˉˉˉˉˉˉˉˉˉ|
     * start ---> node1 ---> node2 ---> node3 ---> end
     *                         |___________________↑
     */
    @Test
    fun test_2() {
        val f = flow {
            start to "node1"
            "node1" to "node2"
            "node2" to "node3"
            "node3" to end

            "node3" to "node1"
            "node2" to end
        }
        assertTrue(f.getStartNode().checkCycle())
    }

    /**
     * 无环
     *
     * start --->  node1 ---> node2 ---> node3 ---->  end
     *              |                                  ↑
     *              |       |-----> f_node1 ---        |
     *              |---> f_node              |---> j_node
     *                      |-----> f_node2 ---
     */
    @Test
    fun test_3() {
        val f = flow {
            start to "node1"
            "node1" to "node2"
            "node2" to "node3"
            "node3" to end
            // fork
            "node1" to "f_node"
            "f_node" to "f_node1"
            "f_node" to "f_node2"
            // join
            "f_node1" to "j_node"
            "f_node2" to "j_node"

            "j_node" to end
        }
        assertFalse(f.getStartNode().checkCycle())
    }
}