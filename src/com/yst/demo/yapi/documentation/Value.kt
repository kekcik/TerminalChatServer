package com.yst.demo.yapi.documentation

open class Value(val name: String, val type: Type, val description: String) {

    enum class Type {
        stringType, longType, longArray, floatArray, array, floatType, struct, rawBytes, booleanType
    }


}
