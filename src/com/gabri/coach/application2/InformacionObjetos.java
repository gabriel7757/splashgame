package com.gabri.coach.application2;

import java.io.Serializable;
import java.util.ArrayList;

public class InformacionObjetos implements Serializable
{

	private static final long serialVersionUID = -2248585622198456477L;

	
	int tipo, id;
	String colorLinea;
	float x, y;
	ArrayList<Point> points;
	
	public InformacionObjetos(int tipo,int id, float x, float y)
	{
		
	this.tipo = tipo;
	this.id = id;
	this.x = x;
	this.y = y;

	}
	
	InformacionObjetos(ArrayList<Point> points, String string)
	{
		this.points = points;
		this.colorLinea = string;
	}


	public String getColor() 
	{
		return colorLinea;
	}

	public ArrayList<Point> getPoints() 
	{
		return points;
	}

	public int getTipo() 
	{
		return tipo;
	}


	public int getId() 
	{
		return id;
	}


	public float getX() 
	{
		return x;
	}


	public float getY() 
	{
		return y;
	}
	
	
}
