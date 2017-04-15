package app.com.thetechnocafe.notes.Features.Authenticaion.Login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import app.com.thetechnocafe.notes.Features.MainActivity.ActivityCallback;
import app.com.thetechnocafe.notes.Features.Authenticaion.CreateAccount.CreateAccountFragment;
import app.com.thetechnocafe.notes.Features.NotesList.NotesListFragment;
import app.com.thetechnocafe.notes.R;

/**
 * Created by gurleensethi on 12/04/17.
 */

public class LoginFragment extends Fragment implements LoginContract.View {

    private TextInputLayout mUsernameTextInputLayout;
    private TextInputEditText mUsernameTextInputEditText;
    private TextInputLayout mPasswordTextInputLayout;
    private TextInputEditText mPasswordTextInputEditText;
    private Button mCreateAccountButton;
    private Button mLoginButton;
    private LoginContract.Presenter mPresenter;
    private ActivityCallback mActivityCallback;

    //Instance method
    public static LoginFragment getInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mActivityCallback = (ActivityCallback) getActivity();

        //Reference the view from XML
        mUsernameTextInputLayout = (TextInputLayout) view.findViewById(R.id.username_text_input_layout);
        mUsernameTextInputEditText = (TextInputEditText) view.findViewById(R.id.username_text_input_edit_text);
        mPasswordTextInputLayout = (TextInputLayout) view.findViewById(R.id.password_text_input_layout);
        mPasswordTextInputEditText = (TextInputEditText) view.findViewById(R.id.password_text_input_edit_text);
        mLoginButton = (Button) view.findViewById(R.id.login_button);
        mCreateAccountButton = (Button) view.findViewById(R.id.create_account_button);

        mPresenter = new LoginPresenter();
        mPresenter.subscribe(this);

        initViews();

        return view;
    }

    private void initViews() {
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsernameTextInputEditText.getText().toString();
                String password = mPasswordTextInputEditText.getText().toString();

                if (validateFields(username, password)) {
                    mPresenter.login(username, password);
                }
            }
        });

        mCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivityCallback.replaceFragment(CreateAccountFragment.getInstance(), true, true);
            }
        });
    }

    //Check if the fields are not empty
    private boolean validateFields(String username, String password) {
        if (username.length() == 0) {
            Snackbar.make(mCreateAccountButton, "Username cannot be empty", Snackbar.LENGTH_LONG).show();
            return false;
        }

        if (password.length() == 0) {
            Snackbar.make(mCreateAccountButton, "Password cannot be empty", Snackbar.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    @Override
    public Context getAppContext() {
        return getContext();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unSubscribe();
    }

    @Override
    public void showCredentialsError() {
        Snackbar.make(mLoginButton, R.string.username_password_wrong, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void loginSuccessful() {
        mActivityCallback.replaceFragment(NotesListFragment.getInstance(), true, false);
    }
}
