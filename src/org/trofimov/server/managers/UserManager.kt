package org.trofimov.server.managers
import org.trofimov.server.helpers.Errors
import org.trofimov.server.helpers.Foo
import org.trofimov.server.models.User
import org.trofimov.server.models.changeTokenFor
import org.trofimov.server.models.getUsers
import org.trofimov.server.models.insertUser
import org.trofimov.server.helpers.toJSON
import org.trofimov.server.helpers.toJSONArray
import java.util.*

/**
 * Created by ivan on 24.04.17.
 */

internal fun tokenGen(): String {
    val alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_"
    return "0123456789123456"
            .map { _ -> alphabet[SplittableRandom().nextInt(64)] }
            .joinToString(separator = "")
}

fun getLoginBy(token: String): User? {
    getUsers()

            .filter { it.token == token }
            .forEach{ return it}
    return null
}

fun putUser(login: String, password: String, name: String): String {
    val users = getUsers()
    users
            .filter { it.login == login }
            .forEach {
                return toJSON(
                        Foo("code", Errors.LOGIN_ALREADY_USED.code.toString(), false),
                        Foo("token", "", true)
                )
            }
    val user = User(null, login, password, name)
    insertUser(user)
    return toJSON(
            Foo("code", Errors.OK.code.toString(), false),
            Foo("token", user.token, true)
    )
}

fun checkUser(login: String, password: String): String {
    val users = getUsers()
    var user : User? = null
    users
            .filter { it.login == login }
            .forEach{ user = it }
    if (user == null)
        return toJSON(
                Foo("code", Errors.LOGIN_NOT_FOUND.code.toString(), false),
                Foo("token", "", true))
    else if (user!!.password != password)
        return toJSON(
                Foo("code", Errors.WRONG_PASSWORD.code.toString(), false),
                Foo("token", "", true))
    else
        return toJSON(
                Foo("code", Errors.OK.code.toString(), false),
                Foo("token", changeTokenFor(user!!), true))
}

fun getUsersOld(): String {
    val users = getUsers()
    return toJSON(Foo("users", toJSONArray(users.map(User::toPrint)), false))
}