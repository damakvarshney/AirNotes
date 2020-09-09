package com.application.airnotes.Data;

import java.io.Serializable;

public class UserNotes implements Serializable {
    String noteId,noteDate,noteTitle,noteDesc;

    public UserNotes() {
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(String noteDate) {
        this.noteDate = noteDate;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteDesc() {
        return noteDesc;
    }

    public void setNoteDesc(String noteDesc) {
        this.noteDesc = noteDesc;
    }

    public UserNotes(String noteId, String noteDate, String noteTitle, String noteDesc) {
        this.noteId = noteId;
        this.noteDate = noteDate;
        this.noteTitle = noteTitle;
        this.noteDesc = noteDesc;
    }
}
