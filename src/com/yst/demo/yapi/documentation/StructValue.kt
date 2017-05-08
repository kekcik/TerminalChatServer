package tracking.server.yapi.documentation

import com.yst.demo.yapi.documentation.Value
import java.util.*

class StructValue(name: String, description: String) : Value(name, Value.Type.struct, description) {

    val values = ArrayList<Value>()

    fun addValue(value: Value): StructValue {
        values.add(value)
        return this
    }

    fun addValue(name: String, type: Value.Type, description: String): StructValue {
        return addValue(Value(name, type, description))
    }

    fun getValues(): Iterable<Value> {
        return values
    }


}
