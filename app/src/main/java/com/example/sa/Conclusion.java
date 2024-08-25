package com.example.sa;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Conclusion extends AppCompatActivity {

    private List<String> xValues = Arrays.asList("Electricity", "Heating", "Vehicle", "Travel", "Foods");
    private BarChart barChart;
    TextView total,warning,msj;
    TextView electricityCarbonFootprint, heatingCarbonFootprint, fuelCarbonFootprint, totalTravelCarbonFootprint, FoodsCarbonFootprint,totalCarbonFootprint;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sonuc);


        barChart = findViewById(R.id.barChart);
        total = findViewById(R.id.textViewTotalCarbonFootPrint);
        warning = findViewById(R.id.textViewWarning);
        msj = findViewById(R.id.textViewWarningMsj);

        electricityCarbonFootprint = findViewById(R.id.calculationElectricty);
        heatingCarbonFootprint = findViewById(R.id.calculationHeating);
        fuelCarbonFootprint = findViewById(R.id.calculationFuel);
        totalTravelCarbonFootprint = findViewById(R.id.calculationTravel);
        FoodsCarbonFootprint = findViewById(R.id.calculationFood);
        totalCarbonFootprint = findViewById(R.id.calculationTotal);


        // Receive the data sent from the Calculation class
        BigDecimal t = (BigDecimal) getIntent().getSerializableExtra("totalCarbonFootprint");

        if (t != null) {
            total.setText(String.valueOf(t)+"CO2 kg");


        }

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, Calculation.electriciTYCarbonFootprint.floatValue()));
        entries.add(new BarEntry(1, Calculation.heatingCarbonFootprint.floatValue()));
        entries.add(new BarEntry(2, Calculation.fuelCarbonFootprint.floatValue()));
        entries.add(new BarEntry(3, Calculation.totalTravelCarbonFootprint.floatValue()));
        entries.add(new BarEntry(4, Calculation.eatCarbonFootprint.floatValue()));

        BarDataSet dataSet = new BarDataSet(entries, "Carbon Footprint");
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.BLACK);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.BLUE);
        colors.add(Color.YELLOW);
        colors.add(Color.MAGENTA);
        dataSet.setColors(colors);

        BarData data = new BarData(dataSet);
        barChart.setData(data);

        // X-axis settings
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

        // Y axis settings (left and right)
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setGranularity(0.1f);
        leftAxis.setGranularityEnabled(true);

        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.2f", value);
            }
        });

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawLabels(false);

        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.2f", value);
            }
        });

        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        barChart.setScaleEnabled(false);
        barChart.setDragEnabled(false);
        barChart.invalidate();

        // Find the highest carbon footprint value and determine the category
        List<BigDecimal> values = Arrays.asList(
                Calculation.electriciTYCarbonFootprint,
                Calculation.heatingCarbonFootprint,
                Calculation.fuelCarbonFootprint,
                Calculation.totalTravelCarbonFootprint,
                Calculation.eatCarbonFootprint);

        BigDecimal max = values.get(0);
        int maxIndex = 0;

        for (int i = 1; i < values.size(); i++) {
            if (values.get(i).compareTo(max) > 0) {
                max = values.get(i);
                maxIndex = i;
            }
        }

        String maxCategory = xValues.get(maxIndex);
        warning.setText(maxCategory + " " + max.toPlainString());

        String warningMesaj;

        switch (maxCategory){

            case "Electricity":
                 warningMesaj = getString(R.string.electricty_warning);
                 msj.setText(warningMesaj);
                 break;

            case "Heating":
                 warningMesaj = getString(R.string.heating_warning);
                 msj.setText(warningMesaj);
                 break;

            case "Vehicle":
                 warningMesaj = getString(R.string.fuel_warning);
                 msj.setText(warningMesaj);
                 break;

            case "Travel":
                 warningMesaj = getString(R.string.travel_warning);
                 msj.setText(warningMesaj);
                 break;

            case "Foods":
                 warningMesaj = getString(R.string.eat_warning);
                 msj.setText(warningMesaj);
                 break;

        }

        electricityCarbonFootprint.setText(String.valueOf(Calculation.electricityAnnualCarbonFootprint)+"CO2 kg");
        heatingCarbonFootprint.setText(String.valueOf(Calculation.heatingAnnualCarbonFootprint)+"CO2 kg");
        fuelCarbonFootprint.setText(String.valueOf(Calculation.fuelAnnualCarbonFootprint)+"CO2 kg");
        totalTravelCarbonFootprint.setText(String.valueOf(Calculation.travelAnnualCarbonFootprint)+"CO2 kg");
        FoodsCarbonFootprint.setText(String.valueOf(Calculation.foodsAnnualCarbonFootprint)+"CO2 kg");
        totalCarbonFootprint.setText(String.valueOf(Calculation.totalAnnualCarbonFootprint)+"CO2 kg");




    }
}