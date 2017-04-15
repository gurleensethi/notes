package app.com.thetechnocafe.notes.Features.Authenticaion.Login;

import app.com.thetechnocafe.notes.Data.NotesDatabaseAPI;
import app.com.thetechnocafe.notes.Utils.AuthPreferences;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by gurleensethi on 12/04/17.
 */

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View mView;
    private CompositeDisposable compositeDisposable;

    @Override
    public void subscribe(LoginContract.View view) {
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
    public void login(final String username, final String password) {
        Disposable disposable = NotesDatabaseAPI.getInstance(mView.getAppContext())
                .checkUserCredentials(username, password)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean result) throws Exception {
                        if (result) {
                            //Save the username, password and state in shared preferences
                            AuthPreferences.getInstance()
                                    .setUsername(mView.getAppContext(), username)
                                    .setPassword(mView.getAppContext(), password)
                                    .setLoginStatus(mView.getAppContext(), true);

                            mView.loginSuccessful();
                        } else {
                            mView.showCredentialsError();
                        }
                    }
                });

        compositeDisposable.add(disposable);
    }
}
