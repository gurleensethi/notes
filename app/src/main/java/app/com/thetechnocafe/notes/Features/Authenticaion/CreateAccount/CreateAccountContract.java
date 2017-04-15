package app.com.thetechnocafe.notes.Features.Authenticaion.CreateAccount;

import app.com.thetechnocafe.notes.BaseApp;

/**
 * Created by gurleensethi on 12/04/17.
 */

public interface CreateAccountContract {
    interface View extends BaseApp.View {
        void showUserAlreadyExitsError();

        void creationSuccessful();
    }

    interface Presenter extends BaseApp.Presenter<CreateAccountContract.View> {
        void createAccount(String username, String password);
    }
}
