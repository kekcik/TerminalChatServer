package org.trofimov.server

import java.util.*

/**
 * Created by ivan on 09.05.17.
 */

class Message constructor(val text: String){
    val date = Date().time
    fun toPrint(): String {
        return toJSON(
                Foo("text", text, true),
                Foo("date", date.toString(), true))
    }
}

class Room constructor(val name: String, val pw: String){
    var messages = mutableListOf<Message>()
    var members = mutableListOf<String>()
    var admins = mutableListOf<String>()
}

private var rooms = mutableListOf<Room>()

fun createRoom(token: String, name: String, pw: String): String {
    val login = getLoginBy(token)
    if (login == "") {
        return toJSON(Foo("code", Errors.WRONG_TOKEN.code.toString(), false))
    }
    rooms
            .filter { it.name == name }
            .forEach { return toJSON(Foo("code", Errors.ROOM_NAME_ALREADY_USED.code.toString(), false))
            }
    //check name

    val room = Room(name, pw)
    room.admins.add(login)
    room.members.add(login)
    room.messages.add(Message("$login создал чат"))
    rooms.add(room)
    return toJSON(Foo("code", Errors.OK.code.toString(), false))
}

fun sendMessage(token: String, roomName: String, text: String): String {
    val login = getLoginBy(token)
    if (login == "") {
        return toJSON(Foo("code", Errors.WRONG_TOKEN.code.toString(), false))
    }
    for (room: Room in rooms) {
        if (room.name == roomName) {
            for (member: String in room.members) {
                if (member == login) {
                    val message = Message(text)
                    room.messages.add(message)
                    return toJSON(Foo("code", Errors.OK.code.toString(), false))
                }
            }
            return toJSON(Foo("code", Errors.WRONG_LOGIN.code.toString(), false))
        }
    }
    return toJSON(Foo("code", Errors.WRONG_ROOM_NAME.code.toString(), false))
}

fun getMessage(token: String, roomName: String): String {
    val login = getLoginBy(token)
    if (login == "") {
        return toJSON(
                Foo("code", Errors.WRONG_TOKEN.code.toString(), false),
                Foo("messages:", "[]", true))
    }
    return rooms
            .firstOrNull { it.name == roomName }
            ?.let { room ->
                if (room.members.contains(login)) toJSON(
                        Foo("code", Errors.OK.code.toString(), false),
                        Foo("messages:", toJSONArray(room.messages.map { it.toPrint() }), true)) else toJSON(
                        Foo("code", Errors.WRONG_LOGIN.code.toString(), false),
                        Foo("messages:", "[]", true))
            }
            ?: toJSON(
            Foo("code", Errors.WRONG_ROOM_NAME.code.toString(), false),
            Foo("messages:", "[]", true))
}

fun connectToRoom(token: String, roomName: String, pw: String): String {
    val login = getLoginBy(token)
    if (login == "") {
        return toJSON(Foo("code", Errors.WRONG_TOKEN.code.toString(), false))
    }
    for (room: Room in rooms) {
        if (room.name == roomName) {
            room.members
                    .filter { it == login }
                    .forEach { return toJSON(Foo("code", Errors.ALREADY_CONNECT.code.toString(), false)) }
            if (room.pw == pw) {
                room.members.add(login)
                return toJSON(Foo("code", Errors.OK.code.toString(), false))
            } else {
                return toJSON(Foo("code", Errors.WRONG_PASSWORD.code.toString(), false))
            }
        }
    }
    return toJSON(Foo("code", Errors.WRONG_ROOM_NAME.code.toString(), false))
}

fun getTopMessage(token: String, roomName: String, amount: Int): String {
    val login = getLoginBy(token)
    if (login == "") {
        return toJSON(
                Foo("code", Errors.WRONG_TOKEN.code.toString(), false),
                Foo("messages:", "[]", true))
    }
    for (room: Room in rooms) {
        if (room.name == roomName) {
            return if (room.members.contains(login)) toJSON(
                    Foo("code", Errors.OK.code.toString(), false),
                    Foo("messages:", toJSONArray(room.messages.subList(maxOf(room.messages.count() - amount, 0),
                            room.messages.count()).map { it.toPrint() }), true)) else toJSON(
                    Foo("code", Errors.WRONG_LOGIN.code.toString(), false),
                    Foo("messages:", "[]", true))
        }
    }
    return toJSON(
            Foo("code", Errors.WRONG_ROOM_NAME.code.toString(), false),
            Foo("messages:", "[]", true))}