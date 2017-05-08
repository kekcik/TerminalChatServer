package com.yst.demo.yapi.documentation

import java.util.*

class MethodDoc(val name: String, val description: String) {

    val input: ArrayList<Value>
    val output: ArrayList<Value>
    val resultCodes: ArrayList<Pair<Result, String>>

    init {
        this.input = ArrayList<Value>()
        this.output = ArrayList<Value>()
        this.output.add(Value("result_code", Value.Type.longType, "Result code"))
        this.resultCodes = ArrayList<Pair<Result, String>>()
    }

    fun addInput(value: Value): MethodDoc {
        input.add(value)
        return this
    }

    fun addOutput(value: Value): MethodDoc {
        output.add(value)
        return this
    }

    fun addInput(name: String, type: Value.Type, description: String): MethodDoc {
        return addInput(Value(name, type, description))
    }

    fun addOutput(name: String, type: Value.Type, description: String): MethodDoc {
        return addOutput(Value(name, type, description))
    }

    fun addResultCode(result: Result, caseDescription: String): MethodDoc {
        resultCodes.add(Pair(result, caseDescription))
        resultCodes.sortBy{o -> o.component1().code}
        return this
    }


}
