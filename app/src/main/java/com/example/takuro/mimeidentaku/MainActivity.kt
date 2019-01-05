package com.example.takuro.mimeidentaku

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

public class MainActivity : AppCompatActivity() {
    internal var recentOperator = R.id.button_equal // 最近押された計算キー
    internal var result: Double = 0.toDouble()  // 計算結果
    internal var isOperatorKeyPushed: Boolean = false    // 計算キーが押されたことを記憶
    internal var isTypingNum :Boolean = false // 数値入力中か否かの判定
    internal var timeMagicFlag :Boolean = false // 現在時刻が表示されるマジックをやるかどうかのフラグ

    internal var buttonListener: View.OnClickListener =
        View.OnClickListener {
            init()
            timeMagicFlag = true
        }

    internal var buttonNumberListener: View.OnClickListener = View.OnClickListener { view ->
        val button = view as Button
        if(maintext.text.toString().length < 10) {
//      直前に押された演算記号が"="だった場合はACと同じ処理
            if (recentOperator == R.id.button_equal && isTypingNum == false) {
                init()
            }
            if (isOperatorKeyPushed == true) {
                maintext.setText(button.text)
            } else {
                if (maintext.text.equals("") || maintext.text == null) {
                    maintext.text = button.text
                } else if (maintext.text.toString().equals("0")) {
                    if (button.text.toString().equals(".")) {
                        maintext.append(button.text)
                    } else {
                        maintext.text = button.text
                    }
                } else {
                    maintext.append(button.text)
                }
            }
            isTypingNum = true
            isOperatorKeyPushed = false
        }
    }

    internal var buttonOperatorListener: View.OnClickListener = View.OnClickListener { view ->
        val operatorButton = view as Button
        val value = java.lang.Double.parseDouble(maintext.text.toString())
//        if (recentOperator == R.id.button_equal) {
//            result = value
//        } else {
            result = calc(recentOperator, result, value)
            var resultString = "%.7f".format(result)
            if(resultString.contains(".")){
                while(resultString.substring(resultString.length - 1).equals("0")){
                    resultString = resultString.dropLast(1)
                }
            }
            if(resultString.substring(resultString.length - 1).equals(".")){
                maintext.setText(resultString.dropLast(1))
            }else{
                maintext.setText(resultString)
            }
//        }

        recentOperator = operatorButton.id
        operator.text = operatorButton.text
        isOperatorKeyPushed = true
        isTypingNum = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById(R.id.operator) as TextView
        findViewById(R.id.maintext) as TextView
        findViewById(R.id.button) as Button
        button.setOnClickListener(buttonListener)

        findViewById<Button>(R.id.button_1).setOnClickListener(buttonNumberListener)
        findViewById<Button>(R.id.button_2).setOnClickListener(buttonNumberListener)
        findViewById<Button>(R.id.button_3).setOnClickListener(buttonNumberListener)
        findViewById<Button>(R.id.button_4).setOnClickListener(buttonNumberListener)
        findViewById<Button>(R.id.button_5).setOnClickListener(buttonNumberListener)
        findViewById<Button>(R.id.button_6).setOnClickListener(buttonNumberListener)
        findViewById<Button>(R.id.button_7).setOnClickListener(buttonNumberListener)
        findViewById<Button>(R.id.button_8).setOnClickListener(buttonNumberListener)
        findViewById<Button>(R.id.button_9).setOnClickListener(buttonNumberListener)
        findViewById<Button>(R.id.button_0).setOnClickListener(buttonNumberListener)
        findViewById<Button>(R.id.button_dot).setOnClickListener(buttonNumberListener)

        findViewById<Button>(R.id.button_add).setOnClickListener(buttonOperatorListener)
        findViewById<Button>(R.id.button_subtract).setOnClickListener(buttonOperatorListener)
        findViewById<Button>(R.id.button_multiply).setOnClickListener(buttonOperatorListener)
        findViewById<Button>(R.id.button_divide).setOnClickListener(buttonOperatorListener)
        findViewById<Button>(R.id.button_equal).setOnClickListener(buttonOperatorListener)

    }

    internal fun calc(operator: Int, value1: Double, value2: Double): Double {
        when (operator) {
            R.id.button_add -> return value1 + value2
            R.id.button_subtract -> return value1 - value2
            R.id.button_multiply -> return value1 * value2
            R.id.button_divide -> return value1 / value2
            else -> return value2
        }
    }

    internal fun init(){
        recentOperator = R.id.button_equal
        isOperatorKeyPushed = false
        operator.text = ""
        maintext.text = "0"
        result = 0.0
    }
}
