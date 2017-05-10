package org.trofimov.server

import java.security.SecureRandom
import java.util.*
import java.util.stream.Stream
import javax.naming.Name

/**
 * Created by ivan on 24.04.17.
 */


private fun tokenGen(): String {
    val alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_"
    return "0123456789123456"
            .map { _ -> alphabet[SplittableRandom().nextInt(64)] }
            .joinToString(separator = "")
}

private class User {
    var login: String
    var password: String
    var name: String
    var token: String

    constructor(login: String, password: String, name: String, token: String) {
        this.login = login
        this.password = password
        this.name = name
        this.token = token
    }

    constructor(login: String, password: String, name: String) {
        this.login = login
        this.password = password
        this.name = name
        this.token = tokenGen()
    }

    fun toPrint(): String {
        return toJSON(
                Pair("login", login),
                Pair("password", password),
                Pair("name", name),
                Pair("token", token)

        )
    }
}

private var users = mutableMapOf<String, User>()

fun getLoginBy(token: String): String {
    for (user: MutableMap.MutableEntry<String, User> in users) {
        if (user.value.token == token) {
            return user.key
        }
    }
    return ""
}

fun putUser(login: String, password: String, name: String): String {
    if (users.containsKey(login))
        return toJSON(
                Pair("code", Errors.LOGIN_ALREADY_USED.code.toString()),
                Pair("token", "")
        )
    users.put(login, User(login, password, name))
    return toJSON(
            Pair("code", Errors.OK.code.toString()),
            Pair("token", users[login]!!.token)
    )
}

fun checkUser(login: String, password: String): String {
    if (!users.containsKey(login))
        return toJSON(
                Pair("code", Errors.LOGIN_NOT_FOUND.code.toString()),
                Pair("token", ""))
    else if (users[login]!!.password != password)
        return toJSON(
                Pair("code", Errors.WRONG_PASSWORD.code.toString()),
                Pair("token", ""))
    else
        return toJSON(
                Pair("code", Errors.OK.code.toString()),
                Pair("token", users[login]!!.token))
}

fun getUsers(): String {
    val values = users.values
    return toJSON(Pair("users", toJSONArray(values.map(User::toPrint))))
}