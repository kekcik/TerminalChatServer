package org.trofimov.server.managers

import spark.Spark
import java.net.URLEncoder
import java.text.SimpleDateFormat
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
        val time = SimpleDateFormat("HH:mm:ss").toString()
        print("GET on $time: login -> $login, $password <- ping: ")
        println(Instant.now().toEpochMilli() - ms)
        ans
    }

    Spark.get(PREFIX + "getUsers") { req, res ->
        val ip = req.ip()
        val ms = Instant.now().toEpochMilli()
        val ans = getUsersOld()
        val time = SimpleDateFormat("HH:mm:ss").toString()
        print("GET on $time: getUsers -> <- from $ip ping: ")
        println(Instant.now().toEpochMilli() - ms)
        ans
    }

    Spark.get(PREFIX + "getRooms") { req, res ->
        val ms = Instant.now().toEpochMilli()
        val ans = getRoomsM()
        val time = SimpleDateFormat("HH:mm:ss").toString()
        print("GET on $time: getRooms -> <- ping: ")
        println(Instant.now().toEpochMilli() - ms)
        ans
    }

    Spark.get(PREFIX + "register/:login/:password/:name") { req, res ->
        val login = req.params("login").split("=")[1]
        val password = req.params("password").split("=")[1]
        val name = req.params("name").split("=")[1]
        val ms = Instant.now().toEpochMilli()
        val ans = putUser(login, password, name)
        val time = SimpleDateFormat("HH:mm:ss").toString()
        print("GET on $time: register -> $login, $password, $name <- ping: ")
        println(Instant.now().toEpochMilli() - ms)
        ans
    }

    Spark.get(PREFIX + "createRoom/:token/:name/:pw") { req, res ->
        val token = req.params("token").split("=")[1]
        val name = req.params("name").split("=")[1]
        val pw = req.params("pw").split("=")[1]
        val ms = Instant.now().toEpochMilli()
        val ans = createRoom(token, name, pw)
        val time = SimpleDateFormat("HH:mm:ss").toString()
        print("GET on $time: createRoom -> $token, $name, $pw <- ping: ")
        println(Instant.now().toEpochMilli() - ms)
        ans
    }

    Spark.get(PREFIX + "sendMessage/:token/:roomName/:text") { req, res ->
        val token = req.params("token").split("=")[1]
        val roomName = req.params("roomName").split("=")[1]
        val text = req.params("text").split("=")[1]
        val ms = Instant.now().toEpochMilli()
        val ans = sendMessage(token, roomName, text)
        val time = SimpleDateFormat("HH:mm:ss").toString()
        print("GET on $time: sendMessage -> $token, $roomName, $text <- ping: ")
        println(Instant.now().toEpochMilli() - ms)
        ans
    }

    Spark.get(PREFIX + "getMessage/:token/:roomName") { req, res ->
        val token = req.params("token").split("=")[1]
        val roomName = req.params("roomName").split("=")[1]
        val ms = Instant.now().toEpochMilli()
        val ans = getMessage(token, roomName)
        val time = SimpleDateFormat("HH:mm:ss").toString()
        print("GET on $time: getMessage -> $token, $roomName <- ping: ")
        println(Instant.now().toEpochMilli() - ms)
        ans
    }

    Spark.get(PREFIX + "connectToRoom/:token/:roomName/:pw") { req, res ->
        val token = req.params("token").split("=")[1]
        val roomName = req.params("roomName").split("=")[1]
        val ms = Instant.now().toEpochMilli()
        val pw = req.params("pw").split("=")[1]
        val ans = connectToRoom(token, roomName, pw)
        val time = SimpleDateFormat("HH:mm:ss").toString()
        print("GET on $time: connectToRoom -> $token, $roomName, $pw <- ping: ")
        println(Instant.now().toEpochMilli() - ms)
        ans
    }

    Spark.get(PREFIX + "getTopMessage/:token/:roomName/:amount") { req, res ->
        val token = req.params("token").split("=")[1]
        val roomName = req.params("roomName").split("=")[1]
        val amount = req.params("amount").split("=")[1].toInt()
        val ms = Instant.now().toEpochMilli()
        val ans = getTopMessage(token, roomName, amount)
        val time = SimpleDateFormat("HH:mm:ss").toString()
        print("GET on $time: getTopMessage -> $token, $roomName, $amount <- ping: ")
        println(Instant.now().toEpochMilli() - ms)
        ans
    }

    Spark.get(PREFIX + "roomsForUser/:token") { req, res ->
        val token = req.params("token").split("=")[1]
        val ms = Instant.now().toEpochMilli()
        val ans = roomsForUser(token)
        val time = SimpleDateFormat("HH:mm:ss").toString()
        print("GET on $time: roomsForUser -> $token <- ping: ")
        println(Instant.now().toEpochMilli() - ms)
        ans
    }
}

