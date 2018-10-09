package io.patamon.kflow

import io.patamon.kflow.core.KFlowException
import org.junit.Test

/**
 * Desc: Check DAG cycle
 */
class TestCheckCycle {

    /**
     * no cycle
     *
     * start ---> node1 ---> end
     *   |___________________↑
     */
    @Test
    fun test_0() {
        flow {
            start to "node1"
            "node1" to end

            start to end
        }
    }

    /**
     * no cycle
     *
     * start ---> node1 ---> node2 ---> node3 ---> end
     *                         |___________________↑
     */
    @Test
    fun test_1() {
        flow {
            start to "node1"
            "node1" to "node2"
            "node2" to "node3"
            "node3" to end

            "node2" to end
        }
    }

    /**
     * has cycle
     *
     *              ↓ˉˉˉˉˉˉˉˉˉˉˉˉˉˉˉˉˉˉˉˉˉ|
     * start ---> node1 ---> node2 ---> node3 ---> end
     *                         |___________________↑
     */
    @Test(expected = KFlowException::class)
    fun test_2() {
        flow {
            start to "node1"
            "node1" to "node2"
            "node2" to "node3"
            "node3" to end

            "node3" to "node1"
            "node2" to end
        }
    }

    /**
     * no cycle
     *
     * start --->  node1 ---> node2 ---> node3 ---->  end
     *              |                                  ↑
     *              |       |-----> f_node1 ---        |
     *              |---> f_node              |---> j_node
     *                      |-----> f_node2 ---
     */
    @Test
    fun test_3() {
        flow {
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
    }


    /**
     * no cycle
     *
     *               |-----> f_node1 ---        |-----> fj_node1 ---
     * start ---> f_node               |---> fj_node               |---> j_node ---> end
     *               |-----> f_node2 ---        |-----> fj_node2 ---
     *
     *
     *
     */
    @Test
    fun test_4() {
        flow {
            // fork/join
            start to "f_node"
            "f_node" to "f_node1"
            "f_node" to "f_node2"
            "f_node1" to "fj_node"
            "f_node2" to "fj_node"
            // fork/join
            "fj_node" to "fj_node1"
            "fj_node" to "fj_node2"
            "fj_node1" to "j_node"
            "fj_node2" to "j_node"

            "j_node" to end
        }
    }

    /**
     * has cycle
     *
     * start ---> node1 ---> end
     *   ↑___________________|
     */
    @Test(expected = KFlowException::class)
    fun test_5() {
        flow {
            start to "node1"
            "node1" to end
            end to start
        }
    }
}