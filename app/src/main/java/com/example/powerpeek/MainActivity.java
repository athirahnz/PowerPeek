package com.example.powerpeek;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button listView_page;
    Button about_page;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        listView_page = (Button) findViewById(R.id.start);
        about_page = (Button) findViewById(R.id.about);
    }

    public void open_listView(View view){
        Intent intent = new Intent(getApplicationContext(), ListViewActivity.class);
        startActivity(intent);
    }
    public void open_about(View view){
        Intent intent = new Intent(getApplicationContext(), about.class);
        startActivity(intent);
    }
}