package com.example.myapplication;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class ClassSummaryDB extends SQLiteOpenHelper {
	public ClassSummaryDB(Context context) {
		super(context, "ClassSummaryDB.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE lectures  ("
				+ "ID TEXT PRIMARY KEY,"
				+ "course TEXT,"
				+ "type TEXT,"
				+ "datetime INTEGER," // Changed from TEXT to INTEGER for storing date as long
				+ "summary TEXT,"
				+ "topic TEXT,"
				+ "lecture TEXT"
				+ ")";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("Write code to modify database schema here");
	}

	public boolean insertLecture(String ID, String course, String type, long date, String summary, String topic, String lecture) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cols = new ContentValues();
		cols.put("ID", ID);
		cols.put("course", course);
		cols.put("type", type);
		cols.put("datetime", date);
		cols.put("summary", summary);
		cols.put("topic", topic);
		cols.put("lecture", lecture);

		try {
			long result = db.insert("lectures", null, cols);
			return result != -1;
		} catch (Exception e) {
			Log.e("DatabaseError", "Error inserting data: " + e.getMessage());
			return false;
		}
	}

	public boolean updateLecture(String ID, String course, String type, long date, String summary, String topic, String lecture) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("course", course);
		contentValues.put("type", type);
		contentValues.put("datetime", date);
		contentValues.put("summary", summary);
		contentValues.put("topic", topic);
		contentValues.put("lecture", lecture);

		int result = db.update("lectures", contentValues, "ID = ?", new String[]{ID});

		return result > 0;
	}

	public Cursor getData(String classCode) {
		SQLiteDatabase db = this.getReadableDatabase();
		String query = "SELECT * FROM lectures WHERE course = ?";
		return db.rawQuery(query, new String[]{classCode});
	}
}
