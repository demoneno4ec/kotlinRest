package myFirstRestApi.test

import java.lang.NumberFormatException

class Utils {
    companion object {
        fun convertToInt(value: String?): Int {
            return try {
                value?.toInt()!!
            } catch (e: NumberFormatException) {
                0
            }
        }
    }
}