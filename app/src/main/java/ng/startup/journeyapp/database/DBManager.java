package ng.startup.journeyapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mungwaagu on 05/11/2018.
 */

public class DBManager {

    private DBHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert_a_goal(String goal_title, String goal_desc, String goal_color,
                       String goal_due_date, String goal_due_date_format, String goal_date_created,
                              String goal_date_created_format, String goal_status) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBHelper.GOAL_TITLE, goal_title);
        contentValue.put(DBHelper.GOAL_DESC, goal_desc);
        contentValue.put(DBHelper.GOAL_COLOR, goal_color);
        contentValue.put(DBHelper.GOAL_DUE_DATE, goal_due_date);
        contentValue.put(DBHelper.GOAL_DUE_DATE_FORMAT, goal_due_date_format);
        contentValue.put(DBHelper.GOAL_DATE_CREATED, goal_date_created);
        contentValue.put(DBHelper.GOAL_DATE_CREATED_FORMAT, goal_date_created_format);
        contentValue.put(DBHelper.GOAL_STATUS, goal_status);
        database.insert(DBHelper.TABLE_GOAL_NAME, null, contentValue);
    }

    public void insert_a_step(String step_title, String step_date_created, String step_date_created_format,
                              String goal_title, String goal_color, String step_journal, String step_status) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBHelper.STEP_TITLE, step_title);
        contentValue.put(DBHelper.STEP_DATE_CREATED, step_date_created);
        contentValue.put(DBHelper.STEP_DATE_CREATED_FORMAT, step_date_created_format);
        contentValue.put(DBHelper.GOAL_TITLE, goal_title);
        contentValue.put(DBHelper.GOAL_COLOR, goal_color);
        contentValue.put(DBHelper.STEP_JOURNAL, step_journal);
        contentValue.put(DBHelper.STEP_STATUS, step_status);
        database.insert(DBHelper.TABLE_STEP_NAME, null, contentValue);
    }

    public void insert_a_streak(String streak_date, int streak_value) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBHelper.STREAK_DATE, streak_date);
        contentValue.put(DBHelper.STREAK_VALUE, streak_value);
        database.insert(DBHelper.TABLE_STREAK_NAME, null, contentValue);
    }

    public Cursor fetch_all_goals() {
        String[] columns = new String[] { DBHelper.GOAL_ID, DBHelper.GOAL_TITLE, DBHelper.GOAL_DESC,
                DBHelper.GOAL_COLOR, DBHelper.GOAL_DUE_DATE, DBHelper.GOAL_DUE_DATE_FORMAT,
                DBHelper.GOAL_DATE_CREATED, DBHelper.GOAL_DATE_CREATED_FORMAT, DBHelper.GOAL_STATUS};
        Cursor cursor = database.query(DBHelper.TABLE_GOAL_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetch_all_steps() {
        String[] columns = new String[] {DBHelper.STEP_ID, DBHelper.STEP_TITLE, DBHelper.STEP_DATE_CREATED,
                DBHelper.STEP_DATE_CREATED_FORMAT, DBHelper.GOAL_TITLE, DBHelper.GOAL_COLOR, DBHelper.STEP_JOURNAL, DBHelper.STEP_STATUS };
        Cursor cursor = database.query(DBHelper.TABLE_STEP_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update_in_goals(int goal_id, String goal_title, String goal_desc, String goal_color,
                               String goal_due_date, String goal_date_created, String goal_status) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBHelper.GOAL_TITLE, goal_title);
        contentValue.put(DBHelper.GOAL_DESC, goal_desc);
        contentValue.put(DBHelper.GOAL_COLOR, goal_color);
        contentValue.put(DBHelper.GOAL_DUE_DATE, goal_due_date);
        contentValue.put(DBHelper.GOAL_DATE_CREATED, goal_date_created);
        contentValue.put(DBHelper.GOAL_STATUS, goal_status);
        int i = database.update(DBHelper.TABLE_GOAL_NAME, contentValue, DBHelper.GOAL_ID + " = " + goal_id, null);
        return i;
    }

    public int update_in_steps(int step_id, String step_title, String step_date_created, String step_date_created_format,
                               String goal_title, String goal_color, String step_journal, String step_status) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBHelper.STEP_TITLE, step_title);
        contentValue.put(DBHelper.STEP_DATE_CREATED, step_date_created);
        contentValue.put(DBHelper.STEP_DATE_CREATED_FORMAT, step_date_created_format);
        contentValue.put(DBHelper.GOAL_TITLE, goal_title);
        contentValue.put(DBHelper.GOAL_COLOR, goal_color);
        contentValue.put(DBHelper.STEP_JOURNAL, step_journal);
        contentValue.put(DBHelper.STEP_STATUS, step_status);
        int i = database.update(DBHelper.TABLE_STEP_NAME, contentValue, DBHelper.STEP_ID + " = " + step_id, null);
        return i;
    }

    public int update_status_in_steps(String step_title, String step_status) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBHelper.STEP_STATUS, step_status);
        int i = database.update(DBHelper.TABLE_STEP_NAME, contentValue, DBHelper.STEP_TITLE + " = '" + step_title + "'", null);
        return i;
    }

    public int update_value_streak(String streak_date, int streak_value) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBHelper.STREAK_VALUE, streak_value);
        int i = database.update(DBHelper.TABLE_STREAK_NAME, contentValue, DBHelper.STREAK_DATE + " = '" + streak_date + "'", null);
        return i;
    }

    public void delete_from_goals(int goal_id) {
        database.delete(DBHelper.TABLE_GOAL_NAME, DBHelper.GOAL_ID + "=" + goal_id, null);
    }

    public void delete_from_steps(int step_id) {
        database.delete(DBHelper.TABLE_STEP_NAME, DBHelper.STEP_ID + "=" + step_id, null);
    }

    //CLOSE DATABASE
    public void closeDB()
    {
        try {
            dbHelper.close();

        }catch (SQLException e)
        {
            e.printStackTrace();
        }


    }

}
