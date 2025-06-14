package com.example.powerpeek;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        // Optional: enable back button
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        // Fetch data
        String month = getIntent().getStringExtra("month");
        float unit = getIntent().getFloatExtra("unit", 0);
        int rebate = getIntent().getIntExtra("rebate", 0);
        float totalCharge = getIntent().getFloatExtra("totalCharge", 0);
        float finalCost = getIntent().getFloatExtra("finalCost", 0);

        // Display to text views
        ((TextView) findViewById(R.id.detailMonth)).setText(month);
        ((TextView) findViewById(R.id.detailUnit)).setText("Units Used: " + unit + " kWh");
        ((TextView) findViewById(R.id.detailRebate)).setText("Rebate: " + rebate + "%");
        ((TextView) findViewById(R.id.detailTotal)).setText("Total Charges: RM " + String.format("%.2f", totalCharge));
        ((TextView) findViewById(R.id.detailFinal)).setText("Final Cost: RM " + String.format("%.2f", finalCost));
    }
}
