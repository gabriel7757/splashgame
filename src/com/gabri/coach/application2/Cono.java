package com.gabri.coach.application2;

import com.rodri.coach.application2.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

public class Cono extends View
{
	
	private Bitmap bmp;
	float x, y;
	int id, alto, ancho;

	public Cono(Context context, int id, float x, float y) 
	{
		super(context);
		// TODO Auto-generated constructor stub
		this.x = x;
		this.y = y;
		this.id = id;
		
		bmp = BitmapFactory.decodeResource(getResources(), R.drawable.cono);
		
		alto = bmp.getHeight();
		ancho = bmp.getWidth();

	}

	@Override
	protected void onDraw(Canvas canvas) 
	{

		canvas.drawBitmap(bmp, x - (ancho / 2), y - (alto / 2), null);

	}

	public int getAncho() 
	{
		// TODO Auto-generated method stub
		return ancho;
	}

	public int getAlto() 
	{
		// TODO Auto-generated method stub
		return alto;
	}

	public float getX() 
	{
		return x;
	}

	public void setX(float x) 
	{
		this.x = x;
	}

	public float getY() 
	{
		return y;
	}

	public void setY(float y) 
	{
		this.y = y;
	}

	public int getId() 
	{
		return id;

	}
}

