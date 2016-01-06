package com.gabri.coach.application2;

import java.io.Serializable;

public class Point implements Serializable 
{

	
	private static final long serialVersionUID = 6646185026626272462L;
	
	float x, y;

	public Point(float x, float y) 
	{
		// TODO Auto-generated constructor stub
		this.x = x;
		this.y = y;
	}

	
	public float getX1()
	{
		return this.x;
	}
	public float getY1()
	{
		return this.y;
	}

}

