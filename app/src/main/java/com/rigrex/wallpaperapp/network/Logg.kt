package com.rigrex.wallpaperapp.network

import android.util.Log

class Logg {
    companion object{
        val isDevMode = true
        val MAX_LOG_LENGTH = 4000
        fun i(TAG: String, s: String?) {
            if (TAG.length > 23) Log.e("_test", " invalid tag :: " + TAG.length) else {
                if (isDevMode) Log.i(TAG, s?:"")
            }
        }

        fun ii(TAG: String, s: String) {
            if (TAG.length > 23) Log.e("_test", " invalid tag :: " + TAG.length) else {
                if (isDevMode) moreLog(TAG, s)
            }
        }

        fun e(TAG: String, s: String?) {
            if (TAG.length > 23) Log.e("_test", ":: invalid tag ") else {
                if (isDevMode) Log.e(TAG, s?:"")
            }
        }

        /**
         * @return The line number of the code that ran this method
         * @author Brian_Entei
         */
        val lineNumber: Int
            get() = ___8drrd3148796d_Xaff()

        /**
         * This methods name is ridiculous on purpose to prevent any other method
         * names in the stack trace from potentially matching this one.
         *
         * @return The line number of the code that called the method that called
         * this method(Should only be called by getLineNumber()).
         * @author Brian_Entei
         */
        private fun ___8drrd3148796d_Xaff(): Int {
            var thisOne = false
            var thisOneCountDown = 1
            val elements =
                Thread.currentThread().stackTrace
            for (element in elements) {
                val methodName = element.methodName
                val lineNum = element.lineNumber
                if (thisOne && thisOneCountDown == 0) {
                    return lineNum
                } else if (thisOne) {
                    thisOneCountDown--
                }
                if (methodName == "___8drrd3148796d_Xaff") {
                    thisOne = true
                }
            }
            return -1
        }

        private fun moreLog(TAG: String, message: String) {
            // Split by line, then ensure each line can fit into Log's maximum length.
            var i = 0
            val length = message.length
            while (i < length) {
                var newline = message.indexOf('\n', i)
                newline = if (newline != -1) newline else length
                do {
                    val end = Math.min(newline, i + MAX_LOG_LENGTH)
                    Log.i(TAG, message.substring(i, end))
                    i = end
                } while (i < newline)
                i++
            }
        }
    }
}