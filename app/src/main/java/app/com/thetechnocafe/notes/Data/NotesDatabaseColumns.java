package app.com.thetechnocafe.notes.Data;

/**
 * Created by gurleensethi on 12/04/17.
 */

public class NotesDatabaseColumns {

    //Columns in the user table
    public static class UserTable {
        public static final String TABLE_NAME = "users";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
    }

    //Columns in the notes table
    public static class NotesTable {
        public static final String TABLE_NAME = "notes";
        public static final String ID = "id";
        public static final String USERNAME = "username";
        public static final String TITLE = "title";
        public static final String NOTE = "note";
        public static final String DATE = "date";
    }

}
