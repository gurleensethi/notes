package app.com.thetechnocafe.notes.Models;

import java.io.Serializable;

/**
 * Created by gurleensethi on 14/04/17.
 */

public class NoteModel implements Serializable {
    private int id;
    private String title;
    private String note;
    private String username;
    private long time;

    public NoteModel(String title, String note, String username, long time) {
        this.title = title;
        this.note = note;
        this.username = username;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
