package com.example.inotes;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by 王浩啊 on 2016/9/15.
 */
public class Edit extends AppCompatActivity implements AMapLocationListener {

    private final String IMAGE_TYPE = "image/*";
    private final int IMAGE_CODE = 0;   //这里的IMAGE_CODE是自己任意定义的

    LocationManagerProxy mLocationManagerProxy;
    private String currentAddress;

    private EditText ettext;
    private TextView hint;
    private TextView location;

    private File phoneFile1, phoneFile2, phoneFile3, videoFile;

    private NotesDB notesDB;
    private SQLiteDatabase dbWriter;

    private ImageView image_edit_1;
    private ImageView image_edit_2;
    private ImageView image_edit_3;
    private ImageView c_video;

    private int image_edit_num = 0;

    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcontent);

        mLocationManagerProxy = LocationManagerProxy.getInstance(this);
        mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 15, this);

        ettext = (EditText) findViewById(R.id.ettext);
        location = (TextView) findViewById(R.id.location);
        hint = (TextView) findViewById(R.id.hint);
        ettext.setTextColor(Color.BLACK);
        location.setTextColor(Color.BLACK);
        hint.setTextColor(Color.BLACK);

        image_edit_1 = (ImageView) findViewById(R.id.image_edit_1);
        image_edit_2 = (ImageView) findViewById(R.id.image_edit_2);
        image_edit_3 = (ImageView) findViewById(R.id.image_edit_3);
        c_video = (ImageView) findViewById(R.id.c_video);

        notesDB = new NotesDB(this);
        dbWriter = notesDB.getWritableDatabase();

        i = new Intent(Edit.this, details.class);

        image_edit_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("image1", phoneFile1.getAbsolutePath());
                i.putExtra("flag", "1");
                startActivity(i);
            }
        });

        image_edit_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("image2", phoneFile2.getAbsolutePath());
                i.putExtra("flag", "2");
                startActivity(i);
            }
        });

        image_edit_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("image3", phoneFile3.getAbsolutePath());
                i.putExtra("flag", "3");
                startActivity(i);
            }
        });

        c_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("video", videoFile.getAbsolutePath());
                i.putExtra("flag", "4");
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.camera:
                new AlertDialog.Builder(Edit.this)
                        .setItems(new String[]{"新的", "本地"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0){
                                    newPhoto();
                                }
                                if(which==1){
                                    localPhoto();
                                }
                            }
                        })
                        .show();
                break;
            case R.id.movie:
                Intent video = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                videoFile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/"+"iNotes"+"/"+getTime()+".mp4");
                video.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile));
                startActivityForResult(video, 2);
                break;
            case R.id.save:
                addDB();
                finish();
                break;
            default:
        }
        return true;
    }

    public void newPhoto(){
        image_edit_num++;
        if (image_edit_num==1){
            Intent iimg = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            phoneFile1 = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/"+"iNotes"+"/"+getTime()+".jpg");
            iimg.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(phoneFile1));
            startActivityForResult(iimg, 1);
        }
        if (image_edit_num==2){
            Intent iimg = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            phoneFile2 = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/"+"iNotes"+"/"+getTime()+".jpg");
            iimg.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(phoneFile2));
            startActivityForResult(iimg, 1);
        }
        else if(image_edit_num>=3){
            Intent iimg = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            phoneFile3 = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/"+"iNotes"+"/"+getTime()+".jpg");
            iimg.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(phoneFile3));
            startActivityForResult(iimg, 1);
        }
    }

    public void localPhoto(){
        image_edit_num++;

        Intent getAlbum = new Intent(Intent.ACTION_PICK);
        getAlbum.setType(IMAGE_TYPE);
        startActivityForResult(getAlbum, IMAGE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
           //Bitmap bitmap = BitmapFactory.decodeFile(phoneFile.getAbsolutePath());
            if(image_edit_num==1){
//                Log.e("long", phoneFile1.getAbsolutePath());
//                Log.e("long", String.valueOf(phoneFile1.getAbsolutePath().length()));
//                if( phoneFile1.getAbsolutePath().charAt(20)=='i') {
//                    Log.e("long","OK");
//                }
                image_edit_1.setImageBitmap(getImageThumbnail(phoneFile1.getAbsolutePath(), 300, 300));
            }
            if(image_edit_num==2){
                image_edit_2.setImageBitmap(getImageThumbnail(phoneFile2.getAbsolutePath(), 300, 300));
            }
            else if(image_edit_num==3){
                image_edit_3.setImageBitmap(getImageThumbnail(phoneFile3.getAbsolutePath(), 300, 300));
            }
        }
        if(requestCode == 2){
            c_video.setImageBitmap(getVideoThumbnail(videoFile.getAbsolutePath(), 300, 300, MediaStore.Images.Thumbnails.MICRO_KIND));
        }

        Bitmap bm = null;
        //外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
        ContentResolver resolver = getContentResolver();
        //此处的用于判断接收的Activity是不是你想要的那个
        if (requestCode == IMAGE_CODE) {
            try {
                Uri originalUri = data.getData();        //获得图片的uri
                bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);

                //    这里开始的第二部分，获取图片的路径：
                String[] proj = {MediaStore.Images.Media.DATA};
                //好像是android多媒体数据库的封装接口，具体的看Android文档
                Cursor cursor = managedQuery(originalUri, proj, null, null, null);
                //按我个人理解 这个是获得用户选择的图片的索引值
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                //将光标移至开头 ，这个很重要，不小心很容易引起越界
                cursor.moveToFirst();
                if(image_edit_num==1){
                    //最后根据索引值获取图片路径
                    String path = cursor.getString(column_index);
                    image_edit_1.setImageBitmap(getImageThumbnail(path, 300, 300));
                    phoneFile1 = new File(path);
                }
                if(image_edit_num==2){
                    String path = cursor.getString(column_index);
                    image_edit_2.setImageBitmap(getImageThumbnail(path, 300, 300));
                    phoneFile2 = new File(path);
                }
                else if(image_edit_num==3){
                    String path = cursor.getString(column_index);
                    image_edit_3.setImageBitmap(getImageThumbnail(path, 300, 300));
                    phoneFile3 = new File(path);
                }
            }catch (IOException e) {
                Log.e("TAG-->Error",e.toString());
            }
        }
    }

    private void addDB(){
        ContentValues cv = new ContentValues();
        cv.put(NotesDB.CONTENT, ettext.getText().toString());
        cv.put(NotesDB.TIME, getTime());
        cv.put(NotesDB.PATH1, phoneFile1+"");
        cv.put(NotesDB.PATH2, phoneFile2+"");
        cv.put(NotesDB.PATH3, phoneFile3+"");
        cv.put(NotesDB.VIDEO, videoFile+"");
        cv.put(NotesDB.LOCATION, currentAddress + "");
        dbWriter.insert(NotesDB.TABLE_NAME, null, cv);
    }

    private String getTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        java.util.Date date = new java.util.Date();
        String str = format.format(date);
        return str;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if(aMapLocation != null){
            //Log.e("Location", aMapLocation.toString());
            currentAddress = aMapLocation.getAddress();
            location.setText(currentAddress);
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

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

//    @Override
//    protected void onDestroy(){
//        super.onDestroy();
//        mLocationManagerProxy.destroy();
//    }

    @Override
    public void onBackPressed(){
        mLocationManagerProxy.destroy();
        deleteFile();
        finish();
    }

    public void deleteFile() {
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
}
