package inam.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeUtils {

	public static String getSmallestDateTime(String currentDateTime, String previousDateTime) {
		if (previousDateTime.equalsIgnoreCase("") || previousDateTime.isEmpty() || previousDateTime==null) {
			return currentDateTime;
		}
		String smallestDateTime = "";
		LocalDateTime currentDateTimeObject = parseStringAndGetFormattedTimeAsLocalDateTime(currentDateTime);
		LocalDateTime previousDateTimeObject = parseStringAndGetFormattedTimeAsLocalDateTime(previousDateTime);

		// TODO

		return smallestDateTime;
	}

	public static String getLargestDateTime(String currentDateTime, String previousDateTime) {
		if (previousDateTime.equalsIgnoreCase("") || previousDateTime.isEmpty() || previousDateTime==null) {
			return currentDateTime;
		}
		String largestDateTime = "";
		LocalDateTime currentDateTimeObject = parseStringAndGetFormattedTimeAsLocalDateTime(currentDateTime);
		LocalDateTime previousDateTimeObject = parseStringAndGetFormattedTimeAsLocalDateTime(previousDateTime);

		// TODO

		return largestDateTime;
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
