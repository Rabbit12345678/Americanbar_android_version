package com.spaculus.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CircularImageView extends ImageView {

	private int borderWidth = 3;
	private int viewWidth;
	private int viewHeight;
	private Bitmap image;
	private Paint paint;
	private Paint paintBorder;
	private BitmapShader shader;
	
	public CircularImageView(Context context) {
		super(context);
		setup();
	}

	public CircularImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setup();
	}

	public CircularImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setup();
	}
	
	private void setup()
	{
		try {
			// init paint
			paint = new Paint();
			paint.setAntiAlias(true);
			
			paintBorder = new Paint();
			setBorderColor(Color.WHITE);
			//setBorderColor(Color.parseColor("#68AEE0"));
			paintBorder.setAntiAlias(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void setBorderWidth(int borderWidth)
	{
		try {
			this.borderWidth = borderWidth;
			this.invalidate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setBorderColor(int borderColor)
	{		
		try {
			if(paintBorder != null)
				paintBorder.setColor(borderColor);
			
			this.invalidate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadBitmap()
	{
		try {
			BitmapDrawable bitmapDrawable = (BitmapDrawable) this.getDrawable();
			
			if(bitmapDrawable != null)
				image = bitmapDrawable.getBitmap();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressLint("DrawAllocation")
	@Override
    public void onDraw(Canvas canvas)
	{
		try {
			//load the bitmap
			loadBitmap();
						
			// init shader
			if(image !=null)
			{			
				shader = new BitmapShader(Bitmap.createScaledBitmap(image, canvas.getWidth(), canvas.getHeight(), false), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
				paint.setShader(shader);
				int circleCenter = viewWidth / 2;

				// circleCenter is the x or y of the view's center
				// radius is the radius in pixels of the cirle to be drawn
				// paint contains the shader that will texture the shape
				canvas.drawCircle(circleCenter + borderWidth, circleCenter + borderWidth, circleCenter + borderWidth, paintBorder);
				canvas.drawCircle(circleCenter + borderWidth, circleCenter + borderWidth, circleCenter, paint);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
    	try {
			int width = measureWidth(widthMeasureSpec);
			int height = measureHeight(heightMeasureSpec, widthMeasureSpec);    	
			
			viewWidth = width - (borderWidth *2);
			viewHeight = height - (borderWidth*2);
			
			setMeasuredDimension(width, height);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private int measureWidth(int measureSpec)
	{
	        int result =0;
			try {
				int specMode = MeasureSpec.getMode(measureSpec);
				int specSize = MeasureSpec.getSize(measureSpec);

				if (specMode == MeasureSpec.EXACTLY) {
				    // We were told how big to be
				    result = specSize;
				} else {
				    // Measure the text
				    result = viewWidth;
				    
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		return result;
	}
	
	private int measureHeight(int measureSpecHeight, int measureSpecWidth) {
        int result = 0;
        try {
			int specMode = MeasureSpec.getMode(measureSpecHeight);
			int specSize = MeasureSpec.getSize(measureSpecHeight);

			if (specMode == MeasureSpec.EXACTLY) {
			    // We were told how big to be
			    result = specSize;
			} else {
			    // Measure the text (beware: ascent is a negative number)
			    result = viewHeight;           
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return result;
    }
}
