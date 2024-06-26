package com.example.myapplication;

import java.io.Serializable;

public class ClassSummary implements Serializable {
    String id = "";
    String course = "";
    String type = "";
    long date;
    String summary = "";
    String topic = "";
    String lecture = "";

    public ClassSummary(String id, String course, String type, long date, String summary, String topic, String lecture){
        this.id = id;
        this.course = course;
        this.type = type;
        this.date = date;
        this.summary = summary;
        this.topic = topic;
        this.lecture = lecture;
    }
}





//public class ClassSummary {
//    String id = "";
//    String course = "";
//    String type = "";
//    long date; // Changed from String to long
//    String summary = "";
//    String topic = "";
//    String lecture = ""; // Added new field for lecture
//
//    public ClassSummary(String id, String course, String type, long date, String summary, String topic, String lecture){
//        this.id = id;
//        this.course = course;
//        this.type = type;
//        this.date = date; // Changed from String to long
//        this.summary = summary;
//        this.topic = topic;
//        this.lecture = lecture; // Added new value for lecture
//    }
//}

