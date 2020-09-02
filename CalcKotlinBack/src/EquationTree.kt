import java.util.*
import kotlin.system.exitProcess

class EquationTree {
    constructor(token_list: Array<String>) {
        _head = null
        _bracketsAmount = 0
        _nodeList = emptyArray()
        _firstPriority = LinkedList<Int>()
        _secondPriority = LinkedList<Int>()
        _thirdPriority = LinkedList<Int>()
        for (token in token_list) {
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
            println("INCORRECT BRACKET STRUCTURE")
            exitProcess(1)
        }
        for (node in _nodeList) {
            if (node.getOperator() != "(" && node.getOperator() != ")" && node.getParent() == null) _head = node
        }
    }
    fun initHelper(first: Int, last: Int) {
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
            println("INCORRECT BRACKET STRUCTURE")
            exitProcess(1)
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
                            println ("UNEXPECTED OPERATOR: ${_nodeList[i].getOperator()}\n")
                            exitProcess(1)
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
    fun operatorProcessor(index: Int) {
        var i = 0
        while (!_nodeList[index + i].isValue()) {
            i++
        }
        var tempNode: EquationNode = _nodeList[index + i]
        while (tempNode.getParent() != null) {
            tempNode = tempNode.getParent()!!
        }
        tempNode.setParent(_nodeList[index])
        _nodeList[index].setRightToken(tempNode)

        i = 0
        while (!_nodeList[index - i].isValue()) {
            i++
        }
        tempNode = _nodeList[index - i]
        while (tempNode.getParent() != null) {
            tempNode = tempNode.getParent()!!
        }
        tempNode.setParent(_nodeList[index])
        _nodeList[index].setLeftToken(tempNode)
    }
    fun computeEquation(): SimpleFraction {
        return _head!!.computeValue()
    }
    private var _firstPriority: Queue<Int>
    private var _secondPriority: Queue<Int>
    private var _thirdPriority: Queue<Int>
    private var _bracketsAmount: Int
    private var _head: EquationNode? = null
    private var _nodeList: Array<EquationNode>
}