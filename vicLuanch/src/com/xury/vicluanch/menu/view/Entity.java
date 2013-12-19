package com.xury.vicluanch.menu.view;

public class Entity {
	private String name;
	private int id;
	private int count=-1;
	private boolean isNews;
	private int type;
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	private String action;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public boolean isNews() {
		return isNews;
	}

	public void setNews(boolean isNews) {
		this.isNews = isNews;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
