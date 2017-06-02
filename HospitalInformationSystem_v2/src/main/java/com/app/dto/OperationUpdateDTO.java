package com.app.dto;

public class OperationUpdateDTO {
	
	private int operationId;
	private int roomId;
	private String date;
	
	public int getOperationId() {
		return operationId;
	}
	public void setOperationId(int operationId) {
		this.operationId = operationId;
	}
	public int getRoomId() {
		return roomId;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "OperationUpdateDTO [operationId=" + operationId + ", roomId=" + roomId + ", date=" + date + "]";
	}
	
	

}
