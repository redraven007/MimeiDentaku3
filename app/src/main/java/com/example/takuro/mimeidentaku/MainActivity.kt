package com.example.takuro.mimeidentaku

import android.app.Activity
import android.content.Context
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import com.example.takuro.mimeidentaku.DateTime

public class MainActivity : AppCompatActivity() {
    internal var recentOperator = R.id.button_equal // 最近押された計算キー
    internal var result: Double = 0.toDouble()  // 計算結果
    internal var isOperatorKeyPushed: Boolean = false    // 計算キーが押されたことを記憶
    internal var isTypingNum: Boolean = false // 数値入力中か否かの判定
    internal var timeMagicFlag: Boolean = false // 現在時刻が表示されるマジックをやるかどうかのフラグ

    internal var buttonListener: View.OnClickListener =
        View.OnClickListener {
            init()
            timeMagicFlag = !timeMagicFlag
        }

    internal var buttonNumberListener: View.OnClickListener = View.OnClickListener { view ->
        if (recentOperator == R.id.button_equal && !isTypingNum) {
            init()
        }
        val button = view as Button
//        TODO なんか8桁数値入れるとバグる(計算結果が1.0EとかになってDoubleのtoStringがうまくいかなくなる)
//        なのでとりあえず7桁までしか入力できなくして暫定対応
//        ↑なんかいじってるうちに大丈夫になってきた感じあるけどまだよくわからんしそのままです
        if (maintext.text.toString().length < 7 || !isTypingNum) {
//      直前に押された演算記号が"="だった場合はACと同じ処理
            var mainTextString = maintext.text.toString()
            if (isOperatorKeyPushed == true) {
                maintext.setText(button.text)
            } else {
                if (mainTextString.equals("") || mainTextString == null) {
                    maintext.text = button.text
                } else if (mainTextString.equals("0")) {
                    if (button.text.toString().equals(".")) {
                        maintext.append(button.text)
                    } else {
                        maintext.text = button.text
                    }
                } else if(mainTextString.substring(mainTextString.length - 1).equals(".")){
                    if (button.text.toString().equals(".")) {
                        //何もしない
                    } else {
                        maintext.append(button.text)
                    }
                }else{
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
        result = calc(recentOperator, operatorButton.id, result, value)
        var resultString = "%.7f".format(result)
        if (resultString.contains(".")) {
            while (resultString.substring(resultString.length - 1).equals("0")) {
                resultString = resultString.dropLast(1)
            }
            if (resultString.substring(resultString.length - 1).equals(".")) {
                resultString = resultString.dropLast(1)
            }
            //文字列を小数点で切って後半の小数点以下部分を3桁以下に
            if (resultString.contains(".")) {
                var array = resultString.split(".")
                if (array[0].length + array[1].length > 7) {
                    when (array[0].length) {
                        1 -> resultString = "%.6f".format(result)
                        2 -> resultString = "%.5f".format(result)
                        3 -> resultString = "%.4f".format(result)
                        4 -> resultString = "%.3f".format(result)
                        5 -> resultString = "%.2f".format(result)
                        6 -> resultString = "%.1f".format(result)
                    }
                }
            }
        }
        if (resultString.length > 8) {
            result = 99999999.0
            maintext.setText("99999999")
        } else {
            maintext.setText(resultString)
        }

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

    internal fun calcStandard(operator: Int, value1: Double, value2: Double): Double {
        when (operator) {
            R.id.button_add -> return value1 + value2
            R.id.button_subtract -> return value1 - value2
            R.id.button_multiply -> return value1 * value2
            R.id.button_divide -> return value1 / value2
            else -> return value2
        }
    }

    internal fun getDate(): String {

        var date: Date = Date()
        var df = SimpleDateFormat("MMddHHmm")
        df.setTimeZone(java.util.TimeZone.getTimeZone("Asia/Tokyo"))
        var dateString = df.format(date)

        if (dateString.substring(0, 1).equals("0")) {
            return dateString.drop(1)
        } else {
            return dateString
        }
    }

    internal fun calc(operator: Int, pushedOperator: Int, value1: Double, value2: Double): Double {
        if (timeMagicFlag && pushedOperator == R.id.button_equal) {
            return getDate().toDouble()
        } else {
            return calcStandard(operator, value1, value2)
        }
    }


    internal fun init() {
        recentOperator = R.id.button_equal
        isOperatorKeyPushed = false
        operator.text = ""
        maintext.text = "0"
        result = 0.0
    }
}
