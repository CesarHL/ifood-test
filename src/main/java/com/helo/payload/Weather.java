package com.helo.payload;

import java.io.Serializable;

public class Weather implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;

	private String temp;

	private Temperature main;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public Temperature getMain() {
		return main;
	}

	public void setMain(Temperature main) {
		this.main = main;
	}

}
