package com.gabri.coach.application2;


import java.util.ArrayList;

import com.gabri.coach.application2.Point;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

public class LineDrawing extends View
{
	

	ArrayList<Point> points = null;
	
	int color[];
	String col;
	
	public LineDrawing(Context context, ArrayList<Point> lineaPuntos, String color) 
	{
		super(context);
		// TODO Auto-generated constructor stub
		this.col = color;
		this.color = getRGB(col);
		points = lineaPuntos;
	}

	@SuppressLint("DrawAllocation")
	protected void onDraw(android.graphics.Canvas canvas) 
	{
		
		Paint paint = new Paint(); 
		paint.setARGB(255,color[0],color[1],color[2]);
		paint.setStrokeWidth(4);
		
		for(int x = 0;x<points.size()-1;x++)
		{
	        canvas.drawLine(points.get(x).getX1(), points.get(x).getY1(), points.get(x+1).getX1(), points.get(x+1).getY1(), paint);
		}
		
	};
	

	private int[] getRGB(String rgb)
	{
	    int[] val = new int[3];
	    
	    for(int i=0; i<3; i++)
	    {
	    	val[i] = Integer.valueOf(rgb.substring(i*2, i*2+2),16).intValue();
	    }
	    return val;
	}

	public String getColor() 
	{
		// TODO Auto-generated method stub
		return col;
	}

	public ArrayList<Point> getPoints() 
	{
		return points;
	}
	
	
}