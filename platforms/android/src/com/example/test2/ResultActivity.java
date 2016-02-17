package com.example.test2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by guswk on 2016-02-11.
 */
public class ResultActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        Context context = getApplicationContext();


        Intent intent = getIntent();
        String photoPath = intent.getStringExtra("strParamName");

        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        int displayWidth = display.getWidth();
        int displayHeight = display.getHeight();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, options);

        float widthScale = options.outWidth/displayWidth;
        float heightScale = options.outHeight/displayHeight;
        float scale = widthScale > heightScale ? widthScale : heightScale;

        if(scale >= 8){
            options.inSampleSize = 8;
        }else if(scale >= 6){
            options.inSampleSize = 6;
        }else if(scale >= 4){
            options.inSampleSize = 4;
        }else if(scale >= 2){
            options.inSampleSize = 2;
        } else {
            options.inSampleSize = 1;
        }

        options.inJustDecodeBounds = false;

        final Bitmap bmp = BitmapFactory.decodeFile(photoPath, options);

        ImageView img = (ImageView) findViewById(R.id.imageView1);
        img.setImageBitmap(bmp);
    }
}
