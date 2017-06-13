package com.app.dto;

public class PaymentDTO {
	
	private String recipient;
	private String account;
	private Double amount;
	private String currency;
	private int managerId;
	
	public PaymentDTO(){}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public int getManagerId() {
		return managerId;
	}

	public void setManagerId(int managerId) {
		this.managerId = managerId;
	}

	@Override
	public String toString() {
		return "PaymentDTO [recipient=" + recipient + ", account=" + account + ", amount=" + amount + ", currency="
				+ currency + "]";
	}

}
