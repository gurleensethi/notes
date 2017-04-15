package app.com.thetechnocafe.notes.Features.MainActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import app.com.thetechnocafe.notes.Features.Authenticaion.Login.LoginFragment;
import app.com.thetechnocafe.notes.Features.CreateNote.CreateNoteFragment;
import app.com.thetechnocafe.notes.Features.NotesList.NotesListFragment;
import app.com.thetechnocafe.notes.R;
import app.com.thetechnocafe.notes.Utils.AuthPreferences;

public class MainActivity extends AppCompatActivity implements ActivityCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check if any fragment is already present in the container
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null) {
            //Check if already logged in
            if (AuthPreferences.getInstance().isLoggedIn(this)) {
                replaceFragment(NotesListFragment.getInstance(), false, false);
            } else {
                replaceFragment(LoginFragment.getInstance(), false, false);
            }
        }
    }

    //Replace the fragment from the fragment container
    @Override
    public void replaceFragment(Fragment fragment, boolean animate, boolean addToStack) {
        //Get the fragment manager
        FragmentManager fragmentManager = getSupportFragmentManager();

        //Get the fragment transactions
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (addToStack) {
            //Add current fragment to back stack
            fragmentTransaction.addToBackStack(fragment.getClass().getName());
        }

        if (animate) {
            //Set custom transition
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        }

        //Replace the current fragment and commit transaction
        fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public void goBack() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        //Check if the current fragment is CreateNotesFragment
        //If yes, then check if the user is pressing back without saving the note
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof CreateNoteFragment) {
            if (!((CreateNoteFragment) fragment).checkIfNoteSaved()) {
                return;
            }
        }

        super.onBackPressed();
    }
}
