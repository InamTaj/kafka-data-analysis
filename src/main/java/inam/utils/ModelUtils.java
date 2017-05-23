package inam.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import inam.java.models.SensorInput;
import inam.java.models.SensorOutput;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class ModelUtils {
	public enum MessageType {INCOMING, OUTGOING}

	public static SensorInput parseStringToSensorInputModel(String line, MessageType messageType) {
		Gson gson = new Gson();
		JsonObject jsonObj = new JsonParser().parse(line).getAsJsonObject();
		int[] v = null;
		int[] i = null;
		switch(messageType) {
			case OUTGOING:
				v = gson.fromJson(jsonObj.get("v").getAsString(), int[].class);
				i = gson.fromJson(jsonObj.get("i").getAsString(), int[].class);
				break;
			case INCOMING:
				v = gson.fromJson(jsonObj.get("v"), int[].class);
				i = gson.fromJson(jsonObj.get("i"), int[].class);
				break;
		}

		return new SensorInput(
				jsonObj.get("id").getAsInt(),
				jsonObj.get("ts").getAsLong(),
				jsonObj.get("t").getAsInt()
				,v, i
		);
	}

	public static SensorOutput parseSensorInputToSensorOutput(SensorInput input) {
		SensorOutput output = new SensorOutput();
		int[] inputCurrents = input.getI(), inputVolts = input.getV();
		float[] outputCurrents, powerVals=null;

		output.setId(input.getId());

		// convert epoch to Timestamp
		LocalDateTime dateTime = Instant.ofEpochMilli(input.getTs() * 1000).atZone(ZoneId.systemDefault()).toLocalDateTime();
		output.setTime(dateTime.toString());

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
			powerVals[j] = (inputVolts[j] * inputCurrents[j]) / 1000;
		}
		output.setPower(powerVals);

		// return object
		return output;
	}
}
