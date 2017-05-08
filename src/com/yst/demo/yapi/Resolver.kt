package com.yst.demo.yapi

import org.json.simple.JSONObject
import spark.Request
import spark.Response
import spark.Route
import com.yst.demo.yapi.documentation.MethodDoc
import com.yst.demo.yapi.documentation.Result.Companion.FIELD_HAS_WRONG_TYPE
import com.yst.demo.yapi.documentation.Result.Companion.FIELD_IS_NOT_REPRESENTED
import com.yst.demo.yapi.documentation.Result.Companion.OK
import com.yst.demo.yapi.documentation.Value
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.LongAdder

abstract class Resolver : Route {

    abstract fun getURL(): String

    abstract fun buildDoc(): MethodDoc

    private val documentation = buildDoc()
            .addResultCode(FIELD_IS_NOT_REPRESENTED, "When some field is not specified")
            .addResultCode(FIELD_HAS_WRONG_TYPE, "When some field has wrong type")
            .addResultCode(OK, "When everything is fine")

    fun getDoc(): MethodDoc {
        return documentation
    }

    private fun parseFloatArray(data: Any): ArrayList<Float> {
        if (data is String) {
            val items = data.split(",")
            val values = ArrayList<Float>()
            for (a in items) {
                val l = a.toFloat()
                values.add(l)
            }
            return values
        }
        throw NumberFormatException()
    }

    private fun parseLongArray(data: Any): ArrayList<Long> {
        if (data is String) {
            val items = data.split(",")
            val values = ArrayList<Long>()
            for (a in items) {
                val l = a.toLong()
                values.add(l)
            }
            return values
        }
        throw NumberFormatException()
    }

    private fun toLong(a: Any?): Long {
        if (a == null) throw NumberFormatException()
        if (a is Long) return a
        if (a is String) return a.toLong()
        throw NumberFormatException()
    }

    private fun toFloat(a: Any?): Float {
        if (a == null) throw NumberFormatException()
        if (a is Number) return a.toFloat()
        if (a is String) return a.toFloat()
        throw NumberFormatException()
    }

    private fun toBoolean(a: Any?): Boolean {
        if (a == null) throw NumberFormatException()
        if (a is Boolean) return a
        if (a is String) return a.toBoolean()
        throw NumberFormatException()
    }

    private fun toString(a: Any?): String {
        if (a == null) throw NumberFormatException()
        if (a is String) return a
        if (a is Number) return a.toString()
        if (a is Boolean) return a.toString()
        throw NumberFormatException()
    }

    fun checkParameters(params: (String) -> Any?, parameters: HashMap<String, Any>): JSONObject? {
        try {
            for (value in documentation.input) {
                val found = params(value.name)
                if (found == null) {
                    return FIELD_IS_NOT_REPRESENTED.buildResultCode()
                } else {
                    when (value.type) {
                        Value.Type.longType -> {
                            parameters.put(value.name, toLong(found))
                        }
                        Value.Type.floatType -> {
                            parameters.put(value.name, toFloat(found))
                        }
                        Value.Type.stringType -> {
                            parameters.put(value.name, toString(found))
                        }
                        Value.Type.booleanType -> {
                            parameters.put(value.name, toBoolean(found))
                        }
                        Value.Type.longArray -> {
                            parameters.put(value.name, parseLongArray(found))
                        }
                        Value.Type.floatArray -> {
                            parameters.put(value.name, parseFloatArray(found))
                        }
                        else -> {
                            Exception("Unknown input type: " + value.type).printStackTrace()
                        }
                    }
                }
            }
            return null
        } catch (e: NumberFormatException) {
            return FIELD_HAS_WRONG_TYPE.buildResultCode()
        }
    }


    fun handle(params: (String) -> Any?): Any {
        val time = System.nanoTime()
        val map = HashMap<String, Any>()
        val error = checkParameters(params, map)
        if (error != null) return error
        val res: Any
        try {
            res = process(map)
        } catch (e: Throwable) {
            e.printStackTrace()
            throw e
        }
        totalTime.add(System.nanoTime() - time)
        totalUsages.increment()
        commonAdder.increment()
        return res
    }

    fun handle(params: JSONObject): Any {
        return handle {
            params[it]
        }
    }

    override fun handle(req: Request, res: Response): Any {
        return handle {
            req.queryParams(it)
        }
    }

    abstract fun process(parameters: HashMap<String, Any>): Any

    init {
        resolvers.add(this)
    }

    val totalTime = LongAdder()
    val totalUsages = LongAdder()

    companion object {

        val resolvers = CopyOnWriteArrayList<Resolver>()

        val commonAdder = LongAdder()
        var lastTimestamp = 0L
        val rps = CopyOnWriteArrayList<Pair<Long, Double>>()

        init {
            Thread(
                    {
                        while (true) {
                            Thread.sleep(5000)
                            val current = System.currentTimeMillis()
                            val dTime = current - lastTimestamp
                            val requests = commonAdder.sum()
                            commonAdder.add(-requests)
                            lastTimestamp = current
                            rps.add(current to requests * 1000.0 / dTime)
                            if (rps.size > 30) rps.removeAt(0)
                        }
                    },
                    "rps counter"
            ).start()
        }
    }

}
