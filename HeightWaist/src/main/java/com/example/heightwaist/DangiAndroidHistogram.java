package com.example.heightwaist;

import java.io.IOException;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class DangiAndroidHistogram extends Activity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_dangi_android_histogram);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        if(metrics.densityDpi==metrics.DENSITY_LOW)
            offset = 0.75f;
        else if(metrics.densityDpi==metrics.DENSITY_MEDIUM)
            offset = 1f;
        else if(metrics.densityDpi==metrics.DENSITY_TV)
            offset = 1.33f;
        else if(metrics.densityDpi==metrics.DENSITY_HIGH)
            offset = 1.5f;
        else if(metrics.densityDpi==metrics.DENSITY_XHIGH)
            offset = 2f;

        Log.e("NIRAV",""+offset);

        colourBins = new int[NUMBER_OF_COLOURS][];

        for (int i = 0; i < NUMBER_OF_COLOURS; i++) {
            colourBins[i] = new int[SIZE];
        }

        loaded = false;

        Button upload = (Button) findViewById(R.id.upload);
        upload.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (flag) {
                    view_color.removeAllViews();
                    view.removeAllViews();
                }
                Intent it = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(it, 101);

                flag = true;

                /*
                 * LinearLayout view = (LinearLayout) findViewById(R.id.lyt);
                 * view.addView(new MyHistogram(getApplicationContext()));
                 */
            }
        });

        Button histogram = (Button) findViewById(R.id.hst_btn);
        histogram.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (bi != null) {
                    isColored = false;
                    view = (LinearLayout) findViewById(R.id.lyt);
                    view.addView(new MyHistogram(getApplicationContext(), bi));
                }
            }
        });
        Button histogram_color = (Button) findViewById(R.id.hst_color_btn);
        histogram_color.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (bi != null) {
                    isColored = true;
                    view_color = (LinearLayout) findViewById(R.id.lyt_color);
                    view_color.addView(new MyHistogram(getApplicationContext(),
                            bi));
                }
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {

            case 101:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    String filename = getRealPathFromURI(selectedImage);
                    bi = BitmapFactory.decodeFile(filename);
                /*
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                bi.compress(Bitmap.CompressFormat.JPEG,10,out);
                bi = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));*/

                    if(bi!=null)
                    {
                        try {
                            new MyAsync().execute();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        Log.e("TEST", "GetRealPath : " + contentUri);

        try {
            if (contentUri.toString().contains("video")) {
                String[] proj = { MediaStore.Video.Media.DATA };
                Cursor cursor = managedQuery(contentUri, proj, null, null, null);
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            } else {
                String[] proj = { MediaStore.Images.Media.DATA };
                Cursor cursor = managedQuery(contentUri, proj, null, null, null);
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            }
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    class MyAsync extends AsyncTask
    {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(0);
        }

        @Override
        protected Object doInBackground(Object... params) {
            // TODO Auto-generated method stub

            try {
                load(bi);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            ImageView img = (ImageView) findViewById(R.id.img);
            img.setImageBitmap(bi);

            ((Button) findViewById(R.id.hst_btn)).setVisibility(View.VISIBLE);
            ((Button) findViewById(R.id.hst_color_btn)).setVisibility(View.VISIBLE);

            dismissDialog(0);
        }

    }

    public void load(Bitmap bi) throws IOException {

        if (bi != null) {
            // Reset all the bins
            for (int i = 0; i < NUMBER_OF_COLOURS; i++) {
                for (int j = 0; j < SIZE; j++) {
                    colourBins[i][j] = 0;
                }
            }

            for (int x = 0; x < bi.getWidth(); x++) {
                for (int y = 0; y < bi.getHeight(); y++) {

                    int pixel = bi.getPixel(x, y);

                    colourBins[RED][Color.red(pixel)]++;
                    colourBins[GREEN][Color.green(pixel)]++;
                    colourBins[BLUE][Color.blue(pixel)]++;
                }
            }

            maxY = 0;

            for (int i = 0; i < NUMBER_OF_COLOURS; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (maxY < colourBins[i][j]) {
                        maxY = colourBins[i][j];
                    }
                }
            }
            loaded = true;
        } else {
            loaded = false;
        }
    }

    class MyHistogram extends View {

        public MyHistogram(Context context, Bitmap bi) {
            super(context);

        }

        @Override
        protected void onDraw(Canvas canvas) {
            // TODO Auto-generated method stub
            super.onDraw(canvas);

            if (loaded) {
                canvas.drawColor(Color.GRAY);

                Log.e("NIRAV", "Height : " + getHeight() + ", Width : "
                        + getWidth());

                int xInterval = (int) ((double) getWidth() / ((double) SIZE + 1));

                for (int i = 0; i < NUMBER_OF_COLOURS; i++) {

                    Paint wallpaint;

                    wallpaint = new Paint();
                    if (isColored) {
                        if (i == RED) {
                            wallpaint.setColor(Color.RED);
                        } else if (i == GREEN) {
                            wallpaint.setColor(Color.GREEN);
                        } else if (i == BLUE) {
                            wallpaint.setColor(Color.BLUE);
                        }
                    } else {
                        wallpaint.setColor(Color.WHITE);
                    }

                    wallpaint.setStyle(Style.FILL);

                    Path wallpath = new Path();
                    wallpath.reset();
                    wallpath.moveTo(0, getHeight());
                    for (int j = 0; j < SIZE - 1; j++) {
                        int value = (int) (((double) colourBins[i][j] / (double) maxY) * (getHeight()+100));


                        //if(j==0) {
                        //   wallpath.moveTo(j * xInterval* offset, getHeight() - value);
                        //}
                        // else {
                        wallpath.lineTo(j * xInterval * offset, getHeight() - value);
                        // }
                    }
                    wallpath.lineTo(SIZE * offset, getHeight());
                    canvas.drawPath(wallpath, wallpaint);
                }

            }

        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        ProgressDialog dataLoadProgress = new ProgressDialog(this);
        dataLoadProgress.setMessage("Loading...");
        dataLoadProgress.setIndeterminate(true);
        dataLoadProgress.setCancelable(false);
        dataLoadProgress.setProgressStyle(android.R.attr.progressBarStyleLarge);
        return dataLoadProgress;

    }
}