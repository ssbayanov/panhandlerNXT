package com.tetrix.bluetooth;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
//import android.graphics.Color;
import android.graphics.Paint;
//import android.graphics.RectF;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.util.Log;


class MySurfaceView extends SurfaceView implements Runnable {

	Thread thread = null;
	SurfaceHolder surfaceHolder;
	volatile boolean running = false;

	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	Random random;
	private byte[] mess;

	public MySurfaceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		surfaceHolder = getHolder();
		random = new Random();
	}

	public void onResumeMySurfaceView(byte[] msg) {
		mess = msg;
		clearAnimation();
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	public void onPauseMySurfaceView() {
		boolean retry = true;
		running = false;
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void run() {
		// TODO Auto-generated method stub
		if (surfaceHolder.getSurface().isValid()) {
			Canvas canvas = surfaceHolder.lockCanvas();

			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(2);
			// this.set;
			canvas.drawColor(0xff000000);

			int w = canvas.getWidth();
			int h = canvas.getHeight();
			float c = h/255; 
			paint.setColor(0xff00ff00);
			int p = w / mess.length;
			
			canvas.drawPoint(0, mess[0], paint);
			for (int i = 1; i < w; i += p) {
					canvas.drawLine(i - p, mess[i-1]*c, i, mess[i]*c, paint);
				Log.d("draw", "dot [" + i + ":" + mess[i] + "]");
			}

			surfaceHolder.unlockCanvasAndPost(canvas);

		}
	}
}
