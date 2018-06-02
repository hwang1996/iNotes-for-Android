package com.example.inotes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.VideoView;

/**
 * Created by 王浩啊 on 2016/9/16.
 */
public class details extends Activity {

    private ImageView image_detail;
    private VideoView s_video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.detail);

        image_detail = (ImageView) findViewById(R.id.image_detail);
        s_video = (VideoView) findViewById(R.id.s_video);

        Intent intent = getIntent();
        String flag = intent.getStringExtra("flag");

        if (flag.equals("1")){
            String image1 = intent.getStringExtra("image1");
            Bitmap bitmap = BitmapFactory.decodeFile(image1);
            image_detail.setVisibility(View.VISIBLE );
            s_video.setVisibility(View.GONE);
            image_detail.setImageBitmap(bitmap);
        }
        if (flag.equals("2")){
            String image2 = intent.getStringExtra("image2");
            Bitmap bitmap = BitmapFactory.decodeFile(image2);
            image_detail.setVisibility(View.VISIBLE);
            s_video.setVisibility(View.GONE);
            image_detail.setImageBitmap(bitmap);
        }
        if (flag.equals("3")){
            String image3 = intent.getStringExtra("image3");
            Bitmap bitmap = BitmapFactory.decodeFile(image3);
            image_detail.setVisibility(View.VISIBLE);
            s_video.setVisibility(View.GONE);
            image_detail.setImageBitmap(bitmap);
        }
        if (flag.equals("4")){
            String video = intent.getStringExtra("video");
            s_video.setVideoURI(Uri.parse(video));
            //image_detail.setVisibility(View.GONE);
            s_video.setVisibility(View.VISIBLE);
            s_video.start();
        }
    }
}
