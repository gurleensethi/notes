package app.com.thetechnocafe.notes.Features.NotesList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.com.thetechnocafe.notes.Models.NoteModel;
import app.com.thetechnocafe.notes.R;
import app.com.thetechnocafe.notes.Utils.DateFormattingUtils;

/**
 * Created by gurleensethi on 14/04/17.
 */

public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.NotesViewHolder> {

    private Context mContext;
    private List<NoteModel> mNotesList;
    private OnNoteClickListener mListener;

    //Interface for click callbacks
    public interface OnNoteClickListener {
        void onNoteClicked(NoteModel note);
    }

    public NotesRecyclerAdapter(Context context, List<NoteModel> notesList) {
        this.mContext = context;
        this.mNotesList = notesList;
    }

    //View Holder
    class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mNoteTextView;
        private TextView mDateTextView;

        NotesViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.title_text_view);
            mNoteTextView = (TextView) itemView.findViewById(R.id.note_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.date_text_view);
        }

        @Override
        public void onClick(View v) {
            //Check if the listener is provided by the user
            if (mListener != null) {
                mListener.onNoteClicked(mNotesList.get(getAdapterPosition()));
            }
        }

        //Bind the data according the position in the list
        void bindData(int position) {
            //Get the particular note
            NoteModel note = mNotesList.get(position);

            //Set the data
            mTitleTextView.setText(note.getTitle());
            mNoteTextView.setText(note.getNote());

            String timeInString = DateFormattingUtils.getInstance().convertLongToDateString(note.getTime());
            mDateTextView.setText(timeInString);
        }
    }

    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_note_recycler, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotesViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return mNotesList.size();
    }

    //Add OnNoteClickListener
    public void addOnNoteClickListener(OnNoteClickListener listener) {
        this.mListener = listener;
    }

    //Update the existing list
    void updateList(List<NoteModel> notesList) {
        this.mNotesList = notesList;
        notifyDataSetChanged();
    }
}
