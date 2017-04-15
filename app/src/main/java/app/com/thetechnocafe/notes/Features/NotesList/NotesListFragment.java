package app.com.thetechnocafe.notes.Features.NotesList;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.com.thetechnocafe.notes.Features.Authenticaion.Login.LoginFragment;
import app.com.thetechnocafe.notes.Features.CreateNote.CreateNoteFragment;
import app.com.thetechnocafe.notes.Features.MainActivity.ActivityCallback;
import app.com.thetechnocafe.notes.Models.NoteModel;
import app.com.thetechnocafe.notes.R;

/**
 * Created by gurleensethi on 12/04/17.
 */

public class NotesListFragment extends Fragment implements NotesListContract.View {

    private static final String TAG = NotesListFragment.class.getSimpleName();

    private Toolbar mToolbar;
    private TextView mNoNotesTextView;
    private RecyclerView mNotesRecyclerView;
    private FloatingActionButton mAddNoteFloatingActionButton;
    private NotesListContract.Presenter mPresenter;
    private ActivityCallback mActivityCallback;
    private NotesRecyclerAdapter mNotesRecyclerAdapter;

    //Instance method
    public static NotesListFragment getInstance() {
        return new NotesListFragment();
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
        View view = inflater.inflate(R.layout.fragment_notes_list, container, false);

        mActivityCallback = (ActivityCallback) getActivity();

        mPresenter = new NotesListPresenter();
        mPresenter.subscribe(this);

        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mNoNotesTextView = (TextView) view.findViewById(R.id.no_notes_text_view);
        mNotesRecyclerView = (RecyclerView) view.findViewById(R.id.notes_recycler_view);
        mAddNoteFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.add_note_floating_action_button);

        initViews();

        return view;
    }

    private void initViews() {
        //Set up the toolbar
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(mToolbar);

        mAddNoteFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivityCallback.replaceFragment(CreateNoteFragment.getInstance(null), true, true);
            }
        });
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_notes_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_account: {
                //Show an AlertDialog to alert the user for action
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setTitle("Are you sure you want to continue?")
                        .setMessage("This will delete all the notes")
                        .setCancelable(true)
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.deleteAccount();
                                dialog.dismiss();
                            }
                        })
                        .create();
                //Show the dialog
                alertDialog.show();
                return true;
            }
            case R.id.logout: {
                mPresenter.logout();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void logoutToSignIn() {
        mActivityCallback.replaceFragment(LoginFragment.getInstance(), false, false);
    }

    @Override
    public void displayNotes(List<NoteModel> notesList) {
        //Check if the list is empty and toggle the visibility accordingly
        if (notesList.size() == 0) {
            mNoNotesTextView.setVisibility(View.VISIBLE);
            mNotesRecyclerView.setVisibility(View.GONE);
        } else {
            mNoNotesTextView.setVisibility(View.GONE);
            mNotesRecyclerView.setVisibility(View.VISIBLE);
            setUpRecyclerView(notesList);
        }
    }

    //Set up the recycler view and adapter if the list is sent for the first time
    //Else refresh the list if new data is sent
    private void setUpRecyclerView(List<NoteModel> notesList) {
        mNotesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mNotesRecyclerAdapter = new NotesRecyclerAdapter(getContext(), notesList);

        //Add click listener when a note is pressed
        mNotesRecyclerAdapter.addOnNoteClickListener(new NotesRecyclerAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClicked(NoteModel note) {
                //Start the create notes fragment for editing
                mActivityCallback.replaceFragment(CreateNoteFragment.getInstance(note), true, true);
            }
        });

        //Set the adapter
        mNotesRecyclerView.setAdapter(mNotesRecyclerAdapter);
    }
}

