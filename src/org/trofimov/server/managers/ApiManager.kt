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
        val ans = checkUser(login, password)
        print("GET: login -> $login, $password <- ping: ")
        println(Instant.now().toEpochMilli() - ms)
        ans
    }

    Spark.get(PREFIX + "getUsers") { req, res ->
        val ip = req.ip()
        val ms = Instant.now().toEpochMilli()
        val ans = getUsersOld()
        print("GET: getUsers -> <- from $ip ping: ")
        println(Instant.now().toEpochMilli() - ms)
        ans
    }

    Spark.get(PREFIX + "getRooms") { req, res ->
        val ms = Instant.now().toEpochMilli()
        val ans = getRoomsM()
        print("GET: getRooms -> <- ping: ")
        println(Instant.now().toEpochMilli() - ms)
        ans
    }

    Spark.get(PREFIX + "register/:login/:password/:name") { req, res ->
        val login = req.params("login").split("=")[1]
        val password = req.params("password").split("=")[1]
        val name = req.params("name").split("=")[1]
        val ms = Instant.now().toEpochMilli()
        val ans = putUser(login, password, name)
        print("GET: register -> $login, $password, $name <- ping: ")
        println(Instant.now().toEpochMilli() - ms)
        ans
    }

    Spark.get(PREFIX + "createRoom/:token/:name/:pw") { req, res ->
        val token = req.params("token").split("=")[1]
        val name = req.params("name").split("=")[1]
        val pw = req.params("pw").split("=")[1]
        val ms = Instant.now().toEpochMilli()
        val ans = createRoom(token, name, pw)
        print("GET: createRoom -> $token, $name, $pw <- ping: ")
        println(Instant.now().toEpochMilli() - ms)
        ans
    }

    Spark.get(PREFIX + "sendMessage/:token/:roomName/:text") { req, res ->
        val token = req.params("token").split("=")[1]
        val roomName = req.params("roomName").split("=")[1]
        val text = req.params("text").split("=")[1]
        val ms = Instant.now().toEpochMilli()
        val ans = sendMessage(token, roomName, text)
        print("GET: sendMessage -> $token, $roomName, $text <- ping: ")
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
        val ans = connectToRoom(token, roomName, pw)
        print("GET: connectToRoom -> $token, $roomName, $pw <- ping: ")
        println(Instant.now().toEpochMilli() - ms)
        ans
    }

    Spark.get(PREFIX + "getTopMessage/:token/:roomName/:amount") { req, res ->
        val token = req.params("token").split("=")[1]
        val roomName = req.params("roomName").split("=")[1]
        val amount = req.params("amount").split("=")[1].toInt()
        val ms = Instant.now().toEpochMilli()
        val ans = getTopMessage(token, roomName, amount)
        print("GET: getTopMessage -> $token, $roomName, $amount <- ping: ")
        println(Instant.now().toEpochMilli() - ms)
        ans
    }

    Spark.get(PREFIX + "roomsForUser/:token") { req, res ->
        val token = req.params("token").split("=")[1]
        val ms = Instant.now().toEpochMilli()
        val ans = roomsForUser(token)
        print("GET: roomsForUser -> $token <- ping: ")
        println(Instant.now().toEpochMilli() - ms)
        ans
    }
}

