package io.patamon.kflow.utils


/**
 * 新增元素
 *
 * @return 返回新添加的元素, 和 put 相反
 */
internal fun <K, V> MutableMap<K, V>.add(k: K, v: V): V {
    this[k] = v
    return v
}