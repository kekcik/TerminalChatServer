package org.trofimov.server

/**
 * Created by ivan on 09.05.17.
 */


enum class Errors(val code: Int) {
    OK(0),
    WRONG_PASSWORD(10),
    //CHAT_NAME_ALREADY_USED(20), //deprecated
    WRONG_TOKEN(30),
    CHAT_NO_FOUND(40),
    LOGIN_ALREADY_USED(50),
    LOGIN_NOT_FOUND(60),
    WRONG_LOGIN(70),
    WRONG_ROOM_NAME(80),
    ROOM_NAME_ALREADY_USED(90),
    ALREADY_CONNECT(100)
}