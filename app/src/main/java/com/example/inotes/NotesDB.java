package com.example.inotes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 王浩啊 on 2016/9/15.
 */
public class NotesDB extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "notes";
    public static final String CONTENT = "content";
    public static final String PATH1 = "path1";
    public static final String PATH2 = "path2";
    public static final String PATH3 = "path3";
    public static final String VIDEO = "video";
    public static final String ID = "_id";
    public static final String TIME = "time";
    public static final String LOCATION = "location";

    public NotesDB(Context context){
        super(context, "notes", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "("
                +  ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CONTENT + " TEXT NOT NULL,"
                + PATH1 + " TEXT NOT NULL,"
                + PATH2 + " TEXT NOT NULL,"
                + PATH3 + " TEXT NOT NULL,"
                + VIDEO + " TEXT NOT NULL,"
                + TIME + " TEXT NOT NULL,"
                + LOCATION + " TEXT NOT NULL)");
        //db.execSQL("SELECT * FROM TABLE_NAME ORDER BY TIME ASC;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
