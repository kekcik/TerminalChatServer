package org.trofimov.server.managers

import org.trofimov.server.helpers.Errors
import org.trofimov.server.helpers.Foo
import org.trofimov.server.models.*
import org.trofimov.server.helpers.toJSON
import org.trofimov.server.helpers.toJSONArray
import java.util.*
import java.util.Random


/**
 * Created by ivan on 09.05.17.
 */

private var rooms = arrayOf<Room>()



fun createRoom(token: String, name: String, pw: String): String {
    val user = getLoginBy(token) ?: return toJSON(Foo("code", Errors.WRONG_TOKEN.code.toString(), false))
    rooms = getRooms()
    rooms
            .filter { it.name == name }
            .forEach {
                return toJSON(Foo("code", Errors.ROOM_NAME_ALREADY_USED.code.toString(), false))
            }
    //check name
    val login = user.login
    val room = Room(Random().nextInt(1_000_000_000), user.userId!!, name, pw)
    val uar = UAR(null, user.userId!!, room.roomId)
    val msg = Message(0, user.userId!!, room.roomId, "$login create chat $name", Date().toString())
    insertUAR(uar)
    insertRoom(room)
    insertMessage(msg)
    return toJSON(Foo("code", Errors.OK.code.toString(), false))
}

fun getRoomsM(): String {
    return toJSON(
            Foo("code", Errors.OK.code.toString(), false),
            Foo("rooms", toJSONArray(getRooms().map(Room::toPrint)), false)
    )
}

fun roomsForUser(token: String): String {
    val user = getLoginBy(token) ?: return toJSON(Foo("code", Errors.WRONG_TOKEN.code.toString(), false))
    val UARs = getUARsForUser(user.userId!!)
    return toJSON(
            Foo("code", Errors.OK.code.toString(), false),
            Foo("uars", toJSONArray(UARs.map(UAR::toPrint)), false)
            )
}

fun sendMessage(token: String, roomName: String, text: String): String {
    val user = getLoginBy(token) ?: return toJSON(Foo("code", Errors.WRONG_TOKEN.code.toString(), false))
    rooms = getRooms()
    for (room: Room in rooms) {
        if (room.name == roomName) {
            val localUARs = getUARsForRoom(room.roomId)
            for (memberId in localUARs) {
                if (memberId.userId == user.userId!!) {
                    val message = Message(0, user.userId!!, room.roomId, text, Date().toString())
                    insertMessage(message)
                    //room.messages.add(message)
                    return toJSON(Foo("code", Errors.OK.code.toString(), false))
                }
            }
            return toJSON(Foo("code", Errors.WRONG_LOGIN.code.toString(), false))
        }
    }
    return toJSON(Foo("code", Errors.WRONG_ROOM_NAME.code.toString(), false))
}


fun connectToRoom(token: String, roomName: String, pw: String): String {
    val user = getLoginBy(token) ?: return toJSON(Foo("code", Errors.WRONG_TOKEN.code.toString(), false))
    rooms = getRooms()
    for (room: Room in rooms) {
        if (room.name == roomName) {
            getUARsForRoom(room.roomId)
                    .filter { it.userId == user.userId!! }
                    .forEach { return toJSON(Foo("code", Errors.ALREADY_CONNECT.code.toString(), false)) }
            if (room.pw == pw) {
                val uar = UAR(Random().nextInt(1_000_000_000), user.userId!!, room.roomId)
                insertUAR(uar)
                return toJSON(Foo("code", Errors.OK.code.toString(), false))
            } else {
                return toJSON(Foo("code", Errors.WRONG_PASSWORD.code.toString(), false))
            }
        }
    }
    return toJSON(Foo("code", Errors.WRONG_ROOM_NAME.code.toString(), false))
}

fun getMessage(token: String, roomName: String): String {
    val user = getLoginBy(token) ?: return toJSON(
            Foo("code", Errors.WRONG_TOKEN.code.toString(), false),
            Foo("messages:", "[]", true))
    rooms = getRooms()
    return rooms
            .firstOrNull { it.name == roomName }
            ?.let { room ->
                if (getUARsForRoom(room.roomId).map(UAR::userId).contains(user.userId)) toJSON(
                        Foo("code", Errors.OK.code.toString(), false),
                        Foo("messages:", toJSONArray(getMessages().filter { it.roomId == room.roomId }.map { it.toPrint() }), false))
                else toJSON(
                        Foo("code", Errors.WRONG_LOGIN.code.toString(), false),
                        Foo("messages:", "[]", true))
            }
            ?: toJSON(
            Foo("code", Errors.WRONG_ROOM_NAME.code.toString(), false),
            Foo("messages:", "[]", true))
}

fun getTopMessage(token: String, roomName: String, amount: Int): String {
    val user = getLoginBy(token) ?: return toJSON(
            Foo("code", Errors.WRONG_TOKEN.code.toString(), false),
            Foo("messages:", "[]", true))
    rooms = getRooms()

    return rooms
            .firstOrNull { it.name == roomName }
            ?.let { room ->
                val memberIds = getUARsForRoom(room.roomId).map(UAR::userId)
                val messages = getMessages().toList().filter { it.roomId == room.roomId }
                val msgInStr = messages.subList(maxOf(messages.size - amount, 0), messages.size)
                        .map { it.toPrint() }
                if (memberIds.contains(user.userId)) toJSON(
                        Foo("code", Errors.OK.code.toString(), false),
                        Foo("messages:", toJSONArray(msgInStr), false))
                else toJSON(
                        Foo("code", Errors.WRONG_LOGIN.code.toString(), false),
                        Foo("messages:", "[]", true))
            }
            ?: toJSON(
            Foo("code", Errors.WRONG_ROOM_NAME.code.toString(), false),
            Foo("messages:", "[]", true))
}

