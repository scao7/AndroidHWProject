package com.example.heightwaist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class drawLine extends ImageView {

    private Paint paint = new Paint();
    private PointF pointA,pointB;
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
