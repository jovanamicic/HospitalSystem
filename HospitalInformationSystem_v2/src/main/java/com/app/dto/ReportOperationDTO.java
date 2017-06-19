package com.app.dto;

public class ReportOperationDTO {
	
	private String name;
	private String startDate;
	private String endDate;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	@Override
	public String toString() {
		return "ReportOperationDTO [name=" + name + ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}
	
	
	
	

}
