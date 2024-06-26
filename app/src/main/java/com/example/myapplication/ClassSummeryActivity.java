package com.example.myapplication;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.EditText;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.widget.Toast;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

public class ClassSummeryActivity extends AppCompatActivity {
    private EditText etCourseId, etDate, etLecture, etTopic, etSum;
     RadioButton lab, theory;
     Button btnSave, btnBack;
    private ClassSummaryDB db;
     SharedPreferences sharedPreferences;
    private String summaryId= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_summery);
        sharedPreferences = getSharedPreferences("course", MODE_PRIVATE);
        String classCode = sharedPreferences.getString("course", "");
        db =  new ClassSummaryDB(this);
        etCourseId = findViewById(R.id.etCourseId);
        if (classCode.length() > 0){
            etCourseId.setText(classCode);}
        etDate = findViewById(R.id.etDate);
        etLecture = findViewById(R.id.etLecture);
        etTopic = findViewById(R.id.etTopic);
        etSum = findViewById(R.id.etSum);
        lab = findViewById(R.id.Lab);
        theory = findViewById(R.id.Theory);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);


        ClassSummary classSummary = (ClassSummary) getIntent().getSerializableExtra("selectedClassSummary");

        if (classSummary != null) {
            // Get the lecture data from the ClassSummary object
            String id = classSummary.id; // Get the ID
            String course = classSummary.course;
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(classSummary.date));
            String type = classSummary.type;
            String topic = classSummary.topic;
            String lecture = classSummary.lecture;
            String summary = classSummary.summary;

            // Set the form fields with the lecture data
            etCourseId.setText(course);
            etDate.setText(date);
            etLecture.setText(lecture); // Set lecture field
            etTopic.setText(topic);
            etSum.setText(summary);
            if (type.equals("Lab")) {
                lab.setChecked(true);
            } else {
                theory.setChecked(true);
            }
            summaryId = id;
        }

        etDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(ClassSummeryActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            month = month + 1;
                            String date = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month, dayOfMonth);
                            etDate.setText(date);
                        }
                    }, year, month, day);

                    datePickerDialog.show();
                }
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String courseId = etCourseId.getText().toString();
                String date = etDate.getText().toString();
                String lecture = etLecture.getText().toString();
                String topic = etTopic.getText().toString();
                String summary = etSum.getText().toString();
                String classType = lab.isChecked() ? "Lab" : "Theory";

                String errorText= "";
                if (lecture.isEmpty()) {
                    errorText = errorText + "\n"+ "Enter Lecture Number ";
                }
                if (topic.isEmpty()) {
                    errorText = errorText + "\n"+ "Enter Topic ";
                }
                if (date.isEmpty()) {
                    errorText = errorText + "\n"+ "Enter Date ";
                }
                if (!isValidDate(date)) {
                    errorText = errorText + "\n"+ "Invalid Date Format. Please use YYYY-MM-DD.";
                }

                if ( !lecture.isEmpty() && !topic.isEmpty() && !date.isEmpty() && isValidDate(date)  ) {
                    long dateLong = convertDateToLong(date);

                    String successText = "Student Info: "+ '\n' + "Topic: " + topic + '\n'+
                            "Lecture number: " + lecture + '\n' + "Course Code: " + courseId + '\n' + "Class Type: " + classType;
                    if(summaryId.isEmpty()){
                        summaryId = UUID.randomUUID().toString();
                        boolean insertSuccess = db.insertLecture(summaryId, courseId, classType, dateLong,  summary, topic, lecture);
                        if (insertSuccess) {
                            showSuccessDialog(successText);
                            etCourseId.setText("");
                            etDate.setText("");
                            etLecture.setText("");
                            etTopic.setText("");
                            etSum.setText("");
                            if (lab.isChecked()) {
                                lab.setChecked(false);
                            } else {
                                theory.setChecked(false);
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(getApplicationContext(), CourseWiseList.class);
                                    startActivity(intent);

                                    finish();
                                }
                            }, 3000);

//                            String keys[] = {"action", "sid", "semester", "id", "course", "type", "topic", "date", "lecture", "summary"};
//                            String values[] = {"backup", "2020-1-60-123", "2024-1", summaryId, courseId, classType, topic, date, lecture, summary};
//
//                            httpRequest(keys, values);

                        } else {
                            showErrorDialog("Failed to insert data into the database.");
                        }
                    }else {
                        boolean updateSuccess = db.updateLecture(summaryId, courseId, classType, dateLong,  summary, topic, lecture);
                        if (updateSuccess) {
                            showSuccessDialog("Data updated successfully.");
                            etCourseId.setText("");
                            etDate.setText("");
                            etLecture.setText("");
                            etTopic.setText("");
                            etSum.setText("");
                            if (lab.isChecked()) {
                                lab.setChecked(false);
                            } else {
                                theory.setChecked(false);
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(getApplicationContext(), CourseWiseList.class);
                                    startActivity(intent);

                                    finish();
                                }
                            }, 3000);
                        } else {
                            showErrorDialog("Failed to update data in the database.");
                        }
                    }
                    String keys[] = {"action", "sid", "semester", "id", "course", "type", "topic", "date", "lecture", "summary"};
                    String values[] = {"backup", "2020-1-60-123", "2024-1", summaryId, courseId, classType, topic, date, lecture, summary};

                    httpRequest(keys, values);
                } else {
                    errorText = errorText + "\n"+ "Invalid Information";
                    showErrorDialog(errorText);
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClassSummeryActivity.this, CourseWiseList.class);
                startActivity(intent);}
        });
    }
//    Methods
@SuppressLint("StaticFieldLeak")
private void httpRequest(final String keys[], final String values[]){
    new AsyncTask<Void,Void,String>(){
        @Override
        protected String doInBackground(Void... voids) {
            List<NameValuePair> params=new ArrayList<NameValuePair>();
            for (int i=0; i<keys.length; i++){
                params.add(new BasicNameValuePair(keys[i],values[i]));
            }
            String url= "https://www.muthosoft.com/univ/cse489/index.php";
            try {
                String data= RemoteAccess.getInstance().makeHttpRequest(url,"POST",params);
                Log.d("HTTP Request", "Data: " + data);  // Add this line
                return data;
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("HTTP Request", "Exception: " + e.getMessage());  // And this line
            }
            return null;
        }
        protected void onPostExecute(String data){
            if(data!=null){
                Toast.makeText(getApplicationContext(),data,Toast.LENGTH_LONG).show();
            }
        }
    }.execute();
}









    // Method to validate date format
    public boolean isValidDate(String date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
    //dialogs
    public void showSuccessDialog(String successMsg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ClassSummeryActivity.this);
        builder.setMessage(successMsg);
        builder.setTitle("SUCCESS");
        builder.setCancelable(true);

        builder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public void showErrorDialog(String errorMsg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ClassSummeryActivity.this);
        builder.setMessage(errorMsg);
        builder.setTitle("ERROR");
        builder.setCancelable(true);

        builder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public long convertDateToLong(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(dateStr);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }


}
