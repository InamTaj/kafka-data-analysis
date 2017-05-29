package inam.models;

import java.util.Arrays;

public class SensorDataClass {
	private int id;
	private float[] power;
	private String timeLowest;
	private String timeHighest;
	private double totalRunningHours;
	private double costPerHour;

	public SensorDataClass(int id) {
		this.id = id;
		setPower(new float[3]);
		setTimeLowest("");
		setTimeHighest("");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public double getTotalRunningHours() {
		return totalRunningHours;
	}

	public void setTotalRunningHours(double totalRunningHours) {
		this.totalRunningHours = totalRunningHours;
	}

	public double getCostPerHour() {
		return costPerHour;
	}

	public void setCostPerHour(double costPerHour) {
		this.costPerHour = costPerHour;
	}

	@Override
	public String toString() {
		return "{" +
				"id=" + id +
				", power=" + Arrays.toString(power) +
				", totalRunningHours='" + totalRunningHours + '\'' +
				", costPerHour=" + costPerHour +
				", timeLowest='" + timeLowest + '\'' +
				", timeHighest='" + timeHighest + '\'' +
				'}';
	}
}
