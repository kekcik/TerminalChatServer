package org.trofimov.server

import com.yst.demo.jsonObject
import kotlin.*
import spark.Spark
import java.net.URI
import java.net.URL

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
    val alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_"
    val PREFIX = "/api/"
    Spark.get(PREFIX + "login/:login/:password") { req, res ->
        val login = req.params("login").split("=")[1]
        val password = req.params("password").split("=")[1]
        checkUser(login, password)
    }

    Spark.get(PREFIX + "getUsers") { req, res ->
        getUsers()
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
        createRoom(token, roomName, text)
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
}

