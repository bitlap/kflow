# KFlow

Kotlin DSL for DAG-Flow execute engine.

## 1. Examples

* Quick start

```kotlin
// start ---> node1 ---> node2 ---> end
val flow = flow {
    // flow lines
    start to "node1"
    "node1" to "node2"
    "node2" to end

    // node handler
    "node1" {
        handler { flowData ->
            flowData["node1"] = "node1Data"
            println("${Thread.currentThread().name} -> node1 handle")
        }
    }

    "node2" {
        handler { flowData ->
            println("${Thread.currentThread().name} -> node2 handle, get node1 data ${flowData["node1"]}")
        }
    }
}
flow.execute()
```

* ForkJoin example

```kotlin
/**
 * start ---> node1 ------> node2 ---> node3 ----> end
 *              |                                  ↑
 *              |       |-----> f_node1 ---        |
 *              |---> f_node              |---> j_node
 *                      |-----> f_node2 ---
 */
```

You can reference test case.

## 2. TODO

* [ ] `Execute context` other build-in features.
* [ ] TODO
