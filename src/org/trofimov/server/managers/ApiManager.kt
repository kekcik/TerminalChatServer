package org.trofimov.server.managers

import spark.Spark

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


fun initMethods() {
    val PREFIX = "/api/"
    Spark.get(PREFIX + "login/:login/:password") { req, res ->
        val login = req.params("login").split("=")[1]
        val password = req.params("password").split("=")[1]
        checkUser(login, password)
    }

    Spark.get(PREFIX + "getUsers") { req, res ->
        getUsersOld()
    }

    Spark.get(PREFIX + "getRooms") { req, res ->
        getRoomsM()
    }

    Spark.get(PREFIX + "register/:login/:password/:name") { req, res ->
        val login = req.params("login").split("=")[1]
        val password = req.params("password").split("=")[1]
        val name = req.params("name").split("=")[1]
        putUser(login, password, name)
    }

    Spark.get(PREFIX + "createRoom/:token/:name/:pw") { req, res ->
        val token = req.params("token").split("=")[1]
        val name = req.params("name").split("=")[1]
        val pw = req.params("pw").split("=")[1]
        createRoom(token, name, pw)
    }

    Spark.get(PREFIX + "sendMessage/:token/:roomName/:text") { req, res ->
        val token = req.params("token").split("=")[1]
        val roomName = req.params("roomName").split("=")[1]
        val text = req.params("text").split("=")[1]
        sendMessage(token, roomName, text)
    }

    Spark.get(PREFIX + "getMessage/:token/:roomName") { req, res ->
        val token = req.params("token").split("=")[1]
        val roomName = req.params("roomName").split("=")[1]
        getMessage(token, roomName)
    }

    Spark.get(PREFIX + "connectToRoom/:token/:roomName/:pw") { req, res ->
        val token = req.params("token").split("=")[1]
        val roomName = req.params("roomName").split("=")[1]
        val pw = req.params("pw").split("=")[1]
        connectToRoom(token, roomName, pw)
    }

    Spark.get(PREFIX + "getTopMessage/:token/:roomName/:amount") { req, res ->
        val token = req.params("token").split("=")[1]
        val roomName = req.params("roomName").split("=")[1]
        val amount = req.params("amount").split("=")[1].toInt()
        getTopMessage(token, roomName, amount)
    }

    Spark.get(PREFIX + "roomsForUser/:token") { req, res ->
        val token = req.params("token").split("=")[1]
        roomsForUser(token)
    }
}

