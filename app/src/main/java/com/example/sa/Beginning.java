package com.example.sa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class Beginning extends AppCompatActivity {

    static Spinner countrySpinner;
    private DatabaseHelper dbHelper;

    static String selectedCountry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beginning);

        countrySpinner = findViewById(R.id.spinner);

        dbHelper = new DatabaseHelper(this);

        List<String> countries = dbHelper.getAllCountries();
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,countries);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(countryAdapter);

    }
    public void continnue (View v ){

        selectedCountry = countrySpinner.getSelectedItem().toString();

        Intent intent = new Intent(this, Calculation.class);
        startActivity(intent);

    }
}
