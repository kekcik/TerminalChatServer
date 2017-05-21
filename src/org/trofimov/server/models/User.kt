package org.trofimov.server.models

import org.trofimov.server.*
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
        return toJSON(
                Foo("login", URLEncoder.encode(login, "UTF-8"), true),
                Foo("password", URLEncoder.encode(password, "UTF-8"), true),
                Foo("name", URLEncoder.encode(name, "UTF-8"), true),
                Foo("token", URLEncoder.encode(token, "UTF-8"), true)
        )
    }
}

fun insertUser(user: User) {
    val connection = DriverManager.getConnection(url, org.trofimov.server.managers.user, password)
    val lgn = user.login
    val pwd = user.password
    val nme = user.name
    val tkn = user.token

    val sql = """
                    INSERT INTO User (login, password, name, token)
                    VALUES ('$lgn', '$pwd', '$nme', '$tkn');"""

    println(sql)

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
    println(sql)

    val stmt = connection.createStatement()
    stmt.executeUpdate(sql)
    connection.close()
    stmt.close()
    return token
}