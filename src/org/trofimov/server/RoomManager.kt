package org.trofimov.server

import java.util.*

/**
 * Created by ivan on 09.05.17.
 */

class Message constructor(text: String){
    val text = text
    val date = Date().time
    fun toPrint(): String {
        return toJSON(
                Pair("text", text),
                Pair("date", date.toString()))
    }
}

class Room constructor(name: String, pw: String){
    val name = name
    val pw = pw
    var messages = mutableListOf<Message>()
    var members = mutableListOf<String>()
    var admins = mutableListOf<String>()
}

private var rooms = mutableListOf<Room>()

fun createRoom(token: String, name: String, pw: String): String {
    val login = getLoginBy(token)
    if (login == "") {
        return toJSON(Pair("code", Errors.WRONG_TOKEN.code.toString()))
    }
    rooms
            .filter { it.name == name }
            .forEach { return toJSON(Pair("code", Errors.ROOM_NAME_ALREADY_USED.code.toString()))
            }
    //check name

    val room = Room(name, pw)
    room.admins.add(login)
    room.members.add(login)
    room.messages.add(Message("$login создал чат"))
    rooms.add(room)
    return toJSON(Pair("code", Errors.OK.code.toString()))
}

fun sendMessage(token: String, roomName: String, text: String): String {
    val login = getLoginBy(token)
    if (login == "") {
        return toJSON(Pair("code", Errors.WRONG_TOKEN.code.toString()))
    }
    for (room: Room in rooms) {
        if (room.name == roomName) {
            for (member: String in room.members) {
                if (member == login) {
                    val message = Message(text)
                    room.messages.add(message)
                    return toJSON(Pair("code", Errors.OK.code.toString()))
                }
            }
            return toJSON(Pair("code", Errors.WRONG_LOGIN.code.toString()))
        }
    }
    return toJSON(Pair("code", Errors.WRONG_ROOM_NAME.code.toString()))
}

fun getMessage(token: String, roomName: String): String {
    val login = getLoginBy(token)
    if (login == "") {
        return toJSON(
                Pair("code", Errors.WRONG_TOKEN.code.toString()),
                Pair("messages:", "[]"))    }
    for (room: Room in rooms) {
        if (room.name == roomName) {
            for (member: String in room.members) {
                if (member == login) {
                    return toJSON(
                            Pair("code", Errors.OK.code.toString()),
                            Pair("messages:", toJSONArray(room.messages.map { it.toPrint() })))
                }
            }
            return toJSON(
                    Pair("code", Errors.WRONG_LOGIN.code.toString()),
                    Pair("messages:", "[]"))        }
    }
    return toJSON(
            Pair("code", Errors.WRONG_ROOM_NAME.code.toString()),
            Pair("messages:", "[]"))}

fun connectToRoom(token: String, roomName: String, pw: String): String {
    val login = getLoginBy(token)
    if (login == "") {
        return toJSON(Pair("code", Errors.WRONG_TOKEN.code.toString()))
    }
    for (room: Room in rooms) {
        if (room.name == roomName) {
            for (member: String in room.members) {
                if (member == login) {
                    return toJSON(Pair("code", Errors.ALREADY_CONNECT.code.toString()))
                }
            }
            if (room.pw == pw) {
                room.members.add(login)
                return toJSON(Pair("code", Errors.OK.code.toString()))
            } else {
                return toJSON(Pair("code", Errors.WRONG_PASSWORD.code.toString()))
            }
        }
    }
    return toJSON(Pair("code", Errors.WRONG_ROOM_NAME.code.toString()))
}

fun getTopMessage(token: String, roomName: String, amount: Int): String {
    val login = getLoginBy(token)
    if (login == "") {
        return toJSON(
                Pair("code", Errors.WRONG_TOKEN.code.toString()),
                Pair("messages:", "[]"))
    }
    for (room: Room in rooms) {
        if (room.name == roomName) {
            for (member: String in room.members) {
                if (member == login) {
                    return toJSON(
                            Pair("code", Errors.OK.code.toString()),
                            Pair("messages:", toJSONArray(room.messages.subList(maxOf(room.messages.count() - amount, 0), room.messages.count()).map { it.toPrint() })))
                }
            }
            return toJSON(
                    Pair("code", Errors.WRONG_LOGIN.code.toString()),
                    Pair("messages:", "[]"))        }
    }
    return toJSON(
            Pair("code", Errors.WRONG_ROOM_NAME.code.toString()),
            Pair("messages:", "[]"))}