package com.example.heightwaist;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
//    TextView pixelInfo;
    boolean vertical = true;
    Button HeightBtn ;
    Button WaistBtn ;
    Button HipBtn ;
    Button Ostu;
    PointF[] height;

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

        HeightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.sample);
                height = ostuCalculateHeight(bm);
                drawOnCanvas(height[0],height[1],true);
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

        Ostu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent thirdPage = new Intent(getBaseContext(), thirdActivity.class);
                startActivity(thirdPage);
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

    private PointF[] ostuCalculateHeight(Bitmap original){
        Bitmap BWimg = Bitmap.createBitmap(original.getWidth(), original.getHeight(), original.getConfig());

        int width = original.getWidth();
        int height = original.getHeight();
        int A, R, G, B, colorPixel;

        double Wcv = 0, th = 0;
        int[] tPXL = new int[256];
        int[][] pxl = new int[width][height];
        double Bw, Bm, Bv, Fw, Fm, Fv;
        int np, ImgPix = 0, fth = 0;

        // pixel check for histogram //
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                colorPixel = original.getPixel(x, y);

                A = Color.alpha(colorPixel);
                R = Color.red(colorPixel);
                G = Color.green(colorPixel);
                B = Color.blue(colorPixel);

                int gray = (int) ( (0.2126 * R) + (0.7152 * G) + (0.0722 * B) ); // (int) ( (0.299 * R) + (0.587 * G) + (0.114 * B) );
                pxl[x][y] = gray;
                tPXL[gray] = tPXL[gray] + 1;
                ImgPix = ImgPix + 1;
            }
        }

        // ----- histo-variance ----- //
        for (int t = 0; t < 256; t++){
            Bw = 0; Bm = 0; Bv = 0;
            Fw = 0; Fm = 0; Fv = 0;
            np = 0;

            if (t == 0){ // all white/foreground as t0 ----- //
                Fw = 1;

                for (int d = 0; d < 256; d++) { //mean
                    Fm = Fm + (d * tPXL[d]);
                }
                Fm = Fm / ImgPix;

                for (int e = 0; e < 256; e++) { //variance
                    Fv = Fv + (Math.pow((e - Fm), 2) * tPXL[e]);
                }
                Fv = Fv / ImgPix;

            }

            else { // main thresholding
                for (int d = 0; d < (t-1); d++){ // BG weight & mean + BG pixel
                    Bw = Bw + tPXL[d];
                    Bm = Bm + (d * tPXL[d]);
                    np = np + tPXL[d];
                }
                Bw = Bw / ImgPix;
                Bm = Bm / np;

                for (int e = 0; e < (t-1); e++) { //BG variance
                    Bv = Bv + (Math.pow((e - Bm), 2) * tPXL[e]);
                }
                Bv = Bv / np;

                for (int j = t; j < 256; j++) { // FG weight & mean + BG pixel
                    Fw = Fw + tPXL[j];
                    Fm = Fm + (j * tPXL[j]);
                    np = ImgPix - np;
                }
                Fw = Fw / ImgPix;
                Fm = Fm / np;

                for (int k = t; k < 256; k++) { //FG variance
                    Fv = Fv + (Math.pow((k - Fm), 2) * tPXL[k]);
                }
                Fv = Fv / np;

            }

            // within class variance
            Wcv = (Bw * Bv) + (Fw * Fv);

            if (t == 0){
                th = Wcv;
            }
            else if (Wcv < th){
                th = Wcv;
                fth = t;
            }
        }

        // set binarize pixel
        float top = Float.POSITIVE_INFINITY;
        float bottom = Float.NEGATIVE_INFINITY;
        System.out.println("width is " + width);
        System.out.println("height is " + height);
        for (int x = 0; x < width; x++) {
             for (int y = 0; y < height; y++) {

                int fnpx = pxl[x][y];
                colorPixel = original.getPixel(x, y);

                A = Color.alpha(colorPixel);
                if (fnpx < fth) { //R > fth

                    if(top > y ){
                        top = y;
                        System.out.println("top is" + top);
                    }
                    if(bottom < y){
                        bottom = y;
                    }
                }
            }
        }
        System.out.println(bottom - top);
        PointF[] points = {new PointF(530,top), new PointF(530,bottom) };

        return points;
    }
}
