package ng.startup.journeyapp.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mungwaagu on 05/11/2018.
 */

public class DBHelper extends SQLiteOpenHelper {

    // Goals Table Name
    public static final String TABLE_GOAL_NAME = "GOALS";

    //bSteps Table Name
    public static final String TABLE_STEP_NAME = "STEPS";

    //bSteps Table Name
    public static final String TABLE_STREAK_NAME = "STREAKS";

    // Goals Table columns
    public static final String GOAL_ID = "_id";
    public static final String GOAL_TITLE = "goal_title";
    public static final String GOAL_DESC = "goal_description";
    public static final String GOAL_COLOR = "goal_color";
    public static final String GOAL_DUE_DATE = "goal_due_date";
    public static final String GOAL_DUE_DATE_FORMAT = "goal_due_date_format";
    public static final String GOAL_DATE_CREATED = "goal_date_created";
    public static final String GOAL_DATE_CREATED_FORMAT = "goal_date_created_format";
    public static final String GOAL_STATUS = "goal_status";

    // Steps Table columns
    public static final String STEP_ID = "_id";
    public static final String STEP_TITLE = "step_title";
    public static final String STEP_DATE_CREATED = "step_date_created";
    public static final String STEP_DATE_CREATED_FORMAT = "step_date_created_format";
    public static final String STEP_JOURNAL = "step_journal";
    public static final String STEP_STATUS = "step_status";

    // Streak Table columns
    public static final String STREAK_ID = "_id";
    public static final String STREAK_DATE = "streak_date";
    public static final String STREAK_VALUE = "streak_value";

    // Database Information
    static final String DB_NAME = "JOURNEY_GOALS_STEPS.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_GOAL_TABLE = "create table " + TABLE_GOAL_NAME + "(" + GOAL_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + GOAL_TITLE + " VARCHAR NOT NULL UNIQUE, "
            + GOAL_DESC + " VARCHAR, "
            + GOAL_COLOR + " VARCHAR, "
            + GOAL_DUE_DATE + " VARCHAR, "
            + GOAL_DUE_DATE_FORMAT + " VARCHAR, "
            + GOAL_DATE_CREATED + " VARCHAR, "
            + GOAL_DATE_CREATED_FORMAT + " VARCHAR, "
            + GOAL_STATUS + " VARCHAR);";


    // Creating table query
    private static final String CREATE_STEP_TABLE = "create table " + TABLE_STEP_NAME + "(" + STEP_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + STEP_TITLE + " VARCHAR NOT NULL UNIQUE, "
            + STEP_DATE_CREATED + " VARCHAR, "
            + STEP_DATE_CREATED_FORMAT + " VARCHAR, "
            + GOAL_TITLE + " VARCHAR, "
            + GOAL_COLOR + " VARCHAR, "
            + STEP_JOURNAL + " VARCHAR, "
            + STEP_STATUS + " VARCHAR);";

    // Creating table query
    private static final String CREATE_STREAK_TABLE = "create table " + TABLE_STREAK_NAME + "(" + STREAK_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + STREAK_DATE + " VARCHAR NOT NULL UNIQUE, "
            + STREAK_VALUE + " INTEGER);";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_GOAL_TABLE);
        db.execSQL(CREATE_STEP_TABLE);
        db.execSQL(CREATE_STREAK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GOAL_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STEP_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STREAK_NAME);
        onCreate(db);
    }

    public List<String> getAllLabels(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT goal_title FROM GOALS WHERE "+DBHelper.GOAL_STATUS+" NOT LIKE '%Discard%' ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public List<String> getDatesPending(String goal_title){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT "+ DBHelper.STEP_DATE_CREATED_FORMAT+" FROM STEPS WHERE "+
                DBHelper.GOAL_TITLE+" = '" + goal_title + "' AND " + DBHelper.STEP_STATUS + " LIKE '%Pending%'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public List<String> getDatesDone(String goal_title){

        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT "+ DBHelper.STEP_DATE_CREATED_FORMAT+" FROM STEPS WHERE "+
                DBHelper.GOAL_TITLE+" = '" + goal_title + "' AND " + DBHelper.STEP_STATUS + " LIKE '%Done%'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    //RETRIEVE
    public String getColor(String goal_title) {

        String labels = "";

        // Select All Query
        String selectQuery = "SELECT goal_color FROM GOALS WHERE goal_title = '" + goal_title +"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;

    }

    public Cursor getStepsOnDate(String on_date, String goal_title) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from STEPS WHERE step_status != 'Discard' " +
                "AND "+ DBHelper.STEP_DATE_CREATED_FORMAT +" = '"+ on_date + "' AND "+
                DBHelper.GOAL_TITLE+ " = '"+goal_title+"'", null );
        return res;
    }

    public Cursor getStepsOnDate(String on_date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from STEPS WHERE step_status != 'Discard' " +
                "AND "+ DBHelper.STEP_DATE_CREATED_FORMAT +" = '"+ on_date + "'", null );
        return res;
    }

    public Cursor getGoals() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from GOALS WHERE goal_status != 'Discard' ", null );
        return res;
    }

    public Cursor getStepsFromGoal(String goal_title) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from STEPS WHERE step_status != 'Discard' AND "+
                DBHelper.GOAL_TITLE+ " = '"+goal_title+"'", null );
        return res;
    }

    public String getStatsGoals() {

        String labels = "";

        // Select All Query
        String goalsQuery = "SELECT count(*) FROM GOALS WHERE goal_status != 'Discard'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(goalsQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;

    }

    public String getStatsSteps() {

        String labels = "";

        // Select All Query
        String goalsQuery = "SELECT count(*) FROM STEPS WHERE step_status != 'Discard'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(goalsQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;

    }

    public String getNoStepsforGoal(String goal_title) {

        String labels = "";

        // Select All Query
        String goalsQuery = "SELECT count(*) FROM STEPS WHERE step_status != 'Discard' AND goal_title = '"+ goal_title + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(goalsQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;

    }

    public List<String> getStreakDates() {

        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT streak_date FROM "+ DBHelper.TABLE_STREAK_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public List<String> getStreakDateStatus(String streak_date) {

        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT step_status FROM "+ DBHelper.TABLE_STEP_NAME + " WHERE "+
                DBHelper.STEP_DATE_CREATED_FORMAT + " = '"+streak_date+"' AND "+
                DBHelper.STEP_STATUS+" LIKE '%Pending%'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }


    public String getStreaks() {

        String labels = "";

        // Select All Query
        String goalsQuery = "SELECT count(*) FROM STREAKS WHERE streak_value = 1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(goalsQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;

    }

}
