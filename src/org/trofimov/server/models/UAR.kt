package org.trofimov.server.models

import org.trofimov.server.helpers.Foo
import org.trofimov.server.helpers.toJSON
import org.trofimov.server.managers.password
import org.trofimov.server.managers.url
import org.trofimov.server.managers.user
import java.sql.DriverManager

/**
 * Created by ivan on 16.05.17.
 */



class UAR(var uarId: Int?, var userId: Int, var roomId: Int) {
    fun toPrint(): String {
        return toJSON(
                Foo("userId", userId.toString(), true),
                Foo("roomId", roomId.toString(), true)
        )
    }
}

fun insertUAR(uar: UAR) {
    val connection = DriverManager.getConnection(url, user, password)
    val uId = uar.userId
    val rId = uar.roomId
    val sql = """
    INSERT INTO UserAssignRoom (userId, roomId)
    VALUES ($uId, $rId);"""
    val stmt = connection.createStatement()
    val rs = stmt.executeUpdate(sql)
    println(rs.toString() + " -- rs")
    connection.close()
    stmt.close()
}

fun getUARs(): Array<UAR> {
    var result = arrayOf<UAR>()
    val connection = DriverManager.getConnection(url, user, password)
    val sql = """
    Select * from UserAssignRoom;"""
    val stmt = connection.createStatement()
    val rs = stmt.executeQuery(sql)
    while (rs.next()) {
        val uarId = rs.getInt("uarId")
        val userId = rs.getInt("userId")
        val roomId = rs.getInt("roomId")
        result += UAR(uarId, userId, roomId)
    }
    connection.close()
    stmt.close()
    return result
}

fun getUARsForUser(userId: Int): Array<UAR> {
    var result = arrayOf<UAR>()
    val connection = DriverManager.getConnection(url, user, password)
    val sql = """
    Select * from UserAssignRoom where userId = '$userId';"""
    val stmt = connection.createStatement()
    val rs = stmt.executeQuery(sql)
    while (rs.next()) {
        val uarId = rs.getInt("uarId")
        val userId = rs.getInt("userId")
        val roomId = rs.getInt("roomId")
        result += UAR(uarId, userId, roomId)
    }
    connection.close()
    return result
}

fun getUARsForRoom(roomId: Int): Array<UAR> {
    var result = arrayOf<UAR>()
    val connection = DriverManager.getConnection(url, user, password)
    val sql = """
    Select * from UserAssignRoom where roomId = '$roomId';"""
    val stmt = connection.createStatement()
    val rs = stmt.executeQuery(sql)
    while (rs.next()) {
        val uarId = rs.getInt("uarId")
        val userId = rs.getInt("userId")
        val roomId = rs.getInt("roomId")
        result += UAR(uarId, userId, roomId)
    }
    connection.close()
    return result
}

