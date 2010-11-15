package com.meetp.communication;

import java.io.Serializable;

public class Balance implements Serializable {
	private static final long serialVersionUID = 3220369338277023658L;
	private int teamOneBalance;
	
	public int getTeamOneBalance(){
		return teamOneBalance;
	}
	
	public void setTeamOneBalance(int b){
		teamOneBalance = b;
	}
}
