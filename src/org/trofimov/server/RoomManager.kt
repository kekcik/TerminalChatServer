package org.trofimov.server

import java.util.*

/**
 * Created by ivan on 09.05.17.
 */

class Message constructor(text: String){
    val text = text
    val date = Date().time
}

class Room constructor(name: String, pw: String){
    val name = name
    val pw = pw
    var messages = mutableListOf<Message>()
    var members = mutableListOf<String>()
    var admins = mutableListOf<String>()
}

private var rooms = mutableListOf<Room>()

fun createRoom(token: String, name: String, pw: String): Int {
    val login = getLoginBy(token)
    if (login == "") {
        return Errors.WRONG_TOKEN.code
    }
    rooms
            .filter { it.name == name }
            .forEach { return Errors.ROOM_NAME_ALREADY_USED.code }
    //check name

    val room = Room(name, pw)
    room.admins.add(login)
    room.members.add(login)
    room.messages.add(Message("$login создал чат"))
    rooms.add(room)
    return Errors.OK.code
}

fun sendMessage(token: String, roomName: String, text: String): Int {
    val login = getLoginBy(token)
    if (login == "") {
        return Errors.WRONG_TOKEN.code
    }
    for (room: Room in rooms) {
        if (room.name == roomName) {
            for (member: String in room.members) {
                if (member == login) {
                    val message = Message(text)
                    room.messages.add(message)
                    return Errors.OK.code
                }
            }
            return Errors.WRONG_LOGIN.code
        }
    }
    return Errors.WRONG_ROOM_NAME.code
}

fun getMessage(token: String, roomName: String): Pair<Int, List<Message>> {
    val login = getLoginBy(token)
    if (login == "") {
        return Pair(Errors.WRONG_TOKEN.code, mutableListOf<Message>())
    }
    for (room: Room in rooms) {
        if (room.name == roomName) {
            for (member: String in room.members) {
                if (member == login) {
                    return Pair(Errors.OK.code, room.messages)
                }
            }
            return Pair(Errors.WRONG_LOGIN.code, mutableListOf<Message>())
        }
    }
    return Pair(Errors.WRONG_ROOM_NAME.code, mutableListOf<Message>())
}

fun connectToRoom(token: String, roomName: String, pw: String): Int {
    val login = getLoginBy(token)
    if (login == "") {
        return Errors.WRONG_TOKEN.code
    }
    for (room: Room in rooms) {
        if (room.name == roomName) {
            for (member: String in room.members) {
                if (member == login) {
                    return Errors.ALREADY_CONNECT.code
                }
            }
            if (room.pw == pw) {
                room.members.add(login)
                return Errors.OK.code
            } else {
                return Errors.WRONG_PASSWORD.code
            }
        }
    }
    return Errors.WRONG_ROOM_NAME.code
}

fun getTopMessage(token: String, roomName: String, amount: Int): Pair<Int, List<Message>> {
    val login = getLoginBy(token)
    if (login == "") {
        return Pair(Errors.WRONG_TOKEN.code, mutableListOf<Message>())
    }
    for (room: Room in rooms) {
        if (room.name == roomName) {
            for (member: String in room.members) {
                if (member == login) {
                    return Pair(Errors.OK.code, room.messages.subList(maxOf(room.messages.count() - amount, 0), room.messages.count()))
                }
            }
            return Pair(Errors.WRONG_LOGIN.code, mutableListOf<Message>())
        }
    }
    return Pair(Errors.WRONG_ROOM_NAME.code, mutableListOf<Message>())
}