package com.example.heightwaist;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import static com.example.heightwaist.R.id.OriginalImage;


public class thirdActivity extends AppCompatActivity {

    Bitmap bi = null;

    boolean isColored;

    LinearLayout view;
    LinearLayout view_color;

    boolean flag;

    private int SIZE = 256;
    // Red, Green, Blue
    private int NUMBER_OF_COLOURS = 3;

    public final int RED = 0;
    public final int GREEN = 1;
    public final int BLUE = 2;

    private int[][] colourBins;
    private volatile boolean loaded = false;
    private int maxY;

    private static final int LDPI = 0;
    private static final int MDPI = 1;
    private static final int TVDPI = 2;
    private static final int HDPI = 3;
    private static final int XHDPI = 4;

    float offset = 1;

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
                    newBm = convertImage(bm);
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



}
