package com.helo.payload;

import java.io.Serializable;
import java.util.List;

public class Track implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public List<String> tacks;

	public List<String> getTacks() {
		return tacks;
	}

	public void setTacks(List<String> tacks) {
		this.tacks = tacks;
	}

}
