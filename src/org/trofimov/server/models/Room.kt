package org.trofimov.server.models

import org.trofimov.server.helpers.Foo
import org.trofimov.server.helpers.toJSON
import org.trofimov.server.managers.password
import org.trofimov.server.managers.url
import org.trofimov.server.managers.user
import java.net.URLEncoder
import java.sql.DriverManager

/**
 * Created by ivan on 15.05.17.
 */


class Room constructor(val roomId: Int, var adminId: Int, val name: String, val pw: String) {
    fun toPrint(): String {
        return toJSON(
                Foo("roomId", roomId.toString(), true),
                Foo("adminId", adminId.toString(), true),
                Foo("name", URLEncoder.encode(name, "UTF-8"), true),
                Foo("pw", URLEncoder.encode(pw, "UTF-8"), true)
        )
    }
}

fun getRooms(): Array<Room> {
    var result = arrayOf<Room>()
    val connection = DriverManager.getConnection(url, user, password)
    val sql = """
            Select * from Room;"""
    val stmt = connection.createStatement()
    val rs = stmt.executeQuery(sql)
    while (rs.next()) {
        val roomId = rs.getInt("roomId")
        val adminId = rs.getInt("adminId")
        val name = rs.getString("name")
        val password = rs.getString("password")
        result += Room(roomId, adminId, name, password)
    }
    connection.close()
    stmt.close()
    return result
}

fun insertRoom(room: Room) {
    val connection = DriverManager.getConnection(url, user, password)
    val ar1 = room.roomId
    val ar2 = room.adminId
    val ar3 = room.name
    val ar4 = room.pw

    val sql = """
                    INSERT INTO Room (roomId, adminId, name, password)
                    VALUES ($ar1, $ar2, '$ar3', '$ar4');"""

    println(sql)

    val stmt = connection.createStatement()

    stmt.executeUpdate(sql)
    connection.close()
    stmt.close()
}