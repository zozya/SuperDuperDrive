package com.udacity.jwdnd.course1.cloudstorage.form;

public class NoteForm {

    private String noteId;
    private String noteTitle;
    private String noteDescription;

    public String getNoteId() {
        return noteId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public String getNoteDescription() {
        return noteDescription;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public void setNoteDescription(String noteDescription) {
        this.noteDescription = noteDescription;
    }

}
