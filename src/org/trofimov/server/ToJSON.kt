package org.trofimov.server

import kotlin.coroutines.experimental.EmptyCoroutineContext.plus

/**
 * Created by ivan on 10.05.17.
 */

fun toJSON(vararg args: Pair<String, String>): String {
    var json = ""
    json += "{"
    for (arg in args) {
        val key = arg.first
        val v = arg.second
        json += """"$key":"$v", """
    }
    json = json.substring(0, json.length - 2)
    json += "}"
    return json
}

//fun toJSONArray(vararg args: String): String {
//    var json = ""
//    json += "["
//    for (arg in args) {
//        json += """"$arg", """
//    }
//    json = json.substring(0, json.length - 2)
//    json += "]"
//    return json
//}

fun toJSONArray(args: List<String>): String {
    var json = ""
    json += "["
    for (arg in args) {
        json += """"$arg", """
    }
    json = json.substring(0, json.length - 2)
    json += "]"
    return json
}