package com.yst.demo.yapi.documentation

import com.yst.demo.jsonObject
import org.json.simple.JSONObject
import java.util.*


class Result(val code: Int, val name: String) {

    init {
        allReturnCodes.add(this)
    }


    fun buildResultCode(): JSONObject = jsonObject("result_code" to code)

    companion object {
        val allReturnCodes = ArrayList<Result>()
        val OK = Result(0, "OK ")
        val FIELD_IS_NOT_REPRESENTED = Result(10, "FIELD_IS_NOT_REPRESENTED ")
        val FIELD_HAS_WRONG_TYPE = Result(20, "FIELD_HAS_WRONG_TYPE ")

    }

}




