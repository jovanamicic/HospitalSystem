package com.app.dto;


public class PasswordDTO {
	
	private String newPassword;
	private String oldPassword;
	
	public PasswordDTO(){}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	@Override
	public String toString() {
		return "PasswordDTO [newPassword=" + newPassword + ", oldPassword=" + oldPassword + "]";
	}
	
}
