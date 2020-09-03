package help_functions
import CustomFloat
import SimpleFraction

fun decimalToFraction(value: CustomFloat): SimpleFraction {
    var tempValue = value.getValue()
    var lower = 1
    for (i in 0 until value.getPrecision()) {
        tempValue *= 10
        lower *= 10
    }
    val tempFraction = SimpleFraction(tempValue.toInt(), lower)
    tempFraction.relax()
    return tempFraction
}

fun equationTokenizer(equation: String): Array<String> {
    var tokenizedEq = arrayOf<String>()
    var tempString = ""
    for (token in equation) {
        if (token == ' ') {
            continue
        }
        if (token == '(' || token == ')' || token == '+' || token == '-' || token == '/'
            || token == '*' || token == '^') {
            if (tempString.isNotEmpty()) {
                tokenizedEq += tempString
                tempString = ""
            }
            tempString += token
            tokenizedEq += tempString
            tempString = ""
            continue
        }
        tempString += token
    }
    if (tempString.isNotEmpty()) {
        tokenizedEq += tempString
    }
    return tokenizedEq
}

fun equationSimplifier(equation: Array<String>): Array<String> {
    var simplifiedEquation = arrayOf<String>()
    simplifiedEquation += "("
    if (equation[0] == "-") {
        simplifiedEquation += "0"
    }
    for (i in equation.indices) {
        if (i - 1 > -1 && equation[i - 1] == "(" && equation[i] == "-") {
            simplifiedEquation += "0"
        }
        simplifiedEquation += equation[i]
    }
    simplifiedEquation += ")"
    return simplifiedEquation
}