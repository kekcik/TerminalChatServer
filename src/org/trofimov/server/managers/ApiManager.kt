package org.trofimov.server.managers

import spark.Spark
import java.net.URLEncoder

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
        println("GET: login -> $login, $password")
        checkUser(login, password)
    }

    Spark.get(PREFIX + "getUsers") { req, res ->
        println("GET: getUsers")
        getUsersOld()
    }

    Spark.get(PREFIX + "getRooms") { req, res ->
        println("GET: getRooms")
        getRoomsM()
    }

    Spark.get(PREFIX + "register/:login/:password/:name") { req, res ->
        val login = req.params("login").split("=")[1]
        val password = req.params("password").split("=")[1]
        val name = req.params("name").split("=")[1]
        println("GET: register -> $login, $password, $name")
        putUser(login, password, name)
    }

    Spark.get(PREFIX + "createRoom/:token/:name/:pw") { req, res ->
        val token = req.params("token").split("=")[1]
        val name = req.params("name").split("=")[1]
        val pw = req.params("pw").split("=")[1]
        println("GET: createRoom -> $token, $name, $pw")
        createRoom(token, name, pw)
    }

    Spark.get(PREFIX + "sendMessage/:token/:roomName/:text") { req, res ->
        val token = req.params("token").split("=")[1]
        val roomName = req.params("roomName").split("=")[1]
        val text = req.params("text").split("=")[1]
        println("GET: sendMessage -> $token, $roomName, $text")
        sendMessage(token, roomName, text)
    }

    Spark.get(PREFIX + "getMessage/:token/:roomName") { req, res ->
        val token = req.params("token").split("=")[1]
        val roomName = req.params("roomName").split("=")[1]
        println("GET: getMessage -> $token, $roomName")
        getMessage(token, roomName)
    }

    Spark.get(PREFIX + "connectToRoom/:token/:roomName/:pw") { req, res ->
        val token = req.params("token").split("=")[1]
        val roomName = req.params("roomName").split("=")[1]
        val pw = req.params("pw").split("=")[1]
        println("GET: connectToRoom -> $token, $roomName, $pw")
        connectToRoom(token, roomName, pw)
    }

    Spark.get(PREFIX + "getTopMessage/:token/:roomName/:amount") { req, res ->
        val token = req.params("token").split("=")[1]
        val roomName = req.params("roomName").split("=")[1]
        val amount = req.params("amount").split("=")[1].toInt()
        println("GET: getTopMessage -> $token, $roomName, $amount")
        getTopMessage(token, roomName, amount)
    }

    Spark.get(PREFIX + "roomsForUser/:token") { req, res ->
        val token = req.params("token").split("=")[1]
        println("GET: roomsForUser -> $token")
        roomsForUser(token)
    }
}

