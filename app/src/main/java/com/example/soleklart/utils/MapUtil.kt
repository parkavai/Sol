package com.example.soleklart.utils


/**
 * filters out key value pairs where values are null
 */
fun <K, V> Map<K, V?>.filterNotNullValues(): Map<K, V> =
    mapNotNull { (key, value) -> value?.let { key to it } }.toMap()