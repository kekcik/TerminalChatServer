/**
 * Created by ivan on 21.04.17.
 */
import spark.Spark.*

fun main(args: Array<String>) {
    port(12345)
    get("/hello") { req, res ->
        val a = 123
        println(req.params())
        "Hello World" + a
    }
    get("/") { req, res ->
        "Main page"
    }
}
