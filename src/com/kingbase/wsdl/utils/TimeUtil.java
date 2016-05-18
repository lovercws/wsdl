package com.kingbase.wsdl.utils;

public final class TimeUtil {
	private TimeUtil() {
	}

	public static long getCurrentTimeInSeconds() {
		return System.currentTimeMillis() / 1000;
	}
}
