package com.example.test2;

/**
 * Created by guswk on 2016-02-13.
 */

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends Activity implements SurfaceHolder.Callback {
    @SuppressWarnings("deprecation")
    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Button button;
    String str;

    @SuppressWarnings("deprecation")
    Camera.PictureCallback jpegCallback;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_main);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, jpegCallback);
            }
        });

        getWindow().setFormat(PixelFormat.UNKNOWN);


        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        jpegCallback = new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                str = savePicture(data);
                refreshCamera();

                Intent intent = new Intent(CameraActivity.this, ResultActivity.class);
                intent.putExtra("strParamName", str);
                startActivity(intent);
            }
        };
    }


    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            return;
        }

        try {
            camera.stopPreview();
        } catch (Exception e) {
        }

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @SuppressWarnings("deprecation")
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        camera = Camera.open();
        camera.stopPreview();
        Camera.Parameters param = camera.getParameters();
        camera.setDisplayOrientation(90);
        camera.setParameters(param);

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
            System.err.println(e);
            return;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        refreshCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    public String savePicture(byte[] data) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String date = dateFormat.format(new Date());
        String photoFile = "Picture_" + date + ".jpg";

        File sdDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/Julda");
        if (!sdDir.exists()) {
            sdDir.mkdir();
        }

        String filename = sdDir + File.separator + photoFile;

        File pictureFile = new File(filename);

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
            Context context = getApplicationContext();
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(pictureFile)));
            Toast.makeText(this, "New Image saved:" + photoFile, Toast.LENGTH_LONG).show();

            return filename;
        } catch (Exception error) {
            Log.d("File not saved: ", error.getMessage());
            Toast.makeText(this, "Image could not be saved.", Toast.LENGTH_LONG).show();
            return null;
        }
    }
}