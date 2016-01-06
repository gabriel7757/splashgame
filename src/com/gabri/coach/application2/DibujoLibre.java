package com.gabri.coach.application2;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class DibujoLibre extends DrawableObject 
{

	private Path mPath;
	private Paint mPaint;
	
	public DibujoLibre(Path path, Paint paint) 
	{
		mPath = path;
		mPaint = paint;
	}
	
	@Override
	public void draw(Canvas canvas) 
	{
		canvas.drawPath(mPath, mPaint);
	}
}