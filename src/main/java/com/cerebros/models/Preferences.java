package com.cerebros.models;

import java.util.Objects;

public class Preferences {

	private String purpose;
	private String risk;
	private String time;
	private String income;

	public Preferences(String purpose, String risk, String time, String income) {
		super();
		this.purpose = purpose;
		this.risk = risk;
		this.time = time;
		this.income = income;
	}

	public Preferences() {
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getRisk() {
		return risk;
	}

	public void setRisk(String risk) {
		this.risk = risk;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	@Override
	public int hashCode() {
		return Objects.hash(income, purpose, risk, time);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Preferences other = (Preferences) obj;
		return Objects.equals(income, other.income) && Objects.equals(purpose, other.purpose)
				&& Objects.equals(risk, other.risk) && Objects.equals(time, other.time);
	}

}
