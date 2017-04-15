package app.com.thetechnocafe.notes.Features.NotesList;

import java.util.List;

import app.com.thetechnocafe.notes.Data.NotesDatabaseAPI;
import app.com.thetechnocafe.notes.Models.NoteModel;
import app.com.thetechnocafe.notes.Utils.AuthPreferences;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by gurleensethi on 12/04/17.
 */

public class NotesListPresenter implements NotesListContract.Presenter {

    private NotesListContract.View mView;
    private CompositeDisposable compositeDisposable;

    @Override
    public void subscribe(NotesListContract.View view) {
        this.mView = view;

        compositeDisposable = new CompositeDisposable();

        //Load the notes into view
        sendNotesList();
    }

    @Override
    public void unSubscribe() {
        mView = null;

        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }

    @Override
    public void logout() {
        invalidateAuthPreferences();

        //Notify view
        mView.logoutToSignIn();
    }

    @Override
    public void deleteAccount() {
        //Delete account and data from database
        NotesDatabaseAPI.getInstance(mView.getAppContext())
                .deleteAccount(AuthPreferences.getInstance().getUsername(mView.getAppContext()));

        invalidateAuthPreferences();

        //Notify view
        mView.logoutToSignIn();
    }

    //Change the logged in state and remove the username and password
    private void invalidateAuthPreferences() {
        AuthPreferences authPreferences = AuthPreferences.getInstance();
        authPreferences.setLoginStatus(mView.getAppContext(), false);
        authPreferences.setUsername(mView.getAppContext(), null);
        authPreferences.setPassword(mView.getAppContext(), null);
    }

    //Get the list from database and send it to the view
    private void sendNotesList() {
        Disposable disposable = NotesDatabaseAPI.getInstance(mView.getAppContext())
                .getListOfNotes(AuthPreferences.getInstance().getUsername(mView.getAppContext()))
                .subscribe(new Consumer<List<NoteModel>>() {
                    @Override
                    public void accept(List<NoteModel> notesList) throws Exception {
                        mView.displayNotes(notesList);
                    }
                });

        compositeDisposable.add(disposable);
    }
}
