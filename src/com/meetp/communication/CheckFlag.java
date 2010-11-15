package com.meetp.communication;

import java.io.Serializable;

public class CheckFlag implements Serializable{
	private static final long serialVersionUID = 1L;
	private long FlagID;
	public void setFlagID(long fid){
		this.FlagID = fid;
	}
	public long getFlagID(){
		return FlagID;
	}	
}
