package com.wind.me.verticalviewpagerlib

import android.util.Log

/**
 * Created by xiawanli on 2018/5/12.
 */

val DEBUG = true


inline fun LogE(tag: String?, msg: String?) {
    if (DEBUG) {
        Log.e(tag, msg)
    }
}

inline fun LogD(tag: String?, msg: String?) {
    if (DEBUG) {
        Log.d(tag, msg)
    }
}

inline fun LogI(tag: String?, msg: String?) {
    if (DEBUG) {
        Log.e(tag, msg)
    }
}

inline fun LogW(tag: String?, msg: String?) {
    if (DEBUG) {
        Log.w(tag, msg)
    }
}