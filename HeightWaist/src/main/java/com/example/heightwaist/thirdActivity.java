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

public class thirdActivity extends AppCompatActivity {

    ImageView imageView;
    Button LoadImage,ApplyChange,JumpToHisto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        imageView = findViewById(R.id.OriginalImage);
        LoadImage = findViewById(R.id.LoadImage);
        ApplyChange = findViewById(R.id.Apply);
        JumpToHisto = findViewById(R.id.Histogram);

        final Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.pic1);
        LoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageBitmap(bm);
            }
        });

        ApplyChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap newBm = converImage(bm);
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


    }



    public  static Bitmap converImage(Bitmap original){
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
