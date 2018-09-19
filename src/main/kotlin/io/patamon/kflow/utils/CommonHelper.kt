package io.patamon.kflow.utils


/**
 * add new kv
 *
 * @return return the added value, opposite to [MutableMap.put]
 */
internal fun <K, V> MutableMap<K, V>.add(k: K, v: V): V {
    this[k] = v
    return v
}