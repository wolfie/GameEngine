package com.github.wolfie.engine;

public class Util {
	private static final long NANOS_IN_SECOND = 1000000000;
	private static final long MILLIS_IN_SECOND = 1000;
	private static final long NANOS_IN_MILLI = 1000000;

	public static double nanoSecondsToSeconds(final long ns) {
		return (double) ns / (double) NANOS_IN_SECOND;
	}

	public static long secondsToNanoSeconds(final double s) {
		return (long) (s * NANOS_IN_SECOND);
	}

	public static long secondsToMilliSeconds(final double s) {
		return (long) (s * MILLIS_IN_SECOND);
	}

	public static double milliSecondsToSeconds(final long ms) {
		return (double) ms / (double) MILLIS_IN_SECOND;
	}

	public static double nanoSecondsToMilliSeconds(final long ns) {
		return (double) ns / (double) NANOS_IN_MILLI;
	}

	public static long milliSecondsToNanoSeconds(final long ms) {
		return ms * NANOS_IN_MILLI;
	}
}
