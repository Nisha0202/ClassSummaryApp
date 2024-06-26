package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ClassSummaryAdapter extends ArrayAdapter<ClassSummary> {

    private List<ClassSummary> classSummaries;
    private Context context;

    public ClassSummaryAdapter(Context context, List<ClassSummary> classSummaries) {
        super(context, 0, classSummaries);
        this.context = context;
        this.classSummaries = classSummaries;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        ClassSummary classSummary = classSummaries.get(position);
        TextView courseTextView = listItemView.findViewById(R.id.course_text_view);
        TextView dateTextView = listItemView.findViewById(R.id.date_text_view);
//        TextView typeTextView = listItemView.findViewById(R.id.type_text_view);
//        TextView topicTextView = listItemView.findViewById(R.id.topic_text_view);
//        TextView lectureTextView = listItemView.findViewById(R.id.lecture_text_view);
        TextView summaryTextView = listItemView.findViewById(R.id.summary_text_view);
        ;

        // Set the TextViews using the ClassSummary object
        courseTextView.setText(classSummary.course);
        dateTextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(classSummary.date)));
//        typeTextView.setText(classSummary.type);
//        topicTextView.setText(classSummary.topic);
//        lectureTextView.setText(classSummary.lecture);
        summaryTextView.setText(classSummary.summary);

        return listItemView;
    }
}

