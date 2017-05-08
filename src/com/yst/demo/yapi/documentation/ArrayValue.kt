package com.yst.demo.yapi.documentation

import java.util.*

class ArrayValue(name: String, description: String) : Value(name, Type.array, description) {

    val values = ArrayList<Value>()

    fun addValue(value: Value): ArrayValue {
        values.add(value)
        return this
    }

    fun addValue(name: String, type: Type, description: String): ArrayValue {
        return addValue(Value(name, type, description))
    }

    fun getValues(): Iterable<Value> {
        return values
    }


}
