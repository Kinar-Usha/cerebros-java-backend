package com.cerebros.models;

public class ActivityReport {
	 private String clientId;
	    private String reportContent;

	    public ActivityReport(String clientId, String reportContent) {
	        this.clientId = clientId;
	        this.reportContent = reportContent;
	    }

	    public String getClientId() {
	        return clientId;
	    }

	    public String getReportContent(String clientId) {
	        return reportContent;
	    }
}
