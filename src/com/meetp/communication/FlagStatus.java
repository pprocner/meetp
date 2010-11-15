package com.meetp.communication;

import java.io.Serializable;

public class FlagStatus implements Serializable{
	private static final long serialVersionUID = 1L;
	private boolean status; //Jeśli true, to można przejąć, jeśli false, to nie można.
	public void setStatus(boolean status){
		this.status = status;
	}
	public boolean getStatus(){
		return status;
	}
}
