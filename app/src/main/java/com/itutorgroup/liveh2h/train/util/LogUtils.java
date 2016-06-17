package com.itutorgroup.liveh2h.train.util;

import android.util.Log;

import com.orhanobut.logger.Logger;

public class LogUtils {
	private static final String TAG = LogUtils.class.getSimpleName();
	private static final boolean IS_DEBUG = true;

	public static void v(String msg) {
		if (IS_DEBUG) {
			Logger.t(TAG).v(msg);
		}
	}

	public static void d(String msg) {
		if (IS_DEBUG) {
			Logger.t(TAG).d(msg);
		}
	}

	public static void i(String msg) {
		if (IS_DEBUG) {
			Logger.t(TAG).i(msg);
		}
	}

	public static void w(String msg) {
		if (IS_DEBUG) {
			Logger.t(TAG).w(msg);
		}
	}

	public static void e(String msg) {
		if (IS_DEBUG) {
			Logger.t(TAG).e(msg);
		}
	}
	
	public static void e(String msg, Throwable tr) {
		if (IS_DEBUG) {
			Logger.t(TAG).e(tr,msg);
		}
	}

}
