package app.com.thetechnocafe.notes.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import app.com.thetechnocafe.notes.Models.NoteModel;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gurleensethi on 12/04/17.
 */

public class NotesDatabaseAPI {
    private static final String TAG = NotesDatabaseAPI.class.getSimpleName();
    private static NotesDatabaseAPI sInstance;
    private NotesDatabaseHelper mDatabaseHelper;

    //Singleton class
    private NotesDatabaseAPI(Context context) {
        mDatabaseHelper = new NotesDatabaseHelper(context);
    }

    //Instance method
    public static NotesDatabaseAPI getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new NotesDatabaseAPI(context);
        }
        return sInstance;
    }

    /**
     * Check if the user exist in database
     * Return an RxJava observable that notifies with a boolean
     * true means user exists, false means user doesn't exists
     *
     * @param username Username entered by the user
     * @param password Password entered by the user
     * @return Observable<Boolean>
     */
    public Observable<Boolean> checkUserCredentials(final String username, final String password) {
        Observable<Boolean> observable = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                //Get the readable database
                SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();

                //SQL query to check if a particular user with the username and password exist
                String userSelectionSQL = "SELECT * FROM " + NotesDatabaseColumns.UserTable.TABLE_NAME
                        + " WHERE " + NotesDatabaseColumns.UserTable.USERNAME + " = ? AND "
                        + NotesDatabaseColumns.UserTable.PASSWORD + " = ?";

                //Run the query and get the cursor
                Cursor cursor = database.rawQuery(userSelectionSQL, new String[]{username, password});

                //Get the total count of results
                int count = cursor.getCount();

                //Close the cursor and database
                cursor.close();
                database.close();

                //If the count is more than one than notify the subscriber that user exits
                //else send false
                if (count > 0) {
                    emitter.onNext(true);
                } else {
                    emitter.onNext(false);
                }
            }
        });

        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Create a new account
     * Check if the username already exists
     * true means user created, false means user already exists
     *
     * @param username Username entered by the user
     * @param password Password entered by the user
     * @return Observable<Boolean>
     */
    public Observable<Boolean> createNewUser(final String username, final String password) {
        Observable<Boolean> observable = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                //Get the readable database
                SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();

                //SQL query to check if a particular user with the username exists
                String userSelectionSQL = "SELECT * FROM " + NotesDatabaseColumns.UserTable.TABLE_NAME
                        + " WHERE " + NotesDatabaseColumns.UserTable.USERNAME + " = ?";

                //Run the query and get the cursor
                Cursor cursor = database.rawQuery(userSelectionSQL, new String[]{username});

                //Get the total count of results
                int count = cursor.getCount();

                //Close the cursor
                cursor.close();

                //If the count is more than one than notify the subscriber that user exits
                //else create the new user
                if (count > 0) {
                    emitter.onNext(false);
                } else {
                    //Create content values with required data
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(NotesDatabaseColumns.UserTable.USERNAME, username);
                    contentValues.put(NotesDatabaseColumns.UserTable.PASSWORD, password);

                    database.insert(NotesDatabaseColumns.UserTable.TABLE_NAME, null, contentValues);

                    //Notify user creation successful
                    emitter.onNext(true);
                }

                //Close database
                database.close();
            }
        });

        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Delete an existing account
     * Delete all the notes corresponding to the existing account
     *
     * @param username Username of the currently logged in user
     */
    public void deleteAccount(final String username) {
        Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(ObservableEmitter<Void> e) throws Exception {
                //Get the readable database
                SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();

                //Delete all the notes for the corresponding user from the note's table
                int result = database.delete(
                        NotesDatabaseColumns.NotesTable.TABLE_NAME,
                        NotesDatabaseColumns.NotesTable.USERNAME + " = ?",
                        new String[]{username}
                );

                //Delete the user from the user's table
                result = database.delete(
                        NotesDatabaseColumns.UserTable.TABLE_NAME,
                        NotesDatabaseColumns.UserTable.USERNAME + " = ?",
                        new String[]{username}
                );

                //Close database
                database.close();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    /**
     * Insert new note for the user that is logged in
     *
     * @param note NoteModel with all the details of the note to be inserted
     * @return Observable<Boolean>
     */
    public Observable<Boolean> insertNote(final NoteModel note) {
        Observable<Boolean> observable = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                //Get the readable database
                SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();

                //Create content values
                ContentValues contentValues = new ContentValues();
                contentValues.put(NotesDatabaseColumns.NotesTable.USERNAME, note.getUsername());
                contentValues.put(NotesDatabaseColumns.NotesTable.TITLE, note.getTitle());
                contentValues.put(NotesDatabaseColumns.NotesTable.NOTE, note.getNote());
                contentValues.put(NotesDatabaseColumns.NotesTable.DATE, String.valueOf(note.getTime()));

                //Insert into database
                database.insert(NotesDatabaseColumns.NotesTable.TABLE_NAME, null, contentValues);

                //Close database
                database.close();

                //Notify subscriber
                emitter.onNext(true);
            }
        });

        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Return the list of notes created by a single user in an observable
     *
     * @param username Username of the currently logged in user
     * @return Observable<List<NoteModel>>
     */
    public Observable<List<NoteModel>> getListOfNotes(final String username) {
        Observable<List<NoteModel>> observable = Observable.create(new ObservableOnSubscribe<List<NoteModel>>() {
            @Override
            public void subscribe(ObservableEmitter<List<NoteModel>> emitter) throws Exception {
                //Get the readable database
                SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();

                //Create an empty notes list
                List<NoteModel> notesList = new ArrayList<>();

                //Run a database query to get all the rows with the particular username
                Cursor cursor = database.query(
                        NotesDatabaseColumns.NotesTable.TABLE_NAME,
                        new String[]{
                                NotesDatabaseColumns.NotesTable.ID,
                                NotesDatabaseColumns.NotesTable.TITLE,
                                NotesDatabaseColumns.NotesTable.NOTE,
                                NotesDatabaseColumns.NotesTable.USERNAME,
                                NotesDatabaseColumns.NotesTable.DATE
                        },
                        NotesDatabaseColumns.NotesTable.USERNAME + " = ?",
                        new String[]{username},
                        null,
                        null,
                        null
                );

                //Iterate over the cursor and retrieve the values
                while (cursor.moveToNext()) {
                    String title = cursor.getString(cursor.getColumnIndex(NotesDatabaseColumns.NotesTable.TITLE));
                    String note = cursor.getString(cursor.getColumnIndex(NotesDatabaseColumns.NotesTable.NOTE));
                    int id = cursor.getInt(cursor.getColumnIndex(NotesDatabaseColumns.NotesTable.ID));
                    String date = cursor.getString(cursor.getColumnIndex(NotesDatabaseColumns.NotesTable.DATE));

                    Log.d(TAG, title);

                    //Create a new NoteModel
                    NoteModel noteModel = new NoteModel(title, note, username, Long.parseLong(date));
                    noteModel.setId(id);

                    //Add to list
                    notesList.add(noteModel);
                }

                //Close cursor and database
                cursor.close();
                database.close();

                //Sort the notes list according to time
                Collections.sort(notesList, new Comparator<NoteModel>() {
                    @Override
                    public int compare(NoteModel o1, NoteModel o2) {
                        if (o1.getTime() > o2.getTime()) {
                            return -1;
                        } else if (o1.getTime() < o2.getTime()) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                });

                //Send the list to subscriber
                emitter.onNext(notesList);
            }
        });

        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Update the existing note with the id provided in the notemodel
     *
     * @param note NoteModel with the new updated content
     * @return Observable<Boolean>
     */
    public Observable<Boolean> updateExistingNote(final NoteModel note) {
        Observable<Boolean> observable = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                //Get the readable database
                SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();

                //Create content values
                ContentValues contentValues = new ContentValues();
                contentValues.put(NotesDatabaseColumns.NotesTable.USERNAME, note.getUsername());
                contentValues.put(NotesDatabaseColumns.NotesTable.TITLE, note.getTitle());
                contentValues.put(NotesDatabaseColumns.NotesTable.NOTE, note.getNote());
                contentValues.put(NotesDatabaseColumns.NotesTable.DATE, String.valueOf(note.getTime()));

                //Update the item
                database.update(
                        NotesDatabaseColumns.NotesTable.TABLE_NAME,
                        contentValues, NotesDatabaseColumns.NotesTable.ID + " = ? ",
                        new String[]{String.valueOf(note.getId())}
                );

                //Close database
                database.close();

                //Notify subscriber
                emitter.onNext(true);
            }
        });

        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Delete a note with particular note id
     *
     * @param noteID ID of the note to be deleted
     * @return Observable<Boolean>
     */
    public Observable<Boolean> deleteExistingNote(final int noteID) {
        Observable<Boolean> observable = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                //Get the readable database
                SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();

                //Delete the note with particular id
                database.delete(
                        NotesDatabaseColumns.NotesTable.TABLE_NAME,
                        NotesDatabaseColumns.NotesTable.ID + " = ? ",
                        new String[]{String.valueOf(noteID)}
                );

                //Close database
                database.close();

                //Notify subscriber
                emitter.onNext(true);
            }
        });

        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
