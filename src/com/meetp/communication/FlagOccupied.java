package com.meetp.communication;

import java.io.Serializable;

public class FlagOccupied implements Serializable{

	private static final long serialVersionUID = 1L;
	private String status;
	private int time;
	
	public String getStatus(){
		return status;
	}
	public int getTime(){
		return time;
	}
	public void setStatus(String status){
		this.status = status;
	}
	public void setTime(int time){
		this.time = time;
	}
}
