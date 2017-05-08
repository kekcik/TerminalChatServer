package com.yst.demo

import com.yst.demo.yapi.Resolver
import com.yst.demo.yapi.documentation.MethodDoc
import com.yst.demo.yapi.documentation.Result
import com.yst.demo.yapi.documentation.Value
import java.util.HashMap

class ExampleResolver : Resolver() {

    override fun getURL() = "/example"

    override fun buildDoc(): MethodDoc {
        return MethodDoc(getURL(), "Returns example")
                .addInput("int_field", Value.Type.longType, "Some description")
                .addInput("string_field", Value.Type.stringType, "Some description x2")
                .addOutput("yo", Value.Type.stringType, "Yo")
                .addOutput("time", Value.Type.longType, "Current nanotime")
    }

    override fun process(parameters: HashMap<String, Any>): Any {
        // fields & types are ALREADY extracted from request using buildDoc()'s data
        // if fields are not represented or have wrong type - user already got message about this
        val intField = parameters["int_field"] as Long
        val stringField = parameters["string_field"] as String
        println("Data: $intField / $stringField")
        return Result.OK.buildResultCode().putMany(
                "yo" to "yo",
                "time" to System.nanoTime()
        )
    }


}