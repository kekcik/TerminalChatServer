package com.yst.demo.yapi.documentation

import com.yst.demo.jsonArray
import com.yst.demo.jsonObject
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import spark.Request
import spark.Response
import spark.Route
import com.yst.demo.yapi.Resolver
import tracking.server.yapi.documentation.StructValue
import java.util.*

class DocumentationResolver(
        resolvers: Collection<Resolver>,
        vararg enums: Pair<String, List<Pair<Int, String>>>
) : Route {

    val documentationPage: ByteArray

    init {
        val reses = ArrayList<Resolver>()
        reses.addAll(resolvers)
        reses.sortBy(Resolver::getURL)

        documentationPage = jsonObject(
                "methods" to jsonArray {
                    for (resolver in reses) {
                        val doc = resolver.getDoc()
                        add(jsonObject(
                                "name" to doc.name,
                                "description" to doc.description,
                                "in" to buildParametersTable(doc.input),
                                "out" to buildParametersTable(doc.output),
                                "result_codes" to jsonArray {
                                    for (availableCodes in doc.resultCodes) {
                                        add(jsonObject(
                                                "description" to availableCodes.component2(),
                                                "code" to availableCodes.component1().code
                                        ))
                                    }
                                }
                        ))
                    }
                },
                "enums" to jsonArray {
                    for (e in enums) {
                        add(enums(e.first, e.second))
                    }
                }

        ).toJSONString().toByteArray()
    }

    private fun enums(name: String, values: List<Pair<Int, String>>): JSONObject {
        return jsonObject(
                "name" to name,
                "values" to jsonArray {
                    for (r in values) {
                        add(jsonObject(
                                "code" to r.first,
                                "name" to r.second
                        ))
                    }
                }
        )
    }

    private fun buildParametersTable(parameters: Iterable<Value>): JSONArray {
        return jsonArray {
            for (value in parameters) {
                if (value is ArrayValue) {
                    add(
                            jsonObject(
                                    "name" to value.name,
                                    "type" to "array",
                                    "description" to value.description,
                                    "inner" to buildParametersTable(value.values)
                            )
                    )
                } else if (value is StructValue) {
                    add(
                            jsonObject(
                                    "name" to value.name,
                                    "type" to "struct",
                                    "description" to value.description,
                                    "inner" to buildParametersTable(value.values)
                            )
                    )
                } else {
                    add(
                            jsonObject(
                                    "name" to value.name,
                                    "type" to value.type.toString(),
                                    "description" to value.description
                            )
                    )
                }
            }
        }
    }

    override fun handle(request: Request, response: Response): Any {
        return documentationPage
    }

}
