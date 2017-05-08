package com.yst.demo

import org.json.simple.JSONArray
import org.json.simple.JSONObject

fun JSONObject.putMany(vararg vls: Pair<String, Any>): JSONObject {
    for (p in vls) {
        put(p.first, p.second)
    }
    return this
}

fun JSONObject.putMany(action: JSONObject.() -> Unit): JSONObject {
    this.action()
    return this
}

fun jsonObject(vararg vls: Pair<String, Any>): JSONObject {
    val res = JSONObject()
    for (p in vls) {
        res.put(p.first, p.second)
    }
    return res
}

fun jsonObject(action: JSONObject.() -> Unit): JSONObject {
    val res = JSONObject()
    action(res)
    return res
}

fun jsonArray(vararg vls: Any): JSONArray {
    val res = JSONArray()
    for (p in vls) {
        res.add(p)
    }
    return res
}

fun <T> jsonArray(data: Iterator<T>, action: (T) -> JSONObject): JSONArray {
    val res= JSONArray()
    while (data.hasNext()) {
        res.add(action(data.next()))
    }
    return res
}

fun <T> jsonArray(data: Iterable<T>, action: (T) -> JSONObject): JSONArray {
    return data.mapTo(JSONArray()) { action(it) }
}

fun jsonArray(action: JSONArray.() -> Unit): JSONArray {
    val res = JSONArray()
    res.action()
    return res
}