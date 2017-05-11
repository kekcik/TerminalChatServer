package org.trofimov.server

import kotlin.coroutines.experimental.EmptyCoroutineContext.plus

/**
 * Created by ivan on 10.05.17.
 */

data class Foo(val name: String, val content: String, val isString: Boolean)

fun toJSON(vararg args: Foo): String {
    var json = ""
    json += "{"
    for (arg in args) {
        val key = arg.name
        val v = arg.content
        if (arg.isString) {
            json += """"$key":"$v", """
        } else {
            json += """"$key":$v, """
        }
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
        if (arg[0] == '{') {
            json += """$arg, """
        } else {
            json += """"$arg", """
        }
    }
    if (args.isNotEmpty()) {
        json = json.substring(0, json.length - 2)
    }
    json += "]"
    return json
}