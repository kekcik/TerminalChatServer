package org.trofimov.server

/**
 * Created by ivan on 21.04.17.
 */

import java.sql.DriverManager

fun main(args: Array<String>) {
    apiManager()
    dbManager()
}

val url = "jdbc:mysql://93.189.43.66:3306/kukumber"
val user = "root"
val password = "123321"

fun dbManager() {
    val driverClass = "com.mysql.jdbc.Driver"
    Class.forName(driverClass)
}





class UAR(var uarId: Int?, var userId: Int, var roomId: Int)

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


