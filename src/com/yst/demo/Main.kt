package com.yst.demo

import com.yst.demo.yapi.Resolver
import com.yst.demo.yapi.documentation.DocumentationResolver
import com.yst.demo.yapi.documentation.Result
import spark.Spark
import spark.Spark.get
import spark.Spark.port
import java.util.*

fun main(args: Array<String>) {

    Spark.externalStaticFileLocation(System.getProperty("user.dir") + "/resources")

    port(12345)
    get("/hello") { req, _ ->
        val a = 123
        println(req.queryParams())
        "Hello World" + a
    }
    get("/") { _, _ ->
        "Main page"
    }


    val resolvers = ArrayList<Resolver>()
    resolvers.add(ExampleResolver())
    for (resolver in resolvers) {
        get(resolver.getURL(), resolver)
    }
    val doc = DocumentationResolver(
            resolvers,
            "result_codes" to Result.allReturnCodes.map { it.code to it.name }
    )
    get("/docs", doc)


}
