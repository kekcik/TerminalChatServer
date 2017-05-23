package org.trofimov.server.managers

import spark.Spark
import java.net.URLEncoder
import java.time.Instant

/**
 * Created by ivan on 22.04.17.
 */

fun apiManager() {
    initServer()
    initMethods()
}

fun initServer() {
    Spark.port(8080)
}

fun encode(str: String): String {
    return URLEncoder.encode(str, "UTF-8")
}
fun initMethods() {
    val PREFIX = "/api/"
    Spark.get(PREFIX + "login/:login/:password") { req, res ->
        val login = req.params("login").split("=")[1]
        val password = req.params("password").split("=")[1]
        val ms = Instant.now().toEpochMilli()
        print("GET: login -> $login, $password <- ping: ")
        val ans = checkUser(login, password)
        println(Instant.now().toEpochMilli() - ms)
        ans
    }

    Spark.get(PREFIX + "getUsers") { req, res ->
        val ip = req.ip()
        val ms = Instant.now().toEpochMilli()
        print("GET: getUsers -> <- from $ip ping: ")
        val ans = getUsersOld()
        println(Instant.now().toEpochMilli() - ms)
        ans
    }

    Spark.get(PREFIX + "getRooms") { req, res ->
        print("GET: getRooms -> <- ping: ")
        val ms = Instant.now().toEpochMilli()
        val ans = getRoomsM()
        println(Instant.now().toEpochMilli() - ms)
        ans
    }

    Spark.get(PREFIX + "register/:login/:password/:name") { req, res ->
        val login = req.params("login").split("=")[1]
        val password = req.params("password").split("=")[1]
        val name = req.params("name").split("=")[1]
        val ms = Instant.now().toEpochMilli()
        print("GET: register -> $login, $password, $name <- ping: ")
        val ans = putUser(login, password, name)
        println(Instant.now().toEpochMilli() - ms)
        ans
    }

    Spark.get(PREFIX + "createRoom/:token/:name/:pw") { req, res ->
        val token = req.params("token").split("=")[1]
        val name = req.params("name").split("=")[1]
        val pw = req.params("pw").split("=")[1]
        val ms = Instant.now().toEpochMilli()
        print("GET: createRoom -> $token, $name, $pw <- ping: ")
        val ans = createRoom(token, name, pw)
        println(Instant.now().toEpochMilli() - ms)
        ans
    }

    Spark.get(PREFIX + "sendMessage/:token/:roomName/:text") { req, res ->
        val token = req.params("token").split("=")[1]
        val roomName = req.params("roomName").split("=")[1]
        val text = req.params("text").split("=")[1]
        val ms = Instant.now().toEpochMilli()
        print("GET: sendMessage -> $token, $roomName, $text <- ping: ")
        val ans = sendMessage(token, roomName, text)
        println(Instant.now().toEpochMilli() - ms)
        ans
    }

    Spark.get(PREFIX + "getMessage/:token/:roomName") { req, res ->
        val token = req.params("token").split("=")[1]
        val roomName = req.params("roomName").split("=")[1]
        val ms = Instant.now().toEpochMilli()
        print("GET: getMessage -> $token, $roomName <- ping: ")
        val ans = getMessage(token, roomName)
        println(Instant.now().toEpochMilli() - ms)
        ans
    }

    Spark.get(PREFIX + "connectToRoom/:token/:roomName/:pw") { req, res ->
        val token = req.params("token").split("=")[1]
        val roomName = req.params("roomName").split("=")[1]
        val ms = Instant.now().toEpochMilli()
        val pw = req.params("pw").split("=")[1]
        print("GET: connectToRoom -> $token, $roomName, $pw <- ping: ")
        val ans = connectToRoom(token, roomName, pw)
        println(Instant.now().toEpochMilli() - ms)
        ans
    }

    Spark.get(PREFIX + "getTopMessage/:token/:roomName/:amount") { req, res ->
        val token = req.params("token").split("=")[1]
        val roomName = req.params("roomName").split("=")[1]
        val amount = req.params("amount").split("=")[1].toInt()
        val ms = Instant.now().toEpochMilli()
        print("GET: getTopMessage -> $token, $roomName, $amount <- ping: ")
        val ans = getTopMessage(token, roomName, amount)
        println(Instant.now().toEpochMilli() - ms)
        ans
    }

    Spark.get(PREFIX + "roomsForUser/:token") { req, res ->
        val token = req.params("token").split("=")[1]
        val ms = Instant.now().toEpochMilli()
        print("GET: roomsForUser -> $token <- ping: ")
        val ans = roomsForUser(token)
        println(Instant.now().toEpochMilli() - ms)
        ans
    }
}

