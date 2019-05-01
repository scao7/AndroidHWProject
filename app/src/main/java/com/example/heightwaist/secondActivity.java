package com.example.heightwaist;
import android.graphics.Bitmap;
import android.graphics.PointF;

import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class secondActivity extends AppCompatActivity {
    PointF pointA = new PointF(530, 40);
    PointF pointB = new PointF(530,1600);
    PointF pointC = new PointF(350,600);
    PointF pointD = new PointF(730,600);
    PointF pointE = new PointF(350,800);
    PointF pointF = new PointF(730,800);
//    TextView pixelInfo;
    boolean vertical = true;
    Button HeightBtn ;
    Button WaistBtn ;
    Button HipBtn ;
    Button Ostu;
    Bitmap image;

    //create a self define drawLine object
    private drawLine mLineView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        HeightBtn = findViewById(R.id.Height);
        WaistBtn = findViewById(R.id.Waist);
        HipBtn = findViewById(R.id.Hip);
        Ostu = findViewById(R.id.Oust);
//        final ImageView imageView = findViewById(R.id.imageView);
//        final ImageView bitmapImageView = findViewById(R.id.lineView);
//        Ostu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bitmapImageView.setImageResource(imageView2Bitmap(imageView));
//            }
//        });
        HeightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawOnCanvas(pointA,pointB,true);
            }
        });
        WaistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawOnCanvas(pointC,pointD,false);
            }
        });
        HipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawOnCanvas(pointE,pointF,false);
            }
        });
        drawOnCanvas(pointA,pointB,true);

    }

    private Bitmap  imageView2Bitmap(ImageView view){
        Bitmap bitmap = ((BitmapDrawable)view.getDrawable()).getBitmap();
        return bitmap;
    }

    private void drawOnCanvas(PointF A, PointF B,boolean vertical){
        mLineView = findViewById(R.id.lineView);
        mLineView.setPointA(A);
        mLineView.setPaintB(B);
        mLineView.vertical = vertical;
        mLineView.draw();
    }
    private void update(){

    }


}
