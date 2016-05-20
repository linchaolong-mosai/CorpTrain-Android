package com.mosai.corporatetraining.util;

import android.util.Log;

public class LogUtils {
	private static final String TAG = LogUtils.class.getSimpleName();
	private static final boolean IS_DEBUG = true;

	public static void v(String msg) {
		if (IS_DEBUG) {
			Log.v(TAG, msg);
		}
	}

	public static void d(String msg) {
		if (IS_DEBUG) {
			Log.d(TAG, msg);
		}
	}

	public static void i(String msg) {
		if (IS_DEBUG) {
			Log.i(TAG, msg);
		}
	}

	public static void w(String msg) {
		if (IS_DEBUG) {
			Log.w(TAG, msg);
		}
	}

	public static void e(String msg) {
		if (IS_DEBUG) {
			Log.e(TAG, msg);
		}
	}
	
	public static void e(String msg, Throwable tr) {
		if (IS_DEBUG) {
			Log.e(TAG, msg, tr);
		}
	}

}
