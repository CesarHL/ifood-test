package com.helo.payload;

import java.io.Serializable;

public class Temperature implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String temp;
	
	private String temp_min;
	
	private String temp_max;

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public String getTemp_min() {
		return temp_min;
	}

	public void setTemp_min(String temp_min) {
		this.temp_min = temp_min;
	}

	public String getTemp_max() {
		return temp_max;
	}

	public void setTemp_max(String temp_max) {
		this.temp_max = temp_max;
	}
	
	

}
