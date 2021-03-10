import kotlin.math.abs

class SimpleFraction {
    constructor() {
        _upper = 1
        _lower = 1
    }
    constructor(upper: Int, lower: Int) {
        _upper = upper
        _lower = lower
    }
    fun getUpper(): Int {
        return _upper
    }
    fun getLower(): Int {
        return _lower
    }
    fun relax() {
        var startUpper: Int = abs(_upper)
        var startLower: Int = abs(_lower)
        while (startUpper != 0 && startLower != 0) {
            if (startUpper > startLower) {
                startUpper %= startLower
            } else {
                startLower %= startUpper
            }
        }
        _upper /= (startUpper + startLower)
        _lower /= (startUpper + startLower)
    }
    operator fun times(value: SimpleFraction): SimpleFraction {
        val temp = SimpleFraction(_upper * value.getUpper(), _lower * value.getLower())
        temp.relax()
        return temp
    }
    operator fun div(value: SimpleFraction): SimpleFraction {
        val temp = SimpleFraction(_upper * value.getLower(), _lower * value.getUpper())
        temp.relax()
        return temp
    }
    operator fun plus(value: SimpleFraction): SimpleFraction {
        val temp = SimpleFraction(_upper * value.getLower() + value.getUpper() * _lower, _lower * value.getLower())
        temp.relax()
        return temp
    }
    operator fun minus(value: SimpleFraction): SimpleFraction {
        val temp = SimpleFraction(_upper * value.getLower() - value.getUpper() * _lower, _lower * value.getLower())
        temp.relax()
        return temp
    }
    operator fun rem(power: SimpleFraction): SimpleFraction {
        if (power.getUpper() == 0) {
            return SimpleFraction()
        }
        val startUpper = _upper
        val startLower = _lower
        val temp = SimpleFraction(_upper, _lower)
        for (i in 1 until power.getUpper()) {
            temp._upper *= startUpper
            temp._lower *= startLower
        }
        temp.relax()
        return temp
    }
    private var _upper: Int
    private var _lower: Int
}