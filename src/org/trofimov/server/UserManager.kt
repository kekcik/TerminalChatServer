package org.trofimov.server

import java.security.SecureRandom
import java.util.*
import java.util.stream.Stream
import javax.naming.Name

/**
 * Created by ivan on 24.04.17.
 */

private enum class Errors(val code: Int) {
    OK(0),
    WRONG_PASSWORD(10),
    CHAT_NAME_ALREADY_USED(20),
    WRONG_TOKEN(30),
    CHAT_NO_FOUND(40),
    LOGIN_ALREADY_USED(50),
    LOGIN_NOT_FOUND(60)
}

private fun tokenGen(): String {
    val alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_"
    return "0123456789123456"
            .map { _ -> alphabet[SplittableRandom().nextInt(64)]}
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
}

private var users = mutableMapOf<String, User>()

fun putUser(login: String, password: String, name: String): Pair<Int, String> {
    if (users.containsKey(login))
        return Pair(Errors.LOGIN_ALREADY_USED.code, "")
    users.put(login, User(login, password, name))
    return Pair(Errors.OK.code, users[login]!!.token)
}

fun checkUser(login: String, password: String): Pair<Int, String> {
    if (!users.containsKey(login))
        return Pair(Errors.LOGIN_NOT_FOUND.code, "")
    if (users[login]!!.password != password)
        return Pair(Errors.WRONG_PASSWORD.code, "")
    else
        return Pair(Errors.OK.code, users[login]!!.token)
}

fun getUsers(): String {
    return users.toString()
}