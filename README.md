# KFlow

Kotlin DSL for DAG-Flow execute engine.

## 1. Examples

* Simple example

```kotlin
// start ---> node1 ---> node2 ---> end
val flow = flow {
    // flow lines
    start to "node1"
    "node1" to "node2"
    "node2" to end

    // node handler
    "node1" {
        handler {
            println("${Thread.currentThread().name} -> node1 handle")
        }
    }
    "node2" {
        handler {
            println("${Thread.currentThread().name} -> node2 handle")
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

* [ ] `Execute context` user defined params.
* [ ] `Execute context` other build-in features.
* [ ] TODO
