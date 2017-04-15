package app.com.thetechnocafe.notes.Features.NotesList;

import java.util.List;

import app.com.thetechnocafe.notes.BaseApp;
import app.com.thetechnocafe.notes.Models.NoteModel;

/**
 * Created by gurleensethi on 12/04/17.
 */

public interface NotesListContract {
    interface View extends BaseApp.View {
        void logoutToSignIn();

        void displayNotes(List<NoteModel> notesList);
    }

    interface Presenter extends BaseApp.Presenter<NotesListContract.View> {
        void logout();

        void deleteAccount();
    }
}
