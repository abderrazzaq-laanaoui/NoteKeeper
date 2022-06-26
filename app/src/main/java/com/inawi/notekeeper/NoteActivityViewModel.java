package com.inawi.notekeeper;

import androidx.lifecycle.ViewModel;

public class NoteActivityViewModel extends ViewModel {

    private String mOriginalNoteCourseId;
    private String mOriginalNoteTitle;
    private String mOriginalNoteText;

    public String getOriginalNoteCourseId() {
        return mOriginalNoteCourseId;
    }

    public void setOriginalNoteCourseId(String originalNoteCourseId) {
        mOriginalNoteCourseId = originalNoteCourseId;
    }

    public String getOriginalNoteTitle() {
        return mOriginalNoteTitle;
    }

    public void setOriginalNoteTitle(String originalNoteTitle) {
        mOriginalNoteTitle = originalNoteTitle;
    }

    public String getOriginalNoteText() {
        return mOriginalNoteText;
    }

    public void setOriginalNoteText(String originalNoteText) {
        mOriginalNoteText = originalNoteText;
    }
}
