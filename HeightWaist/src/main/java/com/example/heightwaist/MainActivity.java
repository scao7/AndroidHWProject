package com.example.heightwaist;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.PointF;
import android.media.Image;
import android.net.Uri;
import android.os.Build;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class MainActivity extends AppCompatActivity {

   ImageView imageView;
   TextView txtResult;
   Button runBtn;
   Button openBtn;
   Button nextBtn;
   Uri imageUri;
   private int current_image;
   int[] images = {R.drawable.sample,
                   R.drawable.pic1,
                   R.drawable.pic2,
                   R.drawable.pic3,
                   R.drawable.pic4,
                   R.drawable.pic5,
                   R.drawable.pic6,
                   };


    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        runBtn = findViewById(R.id.Run);
        openBtn = findViewById(R.id.open_picture);
        nextBtn = findViewById(R.id.next);
        txtResult = findViewById(R.id.txtResult);

//         scan qr code
        runBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //try to pass image information to second activity
                Intent secondPage = new Intent(getBaseContext(), secondActivity.class);
                startActivity(secondPage);
                //send current image to second activity
                Intent i = new Intent(MainActivity.this, secondActivity.class);
                i.putExtra("resId",images[current_image]);
                startActivity(i);

            }
        });

        // currently it read a bitmap
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_image++;
                current_image = current_image % images.length;
                imageView.setImageResource(images[current_image]);
            }
        });

        //open open camera roll
        openBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check runtime permission
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission((Manifest.permission.READ_EXTERNAL_STORAGE))
                            == PackageManager.PERMISSION_DENIED){
                            //permission not granted request
                            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                            //show popup for runtime permission
                            requestPermissions(permissions, PERMISSION_CODE);
                    }
                    else{
                        //permission already granted
                        pickImageFromGallery();
                    }
                }
                else{
                    //system os is less then marshmallow
                    pickImageFromGallery();
                }
            }
        });
    }

    private void pickImageFromGallery(){
        //intent to pick image
        Intent intent = new Intent((Intent.ACTION_PICK));
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    // handle result of runtime permission

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE: {
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // permission granted
                    pickImageFromGallery();
                }
                else{
                    // permission denied
                    Toast.makeText(this,"Permission denied!",Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){
            imageView.setImageURI(data.getData());


        }
    }






}
