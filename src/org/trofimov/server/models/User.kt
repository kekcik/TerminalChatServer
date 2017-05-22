package org.trofimov.server.models

import org.trofimov.server.helpers.Foo
import org.trofimov.server.helpers.toJSON
import org.trofimov.server.managers.password
import org.trofimov.server.managers.tokenGen
import org.trofimov.server.managers.url
import org.trofimov.server.managers.user
import java.net.URLEncoder
import java.sql.DriverManager

/**
 * Created by ivan on 15.05.17.
 */

class User(var userId: Int?, var login: String, var password: String, var name: String) {
    var token: String = tokenGen()
    fun toPrint(): String {
        var id = 0
        if (userId != null) {
            id = userId!!
        } else {
            id = 0
        }
        return toJSON(
                Foo("userId", id.toString(), false),
                Foo("login", login, true),
                Foo("password", password, true),
                Foo("name", name, true),
                Foo("token", token, true)
        )
    }
}

fun insertUser(user: User) {
    val connection = DriverManager.getConnection(url, org.trofimov.server.managers.user, password)
    val lgn = URLEncoder.encode(user.login, "UTF-8")
    val pwd = URLEncoder.encode(user.password, "UTF-8")
    val nme = URLEncoder.encode(user.name, "UTF-8")
    val tkn = URLEncoder.encode(user.token, "UTF-8")

    val sql = """
                    INSERT INTO User (login, password, name, token)
                    VALUES ('$lgn', '$pwd', '$nme', '$tkn');"""

    val stmt = connection.createStatement()

    stmt.executeUpdate(sql)
    connection.close()
    stmt.close()
}

fun getUsers(): Array<User> {
    var result = arrayOf<User>()
    val connection = DriverManager.getConnection(url, user, password)
    val sql = """
            Select * from User;"""
    val stmt = connection.createStatement()
    val rs = stmt.executeQuery(sql)
    while (rs.next()) {
        val userId = rs.getInt("userId")
        val login = rs.getString("login")
        val password = rs.getString("password")
        val name = rs.getString("name")
        val token = rs.getString("token")
        val user = User(userId, login, password, name)
        user.token = token
        result += user
    }
    connection.close()
    stmt.close()
    return result
}

fun changeTokenFor(user: User): String {
    val token = tokenGen()
    val uId = user.userId
    val connection = DriverManager.getConnection(url, org.trofimov.server.managers.user, password)
    val sql = """
        Update User
        Set token = '$token'
        Where userId = $uId;"""
    val stmt = connection.createStatement()
    stmt.executeUpdate(sql)
    connection.close()
    stmt.close()
    return token
}