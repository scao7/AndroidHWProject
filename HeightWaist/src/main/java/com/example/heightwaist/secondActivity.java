package com.example.heightwaist;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Picture;
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
    Button Otsu;
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
        Otsu = findViewById(R.id.Oust);

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

        Otsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent thirdPage = new Intent(getBaseContext(), thirdActivity.class);
                startActivity(thirdPage);

//                Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.pic1);
//                Bitmap newBm = converImage(bm);
//                mLineView.setImageBitmap(newBm);
            }
        });

        drawOnCanvas(pointA,pointB,true);

    }


    private void drawOnCanvas(PointF A, PointF B,boolean vertical){
        mLineView = findViewById(R.id.lineView);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            int resid = bundle.getInt("resId");
            mLineView.setImageResource(resid);
        }
        mLineView.setPointA(A);
        mLineView.setPaintB(B);
        mLineView.vertical = vertical;
        mLineView.draw();
    }




}
