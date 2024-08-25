package com.example.sa;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class HomePage extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_sayfa);

        dbHelper = new DatabaseHelper(this);

        try {
            dbHelper.createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

    }

    public void gecis(View v) {
        Intent az = new Intent(HomePage.this, Beginning.class);
        startActivity(az);
    }

    public void history(View v) {
        Intent ax = new Intent(HomePage.this, HistoryFootPrint.class);
        startActivity(ax);
    }

    public void whatco(View v) {
        Intent cv = new Intent(HomePage.this, whatFootprint.class);
        startActivity(cv);
    }
}
