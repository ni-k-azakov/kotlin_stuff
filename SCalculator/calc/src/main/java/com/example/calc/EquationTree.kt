import java.util.*

class EquationTree {
    constructor(token_list: Array<String>) {
        _head = null
        _bracketsAmount = 0
        _nodeList = emptyArray()
        _firstPriority = LinkedList<Int>()
        _secondPriority = LinkedList<Int>()
        _thirdPriority = LinkedList<Int>()
        exception = ""
        for (token in token_list) {
            if (token[0] == 'E') {
                exception = "ERROR: UNEXPECTED OPERATOR"
                return
            }
            _nodeList += EquationNode(token)
        }
        var firstIndex = -1
        for (i in _nodeList.indices) {
            if (_nodeList[i].getOperator() == "(") {
                if (_bracketsAmount == 0) {
                    firstIndex = i
                }
                _bracketsAmount++
            }
            if (_nodeList[i].getOperator() == ")") {
                _bracketsAmount--
                if (_bracketsAmount == 0) {
                    initHelper(firstIndex, i)
                    firstIndex = -1
                }
            }
        }
        if (_bracketsAmount != 0) {
            exception = "ERROR: INCORRECT BRACKET STRUCTURE"
            return
        }
        for (node in _nodeList) {
            if (node.getOperator() != "(" && node.getOperator() != ")" && node.getParent() == null) _head = node
        }
    }
    private fun initHelper(first: Int, last: Int) {
        var firstIndex = -1
        for (i in first + 1 until last) {
            if (_nodeList[i].getOperator() == "(") {
                if (_bracketsAmount == 0) {
                    firstIndex = i
                }
                _bracketsAmount++
            }
            if (_nodeList[i].getOperator() == ")") {
                _bracketsAmount--
                if (_bracketsAmount == 0) {
                    initHelper(firstIndex, i)
                    firstIndex = -1
                }
            }
        }
        if (_bracketsAmount != 0) {
            exception = "ERROR: INCORRECT BRACKET STRUCTURE"
            return
        }
        for (i in first + 1 until last) {
            if (_nodeList[i].isOperator()) {
                if (_nodeList[i].getOperator() == "(") _bracketsAmount++
                if (_bracketsAmount == 0) {
                    when (_nodeList[i].getOperator()[0]) {
                        '^' -> _firstPriority.add(i)
                        '*', '/' -> _secondPriority.add(i)
                        '+', '-' -> _thirdPriority.add(i)
                        else -> {
                            exception = "ERROR: UNEXPECTED OPERATOR"
                            return
                        }
                    }
                }
                if (_nodeList[i].getOperator() == ")") _bracketsAmount--
            }
        }
        while (_firstPriority.size != 0) {
            operatorProcessor(_firstPriority.element())
            _firstPriority.remove()
        }

        while (_secondPriority.size != 0) {
            operatorProcessor(_secondPriority.element())
            _secondPriority.remove()
        }

        while (_thirdPriority.size != 0) {
            operatorProcessor(_thirdPriority.element())
            _thirdPriority.remove()
        }
    }
    private fun operatorProcessor(index: Int) {
        var i = 1
        if (index + i == _nodeList.size) {
            exception = "ERROR: INCORRECT EQUATION STRUCTURE"
            return
        }
        while (!_nodeList[index + i].isValue()) {
            if (_nodeList[index + i].getOperator() != ")" &&
                _nodeList[index + i].getOperator() != "(") {
                exception = "ERROR: INCORRECT EQUATION STRUCTURE"
                return
            }
            i++
            if (index + i == _nodeList.size) {
                exception = "ERROR: INCORRECT EQUATION STRUCTURE"
                return
            }
        }
        var tempNode: EquationNode = _nodeList[index + i]
        while (tempNode.getParent() != null) {
            tempNode = tempNode.getParent()!!
        }
        tempNode.setParent(_nodeList[index])
        _nodeList[index].setRightToken(tempNode)

        i = 1
        if (index + i == _nodeList.size) {
            exception = "ERROR: INCORRECT EQUATION STRUCTURE"
            return
        }
        while (!_nodeList[index - i].isValue()) {
            if (_nodeList[index + i].getOperator() != ")" &&
                _nodeList[index + i].getOperator() != "(") {
                exception = "ERROR: INCORRECT EQUATION STRUCTURE"
                return
            }
            i++
            if (index + i == _nodeList.size) {
                exception = "ERROR: INCORRECT EQUATION STRUCTURE"
                return
            }
        }
        tempNode = _nodeList[index - i]
        while (tempNode.getParent() != null) {
            tempNode = tempNode.getParent()!!
        }
        tempNode.setParent(_nodeList[index])
        _nodeList[index].setLeftToken(tempNode)
    }
    fun computeEquation(): SimpleFraction {
        if (exception.isNotEmpty()) return SimpleFraction(-1, -1)
        return _head!!.computeValue()
    }
    private var _firstPriority: Queue<Int>
    private var _secondPriority: Queue<Int>
    private var _thirdPriority: Queue<Int>
    private var _bracketsAmount: Int
    private var _head: EquationNode? = null
    private var _nodeList: Array<EquationNode>
    var exception: String
}