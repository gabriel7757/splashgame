package com.gabri.coach.application2;

import com.rodri.coach.application2.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

public class Jugador2Drawing extends View 
{

	static final int colorL = 1;
	static final int colorV = 2;
	private Bitmap bmp;
	float x, y;
	int id, color;
	int anchoCamiseta, altoCamiseta;
	String nombre;
	float tam;
	
	public Jugador2Drawing(Context context, int id, float x, float y) 
	{
		super(context);
		this.x = x;
		this.y = y;
		this.id = id;
		//this.color = color;
		//this.nombre = nombre;
		// DEPENDE DEL COLOR CHAPA AZUL/ROJA
		//if (color == colorL) 
		
		bmp = BitmapFactory.decodeResource(getResources(),
					R.drawable.jugador2);
		
	
		anchoCamiseta = bmp.getWidth();
		altoCamiseta = bmp.getHeight();
		
		/*if(nombre.length() < 3)
		{
			tam = 2;
		}
		else
		{
			tam = (float) (6.0 / nombre.length());
		}*/

	}

	public int getAnchoCamiseta()
	{
		return anchoCamiseta;
	}
	
	public int getAltoCamiseta()
	{
		return altoCamiseta;
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
	
	/*public String getNombre() 
	{
		return nombre;

	}*/

	@Override
	protected void onDraw(Canvas canvas) 
	{

		canvas.drawBitmap(bmp, x - (anchoCamiseta / 2), y - (altoCamiseta / 2),null);

		/*Paint paint = new Paint();
		paint.setTextAlign(Align.CENTER);
		paint.setColor(Color.YELLOW);
		paint.setTextScaleX(tam);
		canvas.drawText(nombre, x, y + (anchoCamiseta / 10), paint);*/

	}


}
