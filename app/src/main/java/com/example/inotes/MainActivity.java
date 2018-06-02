package com.example.inotes;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ImageView neww;
    private ImageView memory;
    private ImageView footprints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        int permission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

        try {
            CreateFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }

        neww = (ImageView) findViewById(R.id.image1);
        memory = (ImageView) findViewById(R.id.image2);
        footprints = (ImageView) findViewById(R.id.image3);

        neww.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(MainActivity.this)
                        .setItems(new String[]{"记事", "提醒"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0){
                                    Intent intent = new Intent(MainActivity.this, Edit.class);
                                    startActivity(intent);
                                }
                                if(which==1){
                                    Intent intent = new Intent(MainActivity.this, clock.class);
                                    startActivity(intent);
                                }
                            }
                        })
                        .show();
            }
        });

        memory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, list.class);
                startActivity(intent);
            }
        });

        footprints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Footprints.class);
                startActivity(intent);
            }
        });
    }

    public void CreateFiles() throws IOException {
        File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/"+"iNotes");
        if(!file.exists()){
            try{
                file.mkdirs();
            }catch (Exception e){

            }
        }
    }
}
