package com.example.takuro.mimeidentaku

import android.text.format.*
import java.text.SimpleDateFormat
import java.util.*

class DateTime() {
    private var mCalendar: Calendar = Calendar.getInstance().also { it.clear() }
    //DateTime ver 1.0
    //プロパティ 日付など各値を取得できる
    var year: Int = mCalendar.get(Calendar.YEAR)
        get() = mCalendar.get(Calendar.YEAR)
    var month: Int = mCalendar.get(Calendar.MONTH)
        get() = mCalendar.get(Calendar.MONTH)
    var day: Int = mCalendar.get(Calendar.DAY_OF_MONTH)
        get() = mCalendar.get(Calendar.DAY_OF_MONTH)
    var hour: Int = mCalendar.get(Calendar.HOUR_OF_DAY)
        get() = mCalendar.get(Calendar.HOUR_OF_DAY)
    var minute: Int = mCalendar.get(Calendar.MINUTE)
        get() = mCalendar.get(Calendar.MINUTE)
    var second: Int = mCalendar.get(Calendar.SECOND)
        get() = mCalendar.get(Calendar.SECOND)
    var isAm: Boolean = mCalendar.get(Calendar.AM_PM) == Calendar.AM
        get() = mCalendar.get(Calendar.AM_PM) == Calendar.AM
    var dayOfWeek: Int = mCalendar.get(Calendar.DAY_OF_WEEK)
        get() = mCalendar.get(Calendar.DAY_OF_WEEK)
    var dayOfYear: Int = mCalendar.get(Calendar.DAY_OF_YEAR)
        get() = mCalendar.get(Calendar.DAY_OF_YEAR)

    //日付計算用
    fun add(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int): DateTime {
        mCalendar.apply {
            add(Calendar.YEAR, year)
            add(Calendar.MONTH, month)
            add(Calendar.DAY_OF_MONTH, month)
            add(Calendar.HOUR, hour)
            add(Calendar.MINUTE, minute)
            add(Calendar.MILLISECOND, second)
        }
        return this
    }

    fun addYeats(year: Int): DateTime = this.also { it.mCalendar.add(Calendar.YEAR, year) }
    fun addMonths(month: Int): DateTime = this.also { it.mCalendar.add(Calendar.MONTH, month) }
    fun addDays(day: Int): DateTime = this.also { it.mCalendar.add(Calendar.DAY_OF_MONTH, day) }
    fun addHours(hour: Int): DateTime = this.also { it.mCalendar.add(Calendar.HOUR, hour) }
    fun addMinutes(minute: Int): DateTime = this.also { it.mCalendar.add(Calendar.MINUTE, minute) }
    fun addSeconds(second: Int): DateTime = this.also { it.mCalendar.add(Calendar.SECOND, second) }

    //日付を設定するメソッド
    fun setDateTime(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int) {
        mCalendar.set(year, month, day, hour, minute, second)
    }

    fun setDateTime(date: Date) {
        mCalendar.time = date
        mCalendar.set(Calendar.MILLISECOND, 0)
    }

    fun setDateTime(calendar: Calendar) {
        setDateTime(calendar.time)
    }

    //日付を文字列に変換するるメソッド
    override fun toString(): String = DateFormat.format("yyyy/MM/dd kk/mm/ss", mCalendar.time).toString()

    fun toString(format: String): String = DateFormat.format(format, mCalendar.time).toString()

    //その他メソッド　日付のクリア
    fun clear(): Unit = mCalendar.clear()

    //Date()の取得
    fun toDate(): Date = mCalendar.time

    //DateTime同士の比較 ==が使える
    override operator fun equals(dateTime: Any?): Boolean {
        return if (dateTime is DateTime) {
            compareTo(dateTime as DateTime) == 0
        } else {
            false
        }
    }

    //DateTime同士の比較 >=が使える
    operator fun compareTo(dateTime: DateTime): Int {
        val calendar: Calendar = Calendar.getInstance().also { it.time = dateTime.toDate() }
        return mCalendar.compareTo(calendar)
    }

    companion object {
        //現在の日時を所有したDateTimeオブジェクトを返す
        fun now(): DateTime {
            return DateTime().also {
                it.mCalendar = Calendar.getInstance()
                it.mCalendar.set(Calendar.MILLISECOND, 0)
            }
        }

        //文字列から日付を作成しDateTimeオブジェクトを返す
        fun parse(dateTimeString: String): DateTime {
            val dateTime = DateTime()
            try {
                dateTime.mCalendar.time =
                        SimpleDateFormat("yyyy/MM/dd kk:mm:ss", Locale.JAPAN).also { it.isLenient = false }
                            .parse(dateTimeString)
            } catch (e: Exception) {
                throw IllegalArgumentException("can't convert string to DateTime")
            }
            return dateTime
        }

        fun parse(dateTimeString: String, format: String): DateTime {
            val dateTime = DateTime()
            try {
                dateTime.mCalendar.time =
                        SimpleDateFormat(format, Locale.JAPAN).also { it.isLenient = false }.parse(dateTimeString)
            } catch (e: Exception) {
                throw IllegalArgumentException("can't convert string to DateTime")
            }
            return dateTime
        }

        //与えられた文字列を変換できるかどうか
        fun tryParse(dateTimeString: String): Boolean {
            return try {
                SimpleDateFormat("yyyy/MM/dd kk:mm:ss", Locale.JAPAN).also { it.isLenient = false }
                    .parse(dateTimeString)
                true
            } catch (e: Exception) {
                false
            }
        }

        fun tryParse(dateTimeString: String, format: String): Boolean {
            return try {
                SimpleDateFormat(format, Locale.JAPAN).also { it.isLenient = false }.parse(dateTimeString)
                true
            } catch (e: Exception) {
                false
            }
        }
    }
}