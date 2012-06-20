package com.masonware.soggy;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RemoteViews.RemoteView;

import com.masonware.soggy.settings.Settings;

@RemoteView
public class ClockView extends View {
	
	Context context;
	private static final float SOGGY_DEGREES_OFFSET = 28.1f;
	
	String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"}; 
	Paint textPaint, facePaint, handPaint;
	String type;
	
	public ClockView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		
		textPaint = new Paint();
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(40);
		
		facePaint = new Paint();
		facePaint.setColor(Color.WHITE);
		facePaint.setStrokeWidth(2);
		facePaint.setStyle(Paint.Style.STROKE);
		
		handPaint = new Paint();
		handPaint.setColor(Color.GREEN);
		handPaint.setStrokeWidth(5);
		handPaint.setStyle(Paint.Style.STROKE);
		
		type = Settings.getString(Settings.KEY_CLOCK_TYPE, "soggy");
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (type == "original") {
			drawOriginal(canvas);
		} else if (type == "soggy") {
			drawSoggy(canvas);
		}
	}

	private double getHandRadians() {
		Calendar c = Calendar.getInstance();
		int totalMins = 7 * 24 * 60;
		int minsSoFar = ((c.get(Calendar.DAY_OF_WEEK)-1) * 24 + c.get(Calendar.HOUR_OF_DAY)) * 60 + c.get(Calendar.MINUTE);
		double radians = ((double)minsSoFar * 2) * Math.PI / totalMins;
		return radians;
	}
	
	private void drawOriginal(Canvas canvas) {
		float center = getWidth()/2;
		float radius = center*5/6;
		float circumference = (float) (Math.PI * radius * 2);
		
		canvas.rotate(-90, center, center);
		
		Path circle = new Path();
		circle.addCircle(center, center, radius, Direction.CW);

		canvas.drawArc(new RectF(center-radius, center-radius, center+radius, center+radius), 0, 360, false, facePaint);
		for(int i=0; i<days.length; i++) {
			double radians = ((double)i * 2) * Math.PI / 7.0;
			canvas.drawLine(center + (float)Math.cos(radians) * radius * 0.9f, 
							center + (float)Math.sin(radians) * radius * 0.9f, 
							center + (float)Math.cos(radians) * radius, 
							center + (float)Math.sin(radians) * radius, 
							facePaint);
			canvas.drawTextOnPath(days[i], circle, i * (circumference/7), -10, textPaint);
		}
		
		double handRadians = getHandRadians();
		canvas.drawLine(center, 
						center, 
						center + (float)Math.cos(handRadians) * radius * 0.9f, 
						center + (float)Math.sin(handRadians) * radius * 0.9f, 
						handPaint);
		
		canvas.rotate(90, center, center);
	}

	private void drawSoggy(Canvas canvas) {
		int width = getWidth();
		Bitmap face = BitmapFactory.decodeResource(context.getResources(), R.drawable.face);
		canvas.drawBitmap(face, 
						  new Rect(0, 0, face.getWidth(), face.getHeight()), 
						  new Rect(0, 0, width, width),
						  new Paint());


		float handDegrees = (float) Math.toDegrees(getHandRadians()) - SOGGY_DEGREES_OFFSET;
		canvas.rotate(handDegrees, width/2, width/2);
		Bitmap hand = BitmapFactory.decodeResource(context.getResources(), R.drawable.hand);
		canvas.drawBitmap(hand, width/2 - hand.getWidth()/2, width/2 + hand.getWidth()/2 - hand.getHeight(), new Paint());
		canvas.rotate(-handDegrees, width/2, width/2);
	}

}
