package com.example.heightwaist;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import static com.example.heightwaist.R.id.OriginalImage;


public class thirdActivity extends AppCompatActivity {
    ImageView imageView;
    Button LoadImage,ApplyChange,JumpToHisto, Histo;
    Bitmap newBm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        imageView = findViewById(OriginalImage);
        LoadImage = findViewById(R.id.LoadImage);
        ApplyChange = findViewById(R.id.Apply);
        JumpToHisto = findViewById(R.id.Demo);
        Histo = findViewById(R.id.Histogram);
        final Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.sample);

        LoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newBm == null )
                    newBm = ostuConvert(bm);
                imageView.setImageBitmap(bm);
            }
        });

        ApplyChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageBitmap(newBm);
            }
        });

        JumpToHisto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextPage = new Intent(getBaseContext(), DangiAndroidHistogram.class);
                startActivity(nextPage);
            }
        });

        Histo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Histo.setText("not implement");
            }
        });
    }

        public  static Bitmap convertImage(Bitmap original){
        Bitmap finalImage = Bitmap.createBitmap(original.getWidth(), original.getHeight(),original.getConfig());
        int colorPixel;
        int A,R,G,B;
        int width = original.getWidth();
        int height = original.getHeight();
        for(int x = 0; x < width; x++ ){
            for(int y = 0 ; y < height; y++){
                colorPixel = original.getPixel(x,y);
                A = Color.alpha(colorPixel);
                R = Color.red(colorPixel);
                G = Color.green(colorPixel);
                B = Color.blue(colorPixel);

                if(R > 100 ){
                    R = 255;
                }
                else{
                    R = 0;
                }
                G= R;
                B= R;
                finalImage.setPixel(x,y,Color.argb(A,R,G,B));
            }
        }

        return finalImage;
    }

    public static Bitmap ostuConvert(Bitmap original){
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
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                int fnpx = pxl[x][y];
                colorPixel = original.getPixel(x, y);

                A = Color.alpha(colorPixel);

                if (fnpx > fth) { //R > fth
                    fnpx = 255;
                    BWimg.setPixel(x, y, Color.argb(A, fnpx, fnpx, fnpx));
                }
                else {
                    fnpx = 0;
                    BWimg.setPixel(x, y, Color.argb(A, fnpx, fnpx, fnpx));
                }
            }
        }
        return BWimg;
    }

}
