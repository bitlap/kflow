package io.patamon.kflow.utils


/**
 * Add new kv
 *
 * @return return the added value, opposite to [MutableMap.put]
 */
internal fun <K, V> MutableMap<K, V>.add(k: K, v: V): V {
    this[k] = v
    return v
}

/**
 * If Collection has more than one elements
 */
internal fun Collection<Any>.moreThanOne() = this.size > 1