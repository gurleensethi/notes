package app.com.thetechnocafe.notes.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by gurleensethi on 12/04/17.
 */

public class NotesDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = NotesDatabaseHelper.class.getSimpleName();
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "notes_database";

    public NotesDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //SQL query to create user table
        String userSQL = "CREATE TABLE " + NotesDatabaseColumns.UserTable.TABLE_NAME
                + "(" + NotesDatabaseColumns.UserTable.USERNAME + " VARCHAR PRIMARY KEY, "
                + NotesDatabaseColumns.UserTable.PASSWORD + " VARCHAR);";

        //SQL query to create notes table
        String notesSQL = "CREATE TABLE " + NotesDatabaseColumns.NotesTable.TABLE_NAME
                + "(" + NotesDatabaseColumns.NotesTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + NotesDatabaseColumns.NotesTable.USERNAME + " varchar, "
                + NotesDatabaseColumns.NotesTable.TITLE + " varchar, "
                + NotesDatabaseColumns.NotesTable.NOTE + " varchar, "
                + NotesDatabaseColumns.NotesTable.DATE + " varchar);";

        Log.d(TAG, "Creating User Table : " + userSQL);
        Log.d(TAG, "Creating Notes Table : " + notesSQL);

        //Create the tables
        db.execSQL(userSQL);
        db.execSQL(notesSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
