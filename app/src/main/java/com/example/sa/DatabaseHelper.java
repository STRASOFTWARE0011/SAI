package com.example.sa;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Sai.db";
    private static final int DATABASE_VERSION = 3;
    private final Context context;
    private SQLiteDatabase database;
    private boolean databaseExist = false;





    public DatabaseHelper(@Nullable Context context) {

        super(context, context.getDatabasePath(DATABASE_NAME).getPath(), null, DATABASE_VERSION);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (!databaseExist) {

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onCreate(db);
    }

    public void createDatabase() throws IOException {
        if (!checkDatabase()) {
            this.getReadableDatabase();
            copyDatabase();
        }
    }

    private boolean checkDatabase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = context.getDatabasePath(DATABASE_NAME).getPath();
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (Exception e) {
            // Veritabanı mevcut değil
        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null;
    }

    private void copyDatabase() throws IOException {
        AssetManager assetManager = context.getAssets();
        InputStream myInput = assetManager.open(DATABASE_NAME);
        String outFileName = context.getDatabasePath(DATABASE_NAME).getPath();
        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public SQLiteDatabase openDatabase() throws SQLException {
        String myPath = context.getDatabasePath(DATABASE_NAME).getPath();
        database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        return database;
    }





    // Emission tablosundan Country sütununu çeken yöntem
    public List<String> getAllCountries() {
        List<String> countries = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT Country FROM Emission_Coal " +
                "UNION " +
                "SELECT Country FROM Emission_Electric " +
                "UNION " +
                "SELECT Country FROM Emission_Flight " +
                "UNION " +
                "SELECT Country FROM Emission_Foods " +
                "UNION " +
                "SELECT Country FROM Emission_NaturalGas " +
                "UNION " +
                "SELECT Country FROM Emission_Transport " +
                "UNION " +
                "SELECT Country FROM Emission_Vehicle ";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                countries.add(cursor.getString(cursor.getColumnIndexOrThrow("Country")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return countries;
    }

  public BigDecimal verikaydetme(BigDecimal total,String date){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "INSERT INTO history (totalValue,history) VALUES (?,?)";
        ContentValues values = new ContentValues();
        values.put("totalValue", String.valueOf(total));
        values.put("history", date);
        db.insert("history", null, values);
        return total;
  }


    public List<String[]> getAllFootprints() {
        List<String[]> footprints = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT history, totalValue FROM history", null);

        if (cursor.moveToFirst()) {
            do {
                String history = cursor.getString(0);
                String totalValue = cursor.getString(1);
                footprints.add(new String[]{history, totalValue});
            } while (cursor.moveToNext());
        }
        cursor.close();
        return footprints;
    }



    // Emission_Coal tablosundan Emission sütununu çeken yöntemi
    public BigDecimal getEmissionFactorCoal(String country) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT Emission FROM Emission_Coal WHERE Country=?";
        Cursor cursor = db.rawQuery(query, new String[]{country});
        BigDecimal emissionFactor = BigDecimal.ZERO;
        if (cursor.moveToFirst()) {
            emissionFactor = new BigDecimal(cursor.getString(0));
        }
        cursor.close();
        return emissionFactor;

    }


    // Emission_Electric tablosundan Solar,Coal,Barrage sütununu çeken yöntem
    public BigDecimal getEmissionFactorElectricity(String country, String energyType) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + energyType + " FROM Emission_Electric WHERE Country=?";
        Cursor cursor = db.rawQuery(query, new String[]{country});

        BigDecimal emissionFactor = BigDecimal.ZERO;
        if (cursor.moveToFirst()) {
            emissionFactor = new BigDecimal(cursor.getString(0));
        }
        cursor.close();
        return emissionFactor;

    }

    // Emission_Flight tablosundan Emission sütununu çeken yöntem
    public BigDecimal getEmissionFactorFlight(String country) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT  Emission FROM Emission_Flight WHERE Country=?";
        Cursor cursor = db.rawQuery(query, new String[]{country});
        BigDecimal emissionFactor = BigDecimal.ZERO;
        if (cursor.moveToFirst()) {
            emissionFactor = new BigDecimal(cursor.getString(0));
        }
        cursor.close();
        return emissionFactor;

    }

    // Emission_Foods tablosundan Red sütununu çeken yöntem
    public BigDecimal getEmissionFactoryRedMeat(String country) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT Red FROM Emission_Foods  WHERE Country=?";
        Cursor cursor = db.rawQuery(query, new String[]{country});
        BigDecimal emissionFactor = BigDecimal.ZERO;
        if (cursor.moveToFirst()) {
            emissionFactor = new BigDecimal(cursor.getString(0));
        }
        cursor.close();
        return emissionFactor;

    }

    public BigDecimal getEmissionFactoryChicken(String country) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT Chicken FROM Emission_Foods WHERE Country=?";
        Cursor cursor = db.rawQuery(query, new String[]{country});
        BigDecimal emissionFactor = BigDecimal.ZERO;
        if (cursor.moveToFirst()) {
            emissionFactor = new BigDecimal(cursor.getString(0));
        }
        cursor.close();
        return emissionFactor;

    }

    // Emission_Foods tablosundan Milk sütununu çeken yöntem
    public BigDecimal getEmissionFactoryMilk(String country) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT Milk FROM Emission_Foods WHERE Country=?";
        Cursor cursor = db.rawQuery(query, new String[]{country});
        BigDecimal emissionFactor = BigDecimal.ZERO;
        if (cursor.moveToFirst()) {
            emissionFactor = new BigDecimal(cursor.getString(0));
        }
        cursor.close();
        return emissionFactor;

    }

    // Emission_Foods tablosundan Fruits sütununu çeken yöntem
    public BigDecimal getEmissionFactoryFruits(String country) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT Frutis FROM Emission_Foods WHERE Country=?";
        Cursor cursor = db.rawQuery(query, new String[]{country});
        BigDecimal emissionFactor = BigDecimal.ZERO;
        if (cursor.moveToFirst()) {
            emissionFactor = new BigDecimal(cursor.getString(0));
        }
        cursor.close();
        return emissionFactor;

    }

    public BigDecimal getEmissionFactorNaturalGas(String country) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT NaturalGas FROM Emission_NaturalGas WHERE Country=?";
        Cursor cursor = db.rawQuery(query, new String[]{country});

        BigDecimal emissionFactor = BigDecimal.ZERO;
        if (cursor.moveToFirst()) {
            emissionFactor = new BigDecimal(cursor.getString(0));
        }
        cursor.close();
        return emissionFactor;
    }

    public BigDecimal getEmissionFactoryBus(String country) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT Emission_Transport FROM Bus WHERE Country=?";
        Cursor cursor = db.rawQuery(query, new String[]{country});
        BigDecimal emissionFactor = BigDecimal.ZERO;
        if (cursor.moveToFirst()) {
            emissionFactor = new BigDecimal(cursor.getString(0));
        }

        cursor.close();
        return emissionFactor;
    }

    public BigDecimal getEmissionFactoryMetro(String country) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT Emission_Transport FROM Metro WHERE Country=?";
        Cursor cursor = db.rawQuery(query, new String[]{country});
        BigDecimal emissionFactor = BigDecimal.ZERO;
        if (cursor.moveToFirst()) {

            emissionFactor = new BigDecimal(cursor.getString(0));

        }
        cursor.close();
        return emissionFactor;
    }

    public BigDecimal getEmissionFActoryStreetcar(String country) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT Streetcar FROM Emission_Transport WHERE Country=?";
        Cursor cursor = db.rawQuery(query, new String[]{country});

        BigDecimal emissionFactor = BigDecimal.ZERO;
        if (cursor.moveToFirst()) {
            emissionFactor = new BigDecimal(cursor.getString(0));
        }
        cursor.close();
        return emissionFactor;
    }

    public BigDecimal getEmissionFactorVehicle(String country, String vehicleTypes) {

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT Disel FROM Emission_Vehicle WHERE Country=?";


        Cursor cursor = db.rawQuery(query, new String[]{country});
        BigDecimal emissionFactor = BigDecimal.ZERO;
        if (cursor.moveToFirst()) {
            emissionFactor = new BigDecimal(cursor.getString(0));
        }
        cursor.close();
        return emissionFactor;
    }

}


