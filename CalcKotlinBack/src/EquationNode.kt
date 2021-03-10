import help_functions.decimalToFraction


class EquationNode(token: String) {
    private var _value: String = ""
    private var _operator: String = "_"
    private var _parentToken: EquationNode? = null
    private lateinit var _leftToken: EquationNode
    private lateinit var _rightToken: EquationNode

    init {
        when (token[0]) {
            '(', ')', '+', '-', '*', '/', '^' -> _operator = token
            else -> _value = token
        }
    }
    fun computeValue(): SimpleFraction {
        when (_operator[0]) {
            '_' -> return decimalToFraction(CustomFloat(_value))
            '+' -> return _leftToken.computeValue() + _rightToken.computeValue()
            '-' -> return _leftToken.computeValue() - _rightToken.computeValue()
            '*' -> return _leftToken.computeValue() * _rightToken.computeValue()
            '/' -> return _leftToken.computeValue() / _rightToken.computeValue()
            '^' -> return _leftToken.computeValue() % _rightToken.computeValue()
            else -> return decimalToFraction(CustomFloat("0"))
        }
    }
    fun setLeftToken(token: EquationNode) {
        _leftToken = token
    }
    fun setRightToken(token: EquationNode) {
        _rightToken = token
    }
    fun isValue(): Boolean {
        if (_value.isEmpty()) return false
        return true
    }
    fun isOperator(): Boolean {
        if (_operator == "_") return false
        return true
    }
    fun getParent(): EquationNode? {
        return _parentToken
    }
    fun setParent(node: EquationNode) {
        _parentToken = node
    }
    fun getValue(): String {
        return _value
    }
    fun getOperator(): String {
        return _operator
    }
    fun getLeftToken(): EquationNode {
        return _leftToken
    }
    fun getRightToken(): EquationNode {
        return  _rightToken
    }
}