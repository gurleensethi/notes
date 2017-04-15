package app.com.thetechnocafe.notes.Features.CreateNote;

import app.com.thetechnocafe.notes.BaseApp;
import app.com.thetechnocafe.notes.Models.NoteModel;

/**
 * Created by gurleensethi on 14/04/17.
 */

public interface CreateNoteContract {
    interface View extends BaseApp.View {
        void actionSuccessful();
    }

    interface Presenter extends BaseApp.Presenter<CreateNoteContract.View> {
        void saveNote(String title, String note);

        void updateNote(NoteModel noteModel);

        void deleteNote(NoteModel noteModel);
    }
}
