package inam.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TimeUtils {

	public static String getSmallestDateTime(String currentDateTime, String previousDateTime) {
		if (previousDateTime.equalsIgnoreCase("") || previousDateTime.isEmpty() || previousDateTime==null) {
			return currentDateTime;
		}
		// get difference between currentDateTimeObject and previousDateTimeObject and return the smallest
		double hours = getHoursDifferenceBetweenTimestamps(currentDateTime, previousDateTime);
		if (hours < 0) {
			return currentDateTime;
		}

		return previousDateTime;
	}

	public static String getLargestDateTime(String currentDateTime, String previousDateTime) {
		if (previousDateTime.equalsIgnoreCase("") || previousDateTime.isEmpty() || previousDateTime==null) {
			return currentDateTime;
		}
		// get difference between currentDateTimeObject and previousDateTimeObject and return the largest
		double hours = getHoursDifferenceBetweenTimestamps(currentDateTime, previousDateTime);
		if (hours > 0) {
			return currentDateTime;
		}

		return previousDateTime;
	}

	private static double getHoursDifferenceBetweenTimestamps(String currentDateTime, String previousDateTime) {
		// calculate total time in hours between two timestamps

		LocalDateTime currentDateTimeObject = parseStringAndGetFormattedTimeAsLocalDateTime(currentDateTime);
		LocalDateTime previousDateTimeObject = parseStringAndGetFormattedTimeAsLocalDateTime(previousDateTime);

		LocalDateTime fromTemp = LocalDateTime.from(previousDateTimeObject);
		long years = fromTemp.until(currentDateTimeObject, ChronoUnit.YEARS);
		fromTemp = fromTemp.plusYears(years);

		long months = fromTemp.until(currentDateTimeObject, ChronoUnit.MONTHS);
		fromTemp = fromTemp.plusMonths(months);

		long days = fromTemp.until(currentDateTimeObject, ChronoUnit.DAYS);
		fromTemp = fromTemp.plusDays(days);

		long hours = fromTemp.until(currentDateTimeObject, ChronoUnit.HOURS);
		fromTemp = fromTemp.plusHours(hours);

		long minutes = fromTemp.until(currentDateTimeObject, ChronoUnit.MINUTES);
		fromTemp = fromTemp.plusMinutes(minutes);

		long seconds = fromTemp.until(currentDateTimeObject, ChronoUnit.SECONDS);
		fromTemp = fromTemp.plusSeconds(seconds);

		long millis = fromTemp.until(currentDateTimeObject, ChronoUnit.MILLIS);

		double totalHours = 0;

		totalHours += years * 8760;
		totalHours += months * 730;
		totalHours += days * 24;
		totalHours += hours;
		totalHours += minutes / 60;

		return totalHours;
	}

	public static double getTotalHoursBetweenTwoDateTimes(String lowestTime, String highestTime) {
		// note: highestTime should be first argument
		return getHoursDifferenceBetweenTimestamps(highestTime, lowestTime);
	}

	public static String parseStringAndGetFormattedTimeAsString(String time) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Utils.DATE_TIME_FORMAT);
		LocalDateTime localDateTime = LocalDateTime.parse(time, formatter);
		return localDateTime.format(formatter);
	}

	public static LocalDateTime parseStringAndGetFormattedTimeAsLocalDateTime(String time) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Utils.DATE_TIME_FORMAT);
		LocalDateTime localDateTime = LocalDateTime.parse(time, formatter);
		return localDateTime;
	}

	public static String getFormattedTimeFromEpoch(Long epochs) {
		// convert epoch to LocalDateTime
		LocalDateTime dateTime = Instant.ofEpochMilli(epochs * 1000).atZone(ZoneId.systemDefault()).toLocalDateTime();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Utils.DATE_TIME_FORMAT);
		return dateTime.format(formatter);
	}
}
