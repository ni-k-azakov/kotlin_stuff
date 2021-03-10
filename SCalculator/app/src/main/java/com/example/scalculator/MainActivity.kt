package com.example.scalculator

import EquationTree
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import help_functions.equationSimplifier
import help_functions.equationTokenizer
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        equation.text.clear()
    }

   fun buttonProcessing(view: View) {
        when (view.getId())  {
            R.id.button_0 -> equation.text.append('0')
            R.id.button_1 -> equation.text.append('1')
            R.id.button_2 -> equation.text.append('2')
            R.id.button_3 -> equation.text.append('3')
            R.id.button_4 -> equation.text.append('4')
            R.id.button_5 -> equation.text.append('5')
            R.id.button_6 -> equation.text.append('6')
            R.id.button_7 -> equation.text.append('7')
            R.id.button_8 -> equation.text.append('8')
            R.id.button_9 -> equation.text.append('9')
            R.id.button_open_bracket -> equation.text.append('(')
            R.id.button_close_bracket -> equation.text.append(')')
            R.id.button_plus -> equation.text.append('+')
            R.id.button_minus -> equation.text.append('-')
            R.id.button_del -> equation.text.append('/')
            R.id.button_mult -> equation.text.append('*')
            R.id.button_pow -> equation.text.append('^')
            R.id.button_dot -> {
                when (equation.text.toString()[equation.text.length - 1]) {
                    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> equation.text.append('.')
                }
            }
            R.id.clear_button -> equation.text.clear()
            R.id.delete_button -> {
                if (equation.text.isNotEmpty()) {
                    equation.text.delete(equation.text.length - 1, equation.text.length)
                }
            }
            R.id.equal_button -> {
                var tokenizedEq = equationTokenizer(equation.text.toString())
                tokenizedEq = equationSimplifier(tokenizedEq)
                val testTree = EquationTree(tokenizedEq)
                val result = testTree.computeEquation()
                equation.text.clear()
                if (result.getUpper() == -1 && result.getLower() == -1) equation.text.append(testTree.exception)
                else {
                    val floatResult = result.getUpper().toFloat() / result.getLower().toFloat()
                    val testResult = floatResult.toInt()
                    if (floatResult == testResult.toFloat()) equation.text.append(
                        result.getUpper().toString()
                    )
                    else equation.text.append(
                        result.getUpper().toString() + "/" + result.getLower().toString()
                    )
                }
            }
            else -> {
                equation.text.clear()
                equation.text.append("Error")
            }
        }
    }
}
