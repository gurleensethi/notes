package app.com.thetechnocafe.notes.Features.CreateNote;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import app.com.thetechnocafe.notes.Features.MainActivity.ActivityCallback;
import app.com.thetechnocafe.notes.Models.NoteModel;
import app.com.thetechnocafe.notes.R;

/**
 * Created by gurleensethi on 13/04/17.
 */

public class CreateNoteFragment extends Fragment implements CreateNoteContract.View {

    private static final String ARGS_NOTE = "note";
    private boolean isNoteForEditing = false;
    private NoteModel NOTE_MODEL;

    private Toolbar mToolbar;
    private EditText mTitleEditText;
    private EditText mNoteEditText;
    private Button mSaveButton;
    private ActivityCallback mActivityCallback;
    private CreateNoteContract.Presenter mPresenter;

    //Instance method
    public static CreateNoteFragment getInstance(NoteModel noteModel) {
        //Create fragment
        CreateNoteFragment fragment = new CreateNoteFragment();

        //Create arguments and set to fragment
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARGS_NOTE, noteModel);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_note, container, false);

        mActivityCallback = (ActivityCallback) getActivity();

        mPresenter = new CreateNotePresenter();
        mPresenter.subscribe(this);

        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mTitleEditText = (EditText) view.findViewById(R.id.title_edit_text);
        mNoteEditText = (EditText) view.findViewById(R.id.enter_note_edit_text);
        mSaveButton = (Button) view.findViewById(R.id.save_button);

        initViews();

        //Check if a NoteModel is sent in arguments
        //If yes, then set up the view for editing
        NOTE_MODEL = (NoteModel) getArguments().getSerializable(ARGS_NOTE);
        if (NOTE_MODEL != null) {
            isNoteForEditing = true;
            setUpViewForNoteEditing(NOTE_MODEL);
        }

        return view;
    }

    private void initViews() {
        //Set up toolbar
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(mToolbar);
        if (appCompatActivity.getSupportActionBar() != null) {
            appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get the fields
                String title = mTitleEditText.getText().toString().trim();
                String note = mNoteEditText.getText().toString().trim();

                if (validateFields(title, note)) {
                    mPresenter.saveNote(title, note);
                }
            }
        });
    }

    //Set up the Views for editing the existing note
    private void setUpViewForNoteEditing(final NoteModel noteModel) {
        mTitleEditText.setText(noteModel.getTitle());
        mNoteEditText.setText(noteModel.getNote());

        //Change the save button text
        mSaveButton.setText(R.string.update);

        //Update the save button click listener
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get the fields
                String title = mTitleEditText.getText().toString().trim();
                String note = mNoteEditText.getText().toString().trim();

                //Set the new fields in note model
                noteModel.setTitle(title);
                noteModel.setNote(note);

                if (validateFields(title, note)) {
                    mPresenter.updateNote(noteModel);
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        //Check if the view is for editing then create the delete option in toolbar
        if (isNoteForEditing) {
            inflater.inflate(R.menu.menu_create_note, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                if (checkIfNoteSaved()) {
                    mActivityCallback.goBack();
                }
                return true;
            }
            case R.id.delete_note: {
                //Show a alert dialog for confirming deletion
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setMessage("Are you sure you want to delete this note?")
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                mPresenter.deleteNote(NOTE_MODEL);
                            }
                        })
                        .create();

                alertDialog.show();

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //Check if the user and entered something and forgot to save
    //If user hasn't entered something the return true
    //Else return false and show conformation dialog
    public boolean checkIfNoteSaved() {
        if (mNoteEditText.getText().length() == 0 && mTitleEditText.getText().length() == 0) {
            return true;
        }

        //Show confirmation dialog before exiting
        showConfirmationDialog();

        return false;
    }

    //Check if the title and note fields are not empty
    private boolean validateFields(String title, String note) {
        if (title.length() == 0) {
            Snackbar.make(mSaveButton, R.string.title_empty, Snackbar.LENGTH_LONG).show();
            return false;
        }

        if (note.length() == 0) {
            Snackbar.make(mSaveButton, R.string.note_empty, Snackbar.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    @Override
    public Context getAppContext() {
        return getContext();
    }

    @Override
    public void actionSuccessful() {
        closeCreateNoteFragment();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unSubscribe();
    }

    //If the user have any unsaved changes then display an alert box for confirmation
    //before exiting
    public void showConfirmationDialog() {
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setMessage("Unsaved changes will discarded. Do you want to continue?")
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(getString(R.string.string_continue), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        closeCreateNoteFragment();
                    }
                })
                .create();

        dialog.show();
    }

    private void closeCreateNoteFragment() {
        mNoteEditText.setText("");
        mTitleEditText.setText("");

        mActivityCallback.goBack();
    }
}
