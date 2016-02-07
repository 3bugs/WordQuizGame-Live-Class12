package com.example.wordquizgame;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Button playGameButton;
    private Button highScoreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playGameButton = (Button) findViewById(R.id.playGameButton);
        playGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MainActivity", "Hello log");

                Toast t = Toast.makeText(
                        MainActivity.this,
                        "Hello",
                        Toast.LENGTH_SHORT
                );
                t.show();

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("เลือกระดับความยาก");
                //dialog.setMessage("Message");
                dialog.setIcon(R.drawable.logo);

                final String[] items = new String[]{"ง่าย", "ปานกลาง", "ยาก"};

                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "คุณเลือก: " + items[which]);

                        Intent i = new Intent(MainActivity.this, GameActivity.class);
                        i.putExtra("diff", which);
                        startActivity(i);
                    }
                });

                dialog.show();

/*
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "คุณคลิกปุ่ม OK");
                    }
                });

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "คุณคลิกปุ่ม Cancel");
                    }
                });
*/
            }
        });

        highScoreButton = (Button) findViewById(R.id.highScoreButton);
        highScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playGameButton.setVisibility(View.INVISIBLE);
            }
        });
    }
}
