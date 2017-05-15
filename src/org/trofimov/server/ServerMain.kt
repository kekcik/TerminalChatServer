package org.trofimov.server

/**
 * Created by ivan on 21.04.17.
 */

import org.trofimov.server.managers.apiManager
import org.trofimov.server.managers.dbManager

fun main(args: Array<String>) {
    apiManager()
    dbManager()
}

