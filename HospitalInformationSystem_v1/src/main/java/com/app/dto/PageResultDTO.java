package com.app.dto;

public class PageResultDTO {

	private int count;
	private Object items;

	public PageResultDTO() {
		super();
	}

	public PageResultDTO(int count, Object items) {
		super();
		this.count = count;
		this.items = items;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Object getItems() {
		return items;
	}

	public void setItems(Object items) {
		this.items = items;
	}
}
