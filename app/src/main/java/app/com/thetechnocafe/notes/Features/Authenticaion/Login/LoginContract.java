package app.com.thetechnocafe.notes.Features.Authenticaion.Login;

import app.com.thetechnocafe.notes.BaseApp;

/**
 * Created by gurleensethi on 12/04/17.
 */

public interface LoginContract {
    interface View extends BaseApp.View {
        void showCredentialsError();

        void loginSuccessful();
    }

    interface Presenter extends BaseApp.Presenter<LoginContract.View> {
        void login(String username, String password);
    }
}
