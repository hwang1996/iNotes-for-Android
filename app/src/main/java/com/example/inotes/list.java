package com.example.inotes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by 王浩啊 on 2016/9/16.
 */
public class list extends AppCompatActivity{

    private MyAdapter adapter;
    private NotesDB notesDB;
    private SQLiteDatabase dbReader;

    private ListView lv;
    private Intent i;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        lv = (ListView) findViewById(R.id.list);
        notesDB = new NotesDB(this);
        dbReader = notesDB.getReadableDatabase();
        //dbReader.execSQL("SELECT * FROM TABLE_NAME ORDERBY TIME ASC;");

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = dbReader.query(NotesDB.TABLE_NAME, null, null, null, null, null, "_id desc");
                //Cursor cursor = dbReader.rawQuery("select * from NotesDB.TABLE_NAME ORDER BY NotesDB.ID desc", null);
                cursor.moveToPosition(position);
                i = new Intent(list.this, SelectAct.class);
                i.putExtra(NotesDB.ID, cursor.getInt(cursor.getColumnIndex(NotesDB.ID)));
                i.putExtra(NotesDB.CONTENT, cursor.getString(cursor.getColumnIndex(NotesDB.CONTENT)));
                i.putExtra(NotesDB.TIME, cursor.getString(cursor.getColumnIndex(NotesDB.TIME)));
                i.putExtra(NotesDB.PATH1, cursor.getString(cursor.getColumnIndex(NotesDB.PATH1)));
                i.putExtra(NotesDB.PATH2, cursor.getString(cursor.getColumnIndex(NotesDB.PATH2)));
                i.putExtra(NotesDB.PATH3, cursor.getString(cursor.getColumnIndex(NotesDB.PATH3)));
                i.putExtra(NotesDB.VIDEO, cursor.getString(cursor.getColumnIndex(NotesDB.VIDEO)));
                i.putExtra(NotesDB.LOCATION, cursor.getString(cursor.getColumnIndex(NotesDB.LOCATION)));
                startActivity(i);
            }
        });
    }

    public void selectDB(){
        Cursor cursor = dbReader.query(NotesDB.TABLE_NAME, null, null, null, null, null, "_id desc");
        adapter = new MyAdapter(this, cursor);
        lv.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectDB();
    }
}
