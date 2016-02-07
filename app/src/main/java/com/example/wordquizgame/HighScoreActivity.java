package com.example.wordquizgame;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.wordquizgame.db.DatabaseHelper;

public class HighScoreActivity extends Activity {

    private DatabaseHelper mHelper;
    private SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        mHelper = new DatabaseHelper(this);
        mDatabase = mHelper.getWritableDatabase();

        Cursor cursor = mDatabase.query(
                DatabaseHelper.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_2,
                cursor,
                new String[]{DatabaseHelper.COL_SCORE, DatabaseHelper.COL_DIFFICULTY},
                new int[]{android.R.id.text1, android.R.id.text2},
                0
        );

        ListView list = (ListView) findViewById(R.id.listView);
        list.setAdapter(adapter);
    }
}
