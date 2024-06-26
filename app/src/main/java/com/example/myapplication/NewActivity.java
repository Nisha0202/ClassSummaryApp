package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private Button btnExitt, btnChoose;   TextView cse405, cse489, cse412, cse495;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
            sharedPreferences = getSharedPreferences("course", MODE_PRIVATE);

             cse405 = findViewById(R.id.cse405);
            cse489 = findViewById(R.id.cse489);
            cse412 = findViewById(R.id.cse412);
             cse495 = findViewById(R.id.cse495);
        btnExitt = findViewById(R.id.btnExitt);
        btnChoose = findViewById(R.id.btnChoose);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCourseDialog();
            }
        });

            btnExitt.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                Intent intent = new Intent(NewActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

        }});

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView = (TextView) v;
                    String course = textView.getText().toString();

                    // Save the course name in SharedPreferences
                    sharedPreferences.edit().putString("course", course).apply();

                    // Navigate to ClassSummaryListActivity
                    Intent intent = new Intent(NewActivity.this, CourseWiseList.class);
                    startActivity(intent);
                }
            };

            cse405.setOnClickListener(clickListener);
            cse489.setOnClickListener(clickListener);
            cse412.setOnClickListener(clickListener);
            cse495.setOnClickListener(clickListener);
        }

    public void onStart(){
        super.onStart();
        super.onStart();
        String keys[] = {"action", "sid", "semester"};
        String values[] = {"restore", "2020-1-60-123", "2024-1"};
        httpRequest(keys, values);

    }
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
                    return data;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
            protected void onPostExecute(String data){
                if(data!=null){
                    updateLocalDBByServerData(data);
                }
            }
        }.execute();
    }
    private void updateLocalDBByServerData (String data){
        ClassSummaryDB db =  new ClassSummaryDB(NewActivity.this);
        System.out.println("found");
        try{
            JSONObject jo = new JSONObject(data);
            if(jo.has("classes")){
                JSONArray ja = jo.getJSONArray("classes");
                for(int i=0; i<ja.length(); i++){
                    JSONObject summary = ja.getJSONObject(i);
                    String id = summary.getString("id");
                    String course = summary.getString("course");
                    String topic = summary.getString("topic");
                    String type = summary.getString("type");
                    long date = summary.getLong("date");
                    String lecture = summary.getString("lecture");
                    String sum = summary.getString("summary");

                    try {

                        db.insertLecture(id, course, type, date,  sum, topic, lecture);
                    }catch(Exception e){}
                }
            }
        }catch(Exception e){}
    }
    private void showCourseDialog() {
        final EditText[] courseInputs = new EditText[4];
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Course");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        for (int i = 0; i < 4; i++) {
            courseInputs[i] = new EditText(this);
            courseInputs[i].setHint("Course Code " + (i + 1));

            //a horizontal margin to the EditText
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.leftMargin = 40;
            params.rightMargin = 40;
            courseInputs[i].setLayoutParams(params);

            layout.addView(courseInputs[i]);
        }

        builder.setView(layout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String course1 = courseInputs[0].getText().toString();
                String course2 = courseInputs[1].getText().toString();
                String course3 = courseInputs[2].getText().toString();
                String course4 = courseInputs[3].getText().toString();

                //the text of the course buttons
                cse405.setText(course1);
                cse489.setText(course2);
                cse412.setText(course3);
                cse495.setText(course4);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    }
