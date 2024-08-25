package com.example.sa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Calculation extends AppCompatActivity {

    static EditText electrcity, heating, fuel, airTravel, trainTravel, meat, milk, vegatablesFruits;
    static CheckBox checkboxAirTravel, checkboxTrainTravel, checkboxMeat;

    static String country = Beginning.selectedCountry;
    public static BigDecimal electriciTYCarbonFootprint, fuelCarbonFootprint, airTravelCarbonFootprint, trainTravelCarbonFootprint,
            MeatCarbonFootprint, MilkCarbonFootprint, VegatablesFruitsCarbonFootprint;
    public static BigDecimal heatingCarbonFootprint, eatCarbonFootprint, totalTravelCarbonFootprint;
    public static BigDecimal totalCarbonFootprint;

    private DatabaseHelper dbHelper;
    private Spinner electrcity_spinner, heating_spinner, vehicless_spinner;

    static BigDecimal electricityEmisson;

    static BigDecimal electricityAnnualCarbonFootprint, heatingAnnualCarbonFootprint, fuelAnnualCarbonFootprint, travelAnnualCarbonFootprint, foodsAnnualCarbonFootprint, totalAnnualCarbonFootprint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculation);

        idBinding();
        dbHelper = new DatabaseHelper(this);

        ArrayAdapter<CharSequence> energyAdapter = ArrayAdapter.createFromResource(this, R.array.electric_types, android.R.layout.simple_spinner_item);
        energyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        electrcity_spinner.setAdapter(energyAdapter);

        ArrayAdapter<CharSequence> heatingAdapter = ArrayAdapter.createFromResource(this, R.array.Heating_types, android.R.layout.simple_spinner_item);
        heatingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        heating_spinner.setAdapter(heatingAdapter);

        ArrayAdapter<CharSequence> vehiclessAdapter = ArrayAdapter.createFromResource(this, R.array.Vehicle_types, android.R.layout.simple_spinner_item);
        vehiclessAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicless_spinner.setAdapter(vehiclessAdapter);
    }

    public void idBinding() {
        electrcity = findViewById(R.id.editTextElectricity);
        heating = findViewById(R.id.editTextHeating);
        fuel = findViewById(R.id.editTextFuel);
        airTravel = findViewById(R.id.editTextAirTravel);
        trainTravel = findViewById(R.id.editTextTrainTravel);
        meat = findViewById(R.id.editTextMeaat);
        milk = findViewById(R.id.editTextMilk);
        vegatablesFruits = findViewById(R.id.editTextVegatablesFruits);
        electrcity_spinner = findViewById(R.id.electricitySpinner);
        checkboxAirTravel = findViewById(R.id.checkBoxTravelAir);
        checkboxTrainTravel = findViewById(R.id.checkBoxTravelTrain);
        checkboxMeat = findViewById(R.id.checkBoxMeat);
        heating_spinner = findViewById(R.id.heatingSpinner);
        vehicless_spinner = findViewById(R.id.vehicleSpinner);
    }

    public void calculate(View v) {
        if (isInputValid()) {
            electricityCalculate();
            heatingCalculate();
            VehiclessCalculate();
            airTravelCalculate();
            trainTravelCalculate();
            meatCalculate();
            milkCalculate();
            vegatablesFruitsCalculate();

            totalCarbonFootprint();

            annualForecast();

            Intent intent = new Intent(this, Conclusion.class);
            intent.putExtra("totalCarbonFootprint", totalCarbonFootprint);
            startActivity(intent);
        }
    }

    private boolean isInputValid() {
        boolean isValid = true;

        // EditText alanlarını kontrol et
        if (electrcity.getText().toString().trim().isEmpty()) {
            electrcity.setError("Please fill in this field");
            isValid = false;
        }
        if (heating.getText().toString().trim().isEmpty()) {
            heating.setError("Please fill in this field");
            isValid = false;
        }
        if (fuel.getText().toString().trim().isEmpty()) {
            fuel.setError("Please fill in this field");
            isValid = false;
        }
        if (!checkboxAirTravel.isChecked() && airTravel.getText().toString().trim().isEmpty()) {
            airTravel.setError("Please fill in this field");
            isValid = false;
        }
        if (!checkboxTrainTravel.isChecked() && trainTravel.getText().toString().trim().isEmpty()) {
            trainTravel.setError("Please fill in this field");
            isValid = false;
        }
        if (!checkboxMeat.isChecked() && meat.getText().toString().trim().isEmpty()) {
            meat.setError("Please fill in this field");
            isValid = false;
        }
        if (milk.getText().toString().trim().isEmpty()) {
            milk.setError("Please fill in this field");
            isValid = false;
        }
        if (vegatablesFruits.getText().toString().trim().isEmpty()) {
            vegatablesFruits.setError("Please fill in this field");
            isValid = false;
        }

        if (!isValid) {
            Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_LONG).show();
        }

        return isValid;
    }

    public void electricityCalculate() {
        String selectedEnergyType = electrcity_spinner.getSelectedItem().toString();
        BigDecimal electricityUsed = getEditTextValue(electrcity);
        electricityEmisson = dbHelper.getEmissionFactorElectricity(country, selectedEnergyType);
        electriciTYCarbonFootprint = electricityUsed.multiply(electricityEmisson);
    }

    public void heatingCalculate() {
        String selectedHeatingType = heating_spinner.getSelectedItem().toString();
        BigDecimal heatingUsed = getEditTextValue(heating);

        if (selectedHeatingType.equals("Natural Gas")) {
            BigDecimal NaturalGasEmisson = dbHelper.getEmissionFactorNaturalGas(country);
            heatingCarbonFootprint = heatingUsed.multiply(NaturalGasEmisson);
        } else if (selectedHeatingType.equals("Coal")) {
            BigDecimal coalEmisson = dbHelper.getEmissionFactorCoal(country);
            heatingCarbonFootprint = heatingUsed.multiply(coalEmisson);
        } else if (selectedHeatingType.equals("Electric")) {
            BigDecimal electricEmissonHeading = electricityEmisson;
            heatingCarbonFootprint = heatingUsed.multiply(electricEmissonHeading);
        }
    }

    public void VehiclessCalculate() {
        BigDecimal fuelUsed = getEditTextValue(fuel);
        String selectedVehicleType = vehicless_spinner.getSelectedItem().toString();
        BigDecimal vehicleEmisson = dbHelper.getEmissionFactorVehicle(country, selectedVehicleType);
        fuelCarbonFootprint = fuelUsed.multiply(vehicleEmisson);
    }

    public void airTravelCalculate() {
        if (checkboxAirTravel.isChecked()) {
            airTravelCarbonFootprint = BigDecimal.ZERO;
        } else {
            BigDecimal airEmisson = dbHelper.getEmissionFactorFlight(country);
            BigDecimal airTravelUsed = getEditTextValue(airTravel);
            airTravelCarbonFootprint = airTravelUsed.multiply(airEmisson);
        }
    }

    public void trainTravelCalculate() {
        if (checkboxTrainTravel.isChecked()) {
            trainTravelCarbonFootprint = BigDecimal.ZERO;
            totalTravelCarbonFootprint = airTravelCarbonFootprint.add(trainTravelCarbonFootprint);
        } else {
            BigDecimal trainEmisson = dbHelper.getEmissionFActoryStreetcar(country);
            BigDecimal trainTravelUsed = getEditTextValue(trainTravel);
            trainTravelCarbonFootprint = trainTravelUsed.multiply(trainEmisson);
        }
        totalTravelCarbonFootprint = airTravelCarbonFootprint.add(trainTravelCarbonFootprint);
    }

    public void meatCalculate() {
        if (checkboxMeat.isChecked()) {
            MeatCarbonFootprint = BigDecimal.ZERO;
        } else {
            BigDecimal meatEmisson = dbHelper.getEmissionFactoryRedMeat(country);
            BigDecimal meatUsed = getEditTextValue(meat);
            MeatCarbonFootprint = meatUsed.multiply(meatEmisson);
        }
    }

    public void milkCalculate() {
        BigDecimal milkEmisson = dbHelper.getEmissionFactoryMilk(country);
        BigDecimal milkUsed = getEditTextValue(milk);
        MilkCarbonFootprint = milkUsed.multiply(milkEmisson);
    }

    public void vegatablesFruitsCalculate() {
        BigDecimal vegatablesFruitsEmisson = dbHelper.getEmissionFactoryFruits(country);
        BigDecimal vegatablesFruitsUsed = getEditTextValue(vegatablesFruits);
        VegatablesFruitsCarbonFootprint = vegatablesFruitsUsed.multiply(vegatablesFruitsEmisson);
        eatCarbonFootprint = MeatCarbonFootprint.add(MilkCarbonFootprint).add(VegatablesFruitsCarbonFootprint);
    }

    public void totalCarbonFootprint() {
        totalCarbonFootprint = eatCarbonFootprint.add(totalTravelCarbonFootprint)
                .add(fuelCarbonFootprint).add(heatingCarbonFootprint).add(electriciTYCarbonFootprint);

        String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        String date = currentDateTime;

        BigDecimal total = totalCarbonFootprint;

        dbHelper.verikaydetme(total, date);


    }

    public void annualForecast() {
        electricityAnnualCarbonFootprint = electriciTYCarbonFootprint.multiply(new BigDecimal("12"));
        heatingAnnualCarbonFootprint = heatingCarbonFootprint.multiply(new BigDecimal("12"));
        fuelAnnualCarbonFootprint = fuelCarbonFootprint.multiply(new BigDecimal("12"));
        travelAnnualCarbonFootprint = totalTravelCarbonFootprint.multiply(new BigDecimal("12"));
        foodsAnnualCarbonFootprint = eatCarbonFootprint.multiply(new BigDecimal("12"));
        totalAnnualCarbonFootprint = totalCarbonFootprint.multiply(new BigDecimal("12"));
    }

    public BigDecimal getEditTextValue(EditText editText) {
        String value = editText.getText().toString().trim();
        if (value.isEmpty()) {
            return BigDecimal.ZERO;
        } else {
            return new BigDecimal(value);
        }
    }
}
