package com.dxa.utils.print;

/**
 * 打印日志的
 */
public enum PrintLevel {
	NONE("none"), INFO("info"), ERROR("error");

	private String type;
	
	private PrintLevel(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return type;
	}
}
