package org.trofimov.server
import java.util.*
/**
 * Created by ivan on 24.04.17.
 */

private fun tokenGen(): String {
    val alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_"
    return "0123456789123456"
            .map { _ -> alphabet[SplittableRandom().nextInt(64)] }
            .joinToString(separator = "")
}

private class User(var login: String, var password: String, var name: String) {
    var token: String = tokenGen()

    fun toPrint(): String {
        return toJSON(
                Foo("login", login, true),
                Foo("password", password, true),
                Foo("name", name, true),
                Foo("token", token, true)
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
                Foo("code", Errors.LOGIN_ALREADY_USED.code.toString(), false),
                Foo("token", "", true)
        )
    users.put(login, User(login, password, name))
    return toJSON(
            Foo("code", Errors.OK.code.toString(), false),
            Foo("token", users[login]!!.token, true)
    )
}

fun checkUser(login: String, password: String): String {
    if (!users.containsKey(login))
        return toJSON(
                Foo("code", Errors.LOGIN_NOT_FOUND.code.toString(), false),
                Foo("token", "", true))
    else if (users[login]!!.password != password)
        return toJSON(
                Foo("code", Errors.WRONG_PASSWORD.code.toString(), false),
                Foo("token", "", true))
    else
        return toJSON(
                Foo("code", Errors.OK.code.toString(), false),
                Foo("token", users[login]!!.token, true))
}

fun getUsers(): String {
    val values = users.values
    return toJSON(Foo("users", toJSONArray(values.map(User::toPrint)), false))
}