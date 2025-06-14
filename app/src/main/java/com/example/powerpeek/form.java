package com.example.powerpeek;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class form extends AppCompatActivity {

    protected Cursor cursor;
    DataHelper dbHelper;
    Button calcBtn;
    EditText monthInput, unitInput, rebateInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Optional: enable back button
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        dbHelper = new DataHelper(this);
        Spinner monthSpinner = findViewById(R.id.spinnerMonth);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.month_array,
                R.layout.spinner_text
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(adapter);

        EditText unitInput = findViewById(R.id.editTextUnits);
        RadioGroup group1 = findViewById(R.id.radioGroup1);
        RadioGroup group2 = findViewById(R.id.radioGroup2);
        Button calcBtn = findViewById(R.id.buttonCalculate);

// Make radio groups mutually exclusive
        group1.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1) group2.clearCheck();
        });
        group2.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1) group1.clearCheck();
        });

        TextView resultText = findViewById(R.id.textViewResult);

        calcBtn.setOnClickListener(view -> {
            String month = monthSpinner.getSelectedItem().toString();
            String unitStr = unitInput.getText().toString().trim();

            if (unitStr.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter electricity usage", Toast.LENGTH_SHORT).show();
                return;
            }

            float unit = Float.parseFloat(unitStr);
            int rebatePercent = -1;

            // Get selected rebate
            int selectedId1 = group1.getCheckedRadioButtonId();
            int selectedId2 = group2.getCheckedRadioButtonId();

            if (selectedId1 != -1) {
                RadioButton selectedRadio = findViewById(selectedId1);
                rebatePercent = Integer.parseInt(selectedRadio.getText().toString().replace("%", ""));
            } else if (selectedId2 != -1) {
                RadioButton selectedRadio = findViewById(selectedId2);
                rebatePercent = Integer.parseInt(selectedRadio.getText().toString().replace("%", ""));
            } else {
                Toast.makeText(getApplicationContext(), "Please select rebate percentage", Toast.LENGTH_SHORT).show();
                return;
            }

            // Block tariff calculation
            float totalCharge = 0f;
            float remainingUnit = unit;

            if (remainingUnit > 0) {
                float block = Math.min(200, remainingUnit);
                totalCharge += block * 0.218f;
                remainingUnit -= block;
            }
            if (remainingUnit > 0) {
                float block = Math.min(100, remainingUnit);
                totalCharge += block * 0.334f;
                remainingUnit -= block;
            }
            if (remainingUnit > 0) {
                float block = Math.min(300, remainingUnit);
                totalCharge += block * 0.516f;
                remainingUnit -= block;
            }
            if (remainingUnit > 0) {
                totalCharge += remainingUnit * 0.546f;
            }

            // Apply rebate
            float rebateAmount = totalCharge * (rebatePercent / 100f);
            float finalCost = totalCharge - rebateAmount;

            // Round to 2 decimal places
            String totalFormatted = String.format("%.2f", totalCharge);
            String finalFormatted = String.format("%.2f", finalCost);

            // Show result before saving
            resultText.setText("Total Charges: RM " + totalFormatted + "\nFinal Cost (after " + rebatePercent + "% rebate): RM " + finalFormatted);

            // Save to DB
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL("INSERT INTO electricity(month, unit, rebate, totalCharge, finalCost) VALUES('" +
                    month + "', '" + unit + "', '" + rebatePercent + "', '" + totalFormatted + "', '" + finalFormatted + "');");

            Toast.makeText(getApplicationContext(), "Data Successfully Saved", Toast.LENGTH_SHORT).show();

            if (ListViewActivity.ma != null) {
                ListViewActivity.ma.RefreshList();
            }
        });

    }
}