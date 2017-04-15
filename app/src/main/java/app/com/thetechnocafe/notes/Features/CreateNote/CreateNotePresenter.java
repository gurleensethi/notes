package app.com.thetechnocafe.notes.Features.CreateNote;

import java.util.Date;

import app.com.thetechnocafe.notes.Data.NotesDatabaseAPI;
import app.com.thetechnocafe.notes.Models.NoteModel;
import app.com.thetechnocafe.notes.Utils.AuthPreferences;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by gurleensethi on 14/04/17.
 */

public class CreateNotePresenter implements CreateNoteContract.Presenter {

    private CreateNoteContract.View mView;
    private CompositeDisposable compositeDisposable;

    @Override
    public void subscribe(CreateNoteContract.View view) {
        mView = view;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void unSubscribe() {
        mView = null;

        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }

    @Override
    public void saveNote(String title, String note) {
        //Get the username and current time
        String username = AuthPreferences.getInstance().getUsername(mView.getAppContext());
        long time = new Date().getTime();

        //Create a note model
        NoteModel noteModel = new NoteModel(title, note, username, time);

        //Insert into database
        Disposable disposable = NotesDatabaseAPI.getInstance(mView.getAppContext())
                .insertNote(noteModel)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean result) throws Exception {
                        if (result) {
                            mView.actionSuccessful();
                        }
                    }
                });

        compositeDisposable.add(disposable);
    }

    @Override
    public void updateNote(NoteModel noteModel) {
        Disposable disposable = NotesDatabaseAPI.getInstance(mView.getAppContext())
                .updateExistingNote(noteModel)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean result) throws Exception {
                        if(result) {
                            mView.actionSuccessful();
                        }
                    }
                });

        compositeDisposable.add(disposable);
    }

    @Override
    public void deleteNote(NoteModel noteModel) {
        Disposable disposable = NotesDatabaseAPI.getInstance(mView.getAppContext())
                .deleteExistingNote(noteModel.getId())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean result) throws Exception {
                        if(result) {
                            mView.actionSuccessful();
                        }
                    }
                });

        compositeDisposable.add(disposable);
    }
}
