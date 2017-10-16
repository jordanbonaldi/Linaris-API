package net.neferett.linaris.utils;

import java.util.function.Predicate;

public class TimeUtils {
	public static Predicate<Long> CreateTestCoolDown(final long time) {
		return past -> getTimeLeft(past, time) > 0;
	}

	public static long getTimeLeft(final Long past, final long time) {
		return past / 1000 + time - System.currentTimeMillis() / 1000;
	}

	public static String getTimeLeftToString(final Long past, final long time) {
		return secondesToDayHoursMinutes(getTimeLeft(past, time));
	}

	public static String minutesToDayHoursMinutes(final int time) {
		final int days = time / 24 / 60;
		final int hours = time / 60 % 24;
		final int minutes = time % 60;
		final String result = (days > 0 ? " §e" + days + " §aJour" + (days > 1 ? "s" : "") : "")
				+ (hours > 0 ? " §e" + hours + " §aHeure" + (hours > 1 ? "s" : "") : "") + " §e" + minutes + " §aMinute"
				+ (minutes > 1 ? "s" : "");
		return result.trim();
	}

	public static String secondesToDayHoursMinutes(final long time) {
		final int secondes = (int) (time % 60);
		final int minutes = (int) (time / 60 % 60);
		final int hours = (int) (time / 3600 % 24);
		final int days = (int) (time / 86400);
		final String result = (days > 0 ? " §e" + days + " §aJour" + (days > 1 ? "s" : "") : "")
				+ (hours > 0 ? " §e" + hours + " §aHeure" + (hours > 1 ? "s" : "") : "")
				+ (minutes > 0 ? " §e" + minutes + " §aMinute" + (minutes > 1 ? "s" : "") : "")
				+ (secondes > 0 ? " §e" + secondes + " §aSeconde" + (secondes > 1 ? "s" : "") : "");
		return result.trim();
	}
}
