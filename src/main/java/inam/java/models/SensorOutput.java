package inam.java.models;

import java.util.Arrays;

public class SensorOutput {
	private int id;
	private String time;
	private float temperature;
	private int[] voltage;
	private float[] current;
	private float[] power;

	public SensorOutput() {}
	public SensorOutput(int id, String time, float temperature, int[] voltage, float[] current, float[] power) {
		this.id = id;
		this.time = time;
		this.temperature = temperature;
		this.voltage = voltage;
		this.current = current;
		this.power = power;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public float getTemperature() {
		return temperature;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	public int[] getVoltage() {
		return voltage;
	}

	public void setVoltage(int[] voltage) {
		this.voltage = voltage;
	}

	public float[] getCurrent() {
		return current;
	}

	public void setCurrent(float[] current) {
		this.current = current;
	}

	public float[] getPower() {
		return power;
	}

	public void setPower(float[] power) {
		this.power = power;
	}

	@Override
	public String toString() {
		return "{" +
				"id=" + id +
				", time='" + time + '\'' +
				", temperature=" + temperature +
				", voltage=" + Arrays.toString(voltage) +
				", current=" + Arrays.toString(current) +
				", power=" + Arrays.toString(power) +
				'}';
	}
}
