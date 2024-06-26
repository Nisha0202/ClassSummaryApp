package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CourseWiseList extends AppCompatActivity {
    Button btnNew, btnback;
    ListView listView;
    ArrayList<String> lecturesList;
    ArrayList<ClassSummary> classSummaries = new ArrayList<>();
    ArrayList<String> idsList = new ArrayList<>();
    ClassSummaryDB db;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_wise_list);
        btnNew = findViewById(R.id.btnNew);
        btnback = findViewById(R.id.btnback);
        listView = findViewById(R.id.listView);
        db = new ClassSummaryDB(this);
        lecturesList = new ArrayList<>();
        sharedPreferences = getSharedPreferences("course", MODE_PRIVATE);
        String classCode = sharedPreferences.getString("course", "");

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewActivity.class);
                startActivity(intent);
            }
        });
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ClassSummeryActivity.class);
                startActivity(intent);
                finish();
            }
        });


        // Get data from database
        Cursor cursor = db.getData(classCode);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No lectures found for " + classCode, Toast.LENGTH_SHORT).show();
            return;
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClassSummary selectedClassSummary = classSummaries.get(position); // Get the selected ClassSummary

                Intent intent = new Intent(getApplicationContext(), ClassSummeryActivity.class);
                intent.putExtra("selectedClassSummary", selectedClassSummary);
                // Pass the ClassSummary to the ClassSummeryActivity


                startActivity(intent);
            }
        });


//        cursor.close();
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String course = cursor.getString(cursor.getColumnIndex("course"));
            @SuppressLint("Range") String type = cursor.getString(cursor.getColumnIndex("type"));
            @SuppressLint("Range") long dateLong = cursor.getLong(cursor.getColumnIndex("datetime"));
            @SuppressLint("Range") String summary = cursor.getString(cursor.getColumnIndex("summary"));
            @SuppressLint("Range") String topic = cursor.getString(cursor.getColumnIndex("topic"));
            @SuppressLint("Range") String lecture = cursor.getString(cursor.getColumnIndex("lecture"));
            @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex("ID")); // Get the ID

            // Create a new ClassSummary object and add it to classSummaries
            ClassSummary classSummary = new ClassSummary(id, course, type, dateLong, summary, topic, lecture);
            classSummaries.add(classSummary);
        }



// Populate classSummaries with data from the database
        ClassSummaryAdapter adapter = new ClassSummaryAdapter(this, classSummaries);
        listView.setAdapter(adapter);


    }


}
