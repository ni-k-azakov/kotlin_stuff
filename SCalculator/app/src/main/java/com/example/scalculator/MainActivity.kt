package com.example.scalculator

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
            R.id.button_dot -> equation.text.append('.')
            R.id.clear_button -> equation.text.clear()
            R.id.delete_button -> {
                if (equation.text.isNotEmpty()) {
                    equation.text.delete(equation.text.length - 1, equation.text.length)
                }
            }
//            R.id.equal_button -> {
//                val calculate = ProcessBuilder("libs/Calculator.exe", "2+1")
//                calculate.start()
//                val input = File("com/example/scalculator/Result.txt")
//                equation.text.clear()
//                equation.text.append(input.readLines()[0])
//            }
        }
    }

//    fun toastMe(view: View) {
//        val myToast = Toast.makeText(this, "SCalc is ready!", Toast.LENGTH_SHORT)
//        myToast.show()
//    }

//    fun startApp(view: View) {
//        val startIntent = Intent(this, SecondActivity::class.java)
//        val countString = numberView.text.toString()
//        val count = Integer.parseInt(countString)
//        startIntent.putExtra(SecondActivity.TOTAL_COUNT, count)
//        startActivity(startIntent)
//    }
}
