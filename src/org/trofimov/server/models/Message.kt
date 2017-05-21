package org.trofimov.server.models

import org.trofimov.server.helpers.Foo
import org.trofimov.server.managers.password
import org.trofimov.server.helpers.toJSON
import org.trofimov.server.managers.url
import org.trofimov.server.managers.user
import java.net.URLEncoder
import java.sql.DriverManager

/**
 * Created by ivan on 16.05.17.
 */

class Message constructor(var messageId: Int, var userId: Int, val roomId: Int, val text: String, var date: String) {
    fun toPrint(): String {
        return toJSON(
                Foo("userId", userId.toString(), false),
                Foo("roomId", roomId.toString(), false),
                Foo("text", URLEncoder.encode(text, "UTF-8"), true),
                Foo("date", URLEncoder.encode(date, "UTF-8"), true))
    }
}

fun insertMessage(msg: Message) {
    val connection = DriverManager.getConnection(url, user, password)
    val ar1 = msg.messageId
    val ar2 = msg.userId
    val ar25 = msg.roomId
    val ar3 = msg.text
    val ar4 = msg.date

    val sql = URLEncoder.encode("""
                    INSERT INTO Message (messageId, userId, roomId, text, date)
                    VALUES ($ar1, $ar2, $ar25, '$ar3', '$ar4');""",  "UTF-8")

    println(sql)

    val stmt = connection.createStatement()

    stmt.executeUpdate(sql)
    connection.close()
    stmt.close()
}

fun getMessages(): Array<Message> {
    var result = arrayOf<Message>()
    val connection = DriverManager.getConnection(url, user, password)
    val sql = """
            Select * from Message;"""
    val stmt = connection.createStatement()
    val rs = stmt.executeQuery(sql)

    while (rs.next()) {
        val messageId = rs.getInt("messageId")
        val userId = rs.getInt("userId")
        val roomId = rs.getInt("roomId")
        val text = rs.getString("text")
        val date = rs.getString("date")
        result += Message(messageId, userId, roomId, text, date)
    }
    connection.close()
    stmt.close()
    return result
}