package com.example.powerpeek;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class ListViewActivity extends AppCompatActivity {

    String[] register;
    ListView listView01;
    Menu menu;
    //access Database
    protected Cursor cursor;
    //use polymorphism to access DataHelper
    DataHelper dbcenter;
    //to access class CreateBio, UpdateBio, and ViewBio
    public static ListViewActivity ma;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view); // Replace with your XML file, like R.layout.activity_listview

        // ✅ Setup toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Optional: set title if not set in XML
        getSupportActionBar().setTitle("PowerPeek");

        // Initialize FAB (optional fix)
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListViewActivity.this, form.class);
                startActivity(intent);
            }
        });

        ma = this;
        dbcenter = new DataHelper(this);
        RefreshList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

    public void RefreshList() {
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM electricity", null);

        ArrayList<String[]> displayList = new ArrayList<>();
        cursor.moveToFirst();
        for (int cc = 0; cc < cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);
            String month = cursor.getString(1);
            String finalCost = cursor.getString(5);
            displayList.add(new String[]{month, finalCost});
        }

        listView01 = findViewById(R.id.listView1);
        listView01.setAdapter(new android.widget.BaseAdapter() {
            @Override
            public int getCount() {
                return displayList.size();
            }

            @Override
            public Object getItem(int position) {
                return displayList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.list_item_text, parent, false);
                }

                TextView monthText = convertView.findViewById(R.id.text1);
                TextView costText = convertView.findViewById(R.id.text2);

                String[] item = displayList.get(position);
                monthText.setText(item[0]);
                costText.setText("Final Cost: RM " + item[1]);

                return convertView;
            }
        });

        listView01.setOnItemClickListener((parent, view, position, id) -> {
            cursor.moveToPosition(position);

            Intent intent = new Intent(ListViewActivity.this, ViewActivity.class);
            intent.putExtra("month", cursor.getString(1));
            intent.putExtra("unit", cursor.getFloat(2));
            intent.putExtra("rebate", cursor.getInt(3));
            intent.putExtra("totalCharge", cursor.getFloat(4));
            intent.putExtra("finalCost", cursor.getFloat(5));
            startActivity(intent);
        });
    }



}