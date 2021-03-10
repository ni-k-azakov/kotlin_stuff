class CustomFloat(value: String) {
    fun getValue(): Float {
        return _value
    }
    fun getPrecision(): Int {
        return _precision
    }
    fun getIntPart(): Int {
        return _intPart
    }
    private var _value: Float = 0.toFloat()
    private var _precision: Int = 0
    private var _intPart: Int = 0

    init {
        var i = 0
        while (i < value.length && value[i] != '.') {
            i++
        }
        var  j = 0
        while (i + j < value.length) {
            j++
        }
        if (j != 0) {
            j--
        }
        _value = value.toFloat()
        _precision = j
        _intPart = i
    }
}