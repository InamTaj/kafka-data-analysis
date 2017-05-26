package inam.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import inam.models.SensorInput;
import inam.models.SensorOutput;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ModelUtils {
	public static SensorInput convertStringReadFromFileToSensorInputModel(String line) {
		Gson gson = new Gson();
		JsonObject jsonObj = new JsonParser().parse(line).getAsJsonObject();
		int[] v = gson.fromJson(jsonObj.get("v").getAsString(), int[].class);;
		int[] i = gson.fromJson(jsonObj.get("i").getAsString(), int[].class);
		return new SensorInput(
				jsonObj.get("id").getAsInt(),
				jsonObj.get("ts").getAsLong(),
				jsonObj.get("t").getAsInt()
				,v, i
		);
	}

	public static SensorInput convertStringReadFromTopicToSensorInputModel(String line) {
		Gson gson = new Gson();
		JsonObject jsonObj = new JsonParser().parse(line).getAsJsonObject();
		int[] v = gson.fromJson(jsonObj.get("v"), int[].class);;
		int[] i = gson.fromJson(jsonObj.get("i"), int[].class);
		return new SensorInput(
				jsonObj.get("id").getAsInt(),
				jsonObj.get("ts").getAsLong(),
				jsonObj.get("t").getAsInt()
				,v, i
		);
	}

	public static SensorOutput convertStringReadFromTopicToSensorOutputModel(String line) {
		Gson gson = new Gson();
		JsonObject jsonObj = new JsonParser().parse(line).getAsJsonObject();
		int[] voltage = gson.fromJson(jsonObj.get("voltage"), int[].class);;
		float[] current = gson.fromJson(jsonObj.get("current"), float[].class);
		float[] power = gson.fromJson(jsonObj.get("power"), float[].class);
		// setting time
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Utils.DATE_TIME_FORMAT);
		LocalDateTime localDateTime = LocalDateTime.parse(jsonObj.get("time").getAsString(), formatter);
		String time = localDateTime.format(formatter);

		return new SensorOutput(
				jsonObj.get("id").getAsInt(),
				time,
				jsonObj.get("temperature").getAsFloat(),
				voltage,
				current,
				power
		);
	}

	public static SensorOutput parseSensorInputToSensorOutput(SensorInput input) {
		SensorOutput output = new SensorOutput();
		int[] inputCurrents = input.getI(), inputVolts = input.getV();
		float[] outputCurrents, powerVals=null;

		output.setId(input.getId());

		// convert epoch to LocalDateTime
		LocalDateTime dateTime = Instant.ofEpochMilli(input.getTs() * 1000).atZone(ZoneId.systemDefault()).toLocalDateTime();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Utils.DATE_TIME_FORMAT);
		String time = dateTime.format(formatter);
		output.setTime(time);

		output.setTemperature(input.getT() / 100);
		output.setVoltage(inputVolts);

		// convert current's values
		outputCurrents = new float[inputCurrents.length];
		for (int i=0; i<inputCurrents.length; i++) {
			outputCurrents[i] = inputCurrents[i] / 100;
		}
		output.setCurrent(outputCurrents);

		// calculate & set power values
		powerVals = new float[inputCurrents.length];
		for (int j=0; j<inputVolts.length; j++) {
			powerVals[j] = (inputVolts[j] * outputCurrents[j]) / 1000;
		}
		output.setPower(powerVals);

		// return object
		return output;
	}
}
