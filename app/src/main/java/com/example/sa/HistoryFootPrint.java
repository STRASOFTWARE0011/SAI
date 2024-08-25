package com.example.sa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class HistoryFootPrint extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_foot_print);

        dbHelper = new DatabaseHelper(this);

        // Retrieve data from the database
        List<String[]> data = dbHelper.getAllFootprints();

        // Find the main layout
        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        // Use LayoutInflater to inflate the layout
        LayoutInflater inflater = LayoutInflater.from(this);

        // Start a loop for the data
        for (String[] record : data) {
            // Inflate the CardView layout
            View cardView = inflater.inflate(R.layout.history, linearLayout, false);

            // Populate TextViews with data
            TextView dateTextView = cardView.findViewById(R.id.textView12);
            TextView totalTextView = cardView.findViewById(R.id.textView13);

            dateTextView.setText(record[0]);
            totalTextView.setText(record[1]);

            // Add the inflated CardView to the main layout
            linearLayout.addView(cardView);
        }
    }
}
