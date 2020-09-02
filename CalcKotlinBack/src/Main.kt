import help_functions.equationSimplifier
import help_functions.equationTokenizer

fun main() {
    val input = readLine()
    var tokenizedEq = equationTokenizer(input!!)
    tokenizedEq = equationSimplifier(tokenizedEq)
    val testTree = EquationTree(tokenizedEq)
    val result = testTree.computeEquation()
    if (result.getLower() != 1) println("${result.getUpper()}/${result.getLower()}")
    else println("${result.getUpper()}")
}