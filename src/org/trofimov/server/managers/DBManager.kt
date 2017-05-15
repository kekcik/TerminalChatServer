package org.trofimov.server.managers

/**
 * Created by ivan on 11.05.17.
 */

val url = "jdbc:mysql://93.189.43.66:3306/kukumber"
val user = "root"
val password = "123321"

fun dbManager() {
    val driverClass = "com.mysql.jdbc.Driver"
    Class.forName(driverClass)
}




