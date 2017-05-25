package inam.models;

import java.util.Arrays;

public class SensorDataClass {
	private float[] power;
	private String timeLowest;
	private String timeHighest;

	public SensorDataClass() {
		setPower(new float[3]);
		setTimeLowest("");
		setTimeHighest("");
	}

	public SensorDataClass(float[] power, String timeLowest, String timeHighest) {
		this.power = power;
		this.timeLowest = timeLowest;
		this.timeHighest = timeHighest;
	}

	public float[] getPower() {
		return power;
	}

	public void setPower(float[] power) {
		this.power = power;
	}

	public String getTimeLowest() {
		return timeLowest;
	}

	public void setTimeLowest(String timeLowest) {
		this.timeLowest = timeLowest;
	}

	public String getTimeHighest() {
		return timeHighest;
	}

	public void setTimeHighest(String timeHighest) {
		this.timeHighest = timeHighest;
	}

	@Override
	public String toString() {
		return "SensorDataClass{" +
				"power=" + Arrays.toString(power) +
				", timeLowest='" + timeLowest + '\'' +
				", timeHighest='" + timeHighest + '\'' +
				'}';
	}
}
