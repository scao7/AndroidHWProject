package com.example.heightwaist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class drawLine extends android.support.v7.widget.AppCompatImageView {

    private Paint paint = new Paint();
    private PointF pointA,pointB;
    private  String pixelInfo;
    public boolean vertical;
    public drawLine(Context context) {
        super(context);
    }

    public drawLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public drawLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);
        canvas.drawLine(pointA.x, pointA.y,pointB.x,pointB.y,paint);
        paint.setColor(Color.GREEN);
        paint.setTextSize(40);


        if(vertical){
            pixelInfo = Math.abs(pointB.y - pointA.y)+"pixel" ;
            canvas.drawText(pixelInfo, pointA.x, (pointA.y + pointB.y)/2, paint);
        }
        else {
            pixelInfo = Math.abs(pointB.x - pointA.y ) + "pixel";
            canvas.drawText(pixelInfo, (pointA.x + pointB.x)/2, pointA.y , paint);
        }


    }

    public void setPointA(PointF point){
           pointA =  point;
    }
    public void setPaintB(PointF point){
        pointB = point;
    }

    public void draw(){
        invalidate();
        requestLayout();
    }
}
