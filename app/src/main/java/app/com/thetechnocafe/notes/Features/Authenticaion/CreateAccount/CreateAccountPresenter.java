package app.com.thetechnocafe.notes.Features.Authenticaion.CreateAccount;

import app.com.thetechnocafe.notes.Data.NotesDatabaseAPI;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by gurleensethi on 12/04/17.
 */

public class CreateAccountPresenter implements CreateAccountContract.Presenter {

    private CreateAccountContract.View mView;
    private CompositeDisposable compositeDisposable;

    @Override
    public void subscribe(CreateAccountContract.View view) {
        this.mView = view;
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
    public void createAccount(String username, String password) {
        NotesDatabaseAPI.getInstance(mView.getAppContext())
                .createNewUser(username, password)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean result) throws Exception {
                        if(result) {
                            mView.creationSuccessful();
                        } else {
                            mView.showUserAlreadyExitsError();
                        }
                    }
                });
    }
}
