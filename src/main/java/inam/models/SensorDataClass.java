package inam.models;

import java.util.Arrays;

public class SensorDataClass {
	private int id;
	private float[] power;
	private String timeLowest;
	private String timeHighest;
	private String totalTimeRunning;
	private long costPerHour;

	public SensorDataClass() {
		setPower(new float[3]);
		setTimeLowest("");
		setTimeHighest("");
	}

	public SensorDataClass(int id) {
		this.id = id;
		setPower(new float[3]);
		setTimeLowest("");
		setTimeHighest("");
	}

	public SensorDataClass(int id, float[] power, String timeLowest, String timeHighest) {
		setId(id);
		setPower(power);
		setTimeLowest(timeLowest);
		setTimeHighest(timeHighest);
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

	public String getTotalTimeRunning() {
		return totalTimeRunning;
	}

	public void setTotalTimeRunning(String totalTimeRunning) {
		this.totalTimeRunning = totalTimeRunning;
	}

	public long getCostPerHour() {
		return costPerHour;
	}

	public void setCostPerHour(long costPerHour) {
		this.costPerHour = costPerHour;
	}

	@Override
	public String toString() {
		return "{" +
				"id=" + id +
				", power=" + Arrays.toString(power) +
				", totalTimeRunning='" + totalTimeRunning + '\'' +
				", costPerHour=" + costPerHour +
				", timeLowest='" + timeLowest + '\'' +
				", timeHighest='" + timeHighest + '\'' +
				'}';
	}
}
