package com.example.heightwaist;
import android.graphics.PointF;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class secondActivity extends AppCompatActivity {
    PointF pointA = new PointF(530, 40);
    PointF pointB = new PointF(530,1600);
    PointF pointC = new PointF(350,600);
    PointF pointD = new PointF(730,600);
    PointF pointE = new PointF(350,800);
    PointF pointF = new PointF(730,800);
    int switchCase = 0;
    Button HeightBtn ;
    Button WaistBtn ;
    Button HipBtn ;

    private drawLine mLineView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        HeightBtn = findViewById(R.id.Height);
        WaistBtn = findViewById(R.id.Waist);
        HipBtn = findViewById(R.id.Hip);
        HeightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawOnCanvas(pointA,pointB);
            }
        });
        WaistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawOnCanvas(pointC,pointD);
            }
        });
        HipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawOnCanvas(pointE,pointF);
            }
        });
        drawOnCanvas(pointA,pointB);

    }

    private void drawOnCanvas(PointF A, PointF B){
        mLineView = findViewById(R.id.lineView);
//        mLineView.setPointA(pointA);
//        mLineView.setPaintB(pointB);
        mLineView.setPointA(A);
        mLineView.setPaintB(B);
        mLineView.draw();
    }
    private void update(){
        HeightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pointA.x = 250;
                pointA.y = 100;
                pointB.x = 250;
                pointB.y = 400;

            }
        });

        WaistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pointA.x = 250;
                pointA.y = 100;
                pointB.x = 250;
                pointB.y = 400;
            }
        });

        WaistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pointA.x = 250;
                pointA.y = 100;
                pointB.x = 250;
                pointB.y = 400;
            }
        });
    }


}
