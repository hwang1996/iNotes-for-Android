package com.example.inotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

/**
 * Created by 王浩啊 on 2016/9/16.
 */
public class SelectAct extends AppCompatActivity {

    private NotesDB notesDB;
    private SQLiteDatabase dbWriter;

    private TextView s_text;
    private TextView s_time;
    private ImageView image_s_1;
    private ImageView image_s_2;
    private ImageView image_s_3;
    private ImageView s_video;
    private TextView s_location;

    private File phoneFile1, phoneFile2, phoneFile3, videoFile;

    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectcontent);

        notesDB = new NotesDB(this);
        dbWriter = notesDB.getWritableDatabase();

        s_text = (TextView) findViewById(R.id.s_text);
        s_time = (TextView) findViewById(R.id.s_time);
        image_s_1 = (ImageView) findViewById(R.id.image_s_1);
        image_s_2 = (ImageView) findViewById(R.id.image_s_2);
        image_s_3 = (ImageView) findViewById(R.id.image_s_3);
        s_video = (ImageView) findViewById(R.id.s_video);
        s_location = (TextView) findViewById(R.id.s_location);

        s_text.setText(getIntent().getStringExtra(NotesDB.CONTENT));
        s_location.setText(getIntent().getStringExtra(NotesDB.LOCATION));
        s_time.setText(getIntent().getStringExtra(NotesDB.TIME));
        image_s_1.setImageBitmap(getImageThumbnail(getIntent().getStringExtra(NotesDB.PATH1), 300, 300));
        image_s_2.setImageBitmap(getImageThumbnail(getIntent().getStringExtra(NotesDB.PATH2), 300, 300));
        image_s_3.setImageBitmap(getImageThumbnail(getIntent().getStringExtra(NotesDB.PATH3), 300, 300));
        s_video.setImageBitmap(getVideoThumbnail(getIntent().getStringExtra(NotesDB.VIDEO), 300, 300, MediaStore.Images.Thumbnails.MICRO_KIND));

        i = new Intent(SelectAct.this, details.class);

        image_s_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String image1 = getIntent().getStringExtra(NotesDB.PATH1);
                i.putExtra("image1", image1);
                i.putExtra("flag", "1");
                startActivity(i);
            }
        });
        image_s_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("image2", getIntent().getStringExtra(NotesDB.PATH2));
                i.putExtra("flag", "2");
                startActivity(i);
            }
        });
        image_s_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("image3", getIntent().getStringExtra(NotesDB.PATH3));
                i.putExtra("flag", "3");
                startActivity(i);
            }
        });
        s_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("video", getIntent().getStringExtra(NotesDB.VIDEO));
                i.putExtra("flag", "4");
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.select, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                new AlertDialog.Builder(this)
                        .setMessage("确认删除？")
                        .setNegativeButton("否", null)
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteDate();
                                deleteFile();
                                finish();
                            }
                        })
                        .show();
                break;
            default:
        }
        return true;
    }

    public void deleteDate() {
        dbWriter.delete(NotesDB.TABLE_NAME,
                "_id=" + getIntent().getIntExtra(NotesDB.ID, 0), null);
    }

    public void deleteFile(){
        phoneFile1 = new File(getIntent().getStringExtra(NotesDB.PATH1));
        phoneFile2 = new File(getIntent().getStringExtra(NotesDB.PATH2));
        phoneFile3 = new File(getIntent().getStringExtra(NotesDB.PATH3));
        videoFile = new File(getIntent().getStringExtra(NotesDB.VIDEO));
        if (phoneFile1.exists()) { // 判断文件是否存在
            if (phoneFile1.isFile()) { // 判断是否是文件
                if(phoneFile1.getAbsolutePath().charAt(20)=='i'&&phoneFile1.getAbsolutePath().charAt(21)=='N'){
                    phoneFile1.delete(); // delete()方法 你应该知道 是删除的意思;
                }
            }
        }
        if (phoneFile2.exists()) { // 判断文件是否存在
            if (phoneFile2.isFile()) { // 判断是否是文件
                if(phoneFile2.getAbsolutePath().charAt(20)=='i'&&phoneFile2.getAbsolutePath().charAt(21)=='N'){
                    phoneFile2.delete(); // delete()方法 你应该知道 是删除的意思;
                }
            }
        }
        if (phoneFile3.exists()) { // 判断文件是否存在
            if (phoneFile3.isFile()) { // 判断是否是文件
                if(phoneFile3.getAbsolutePath().charAt(20)=='i'&&phoneFile3.getAbsolutePath().charAt(21)=='N'){
                    phoneFile3.delete(); // delete()方法 你应该知道 是删除的意思;
                }
            }
        }
        if (videoFile.exists()) { // 判断文件是否存在
            if (videoFile.isFile()) { // 判断是否是文件
                videoFile.delete(); // delete()方法 你应该知道 是删除的意思;
            }
        }
    }

    //获取图片缩略图
    public Bitmap getImageThumbnail(String uri, int width, int height){
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();    //直接获取缩略图
        options.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeFile(uri, options);
        options.inJustDecodeBounds = false;
        int beWidth = options.outWidth/width;   //获得缩略图的宽度
        int beHeight = options.outHeight/height;
        int be = 1;
        if(beWidth<beHeight){
            be = beWidth;
        }else{
            be = beHeight;
        }
        if(be<=0){
            be = 1;
        }
        options.inSampleSize = be;
        bitmap = BitmapFactory.decodeFile(uri, options);   //获取转化之后的图片
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }
    //获取视频缩略图
    public Bitmap getVideoThumbnail(String uri, int width, int height, int kind){
        Bitmap bitmap = null;
        bitmap = ThumbnailUtils.createVideoThumbnail(uri, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

        return bitmap;
    }
}
