package io.patamon.kflow

import org.junit.Test

/**
 * Test simple flow
 */
class TestFlow {

    @Test
    fun testSimpleFlow() {
        val flow = flow {
            start to "node1"
            "node1" to "node2"
            "node2" to end

            "node1" {
                handler {
                    println("node1 handle")
                }
            }

            "node2" {
                handler {
                    println("node2 handle")
                }
            }
        }
        flow.execute()
    }

    @Test
    fun testForkJoinFlow() {
        val flow = flow {
            start to "node1"
            "node1" to "f1_node1"
            "node1" to "f2_node1"

            "f1_node1" to "f1_node2"
            "f1_node2" to end

            "f2_node1" to "ff1_node1"
            "f2_node1" to "ff1_node2"

            "ff1_node1" to "jj1_node"
            "ff1_node2" to "jj1_node"

            "jj1_node" to end

            "node1" {
                handler {
                    println("node1 handle")
                }
            }

            "f1_node1" {
                handler {
                    Thread.sleep(1000)
                    println("f1_node1 handle")
                }
            }

            "f1_node2" {
                handler {
                    println("f1_node2 handle")
                }
            }

            "f2_node1" {
                handler {
                    println("f2_node1 handle")
                }
            }

            "ff1_node1" {
                handler {
                    println("ff1_node1 handle")
                }
            }

            "ff1_node2" {
                handler {
                    println("ff1_node2 handle")
                }
            }

            "jj1_node" {
                handler {
                    println("jj1_node handle")
                }
            }
        }
        flow.execute()
    }
}