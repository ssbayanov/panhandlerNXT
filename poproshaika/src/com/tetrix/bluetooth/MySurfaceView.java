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


class MySurfaceView extends SurfaceView implements Runnable{
    
    Thread thread = null;
    SurfaceHolder surfaceHolder;
    volatile boolean running = false;
     
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Random random;
 
  public MySurfaceView(Context context) {
   super(context);
   // TODO Auto-generated constructor stub
   surfaceHolder = getHolder();
   random = new Random();
  }
   
  public void onResumeMySurfaceView(){
	  clearAnimation();
   running = true;
   thread = new Thread(this);
   thread.start();
 }
   
  public void onPauseMySurfaceView(){
   boolean retry = true;
   running = false;
   while(retry){
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
    if(surfaceHolder.getSurface().isValid()){
     Canvas canvas = surfaceHolder.lockCanvas();
     //... actual drawing on canvas
     
     paint.setStyle(Paint.Style.STROKE);
     paint.setStrokeWidth(2);
     //this.set;
     canvas.drawColor (0xff000000);
     
     int w = canvas.getWidth();
     int h = canvas.getHeight();
     int x = random.nextInt(w-1); 
     int y = random.nextInt(h-1);
     /*int r = random.nextInt(255);
     int g = random.nextInt(255);
     int b = random.nextInt(255);*/
     paint.setColor(0xff00ff00);
     //int[] f = new int[32];
     int p = w/32;
     canvas.drawPoint(x, 0, paint);
     int lasty = y;
     for(int i = 0; i < w; i+=p ){
    	 y = random.nextInt(h-1); 
    	 canvas.drawLine(i-p, lasty, i, y, paint);
    	 Log.d("draw","dot ["+i+":"+y+"]");
    	 lasty = y;
     }
     
      
     surfaceHolder.unlockCanvasAndPost(canvas);
     
    }
   }
  }
 