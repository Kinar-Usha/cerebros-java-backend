package com.cerebros.models;

public class Preferences {

	private String  purpose;
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
	
	public Preferences() {}

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


	
	
	
	
	
	
    
	
}
