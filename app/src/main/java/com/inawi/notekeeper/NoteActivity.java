package com.inawi.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

public class NoteActivity extends AppCompatActivity {

    public final static String NOTE_POSITION = "com.inawi.notekeeper.NOTE_POSITION";
    public static final int POSITION_NOT_SET = -1;
    private NoteInfo mNote;
    private boolean mIsNewNote;
    private Spinner mSpinnerCourses;
    private EditText mTextNoteTitle;
    private EditText mTextNoteText;
    private int mNotePosition;
    private boolean mIsCanceling = false;
    private NoteActivityViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewModelProvider viewModelProvider = new ViewModelProvider(getViewModelStore(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()));
        mViewModel = viewModelProvider.get(NoteActivityViewModel.class);

        mSpinnerCourses = findViewById(R.id.spinner_courses);
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> adapterCourses = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses);
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCourses.setAdapter(adapterCourses);

        readDisplayStateValues();
        saveOriginalNoteValues();

        mTextNoteTitle = (EditText) findViewById(R.id.text_note_title);
        mTextNoteText = (EditText) findViewById(R.id.text_note_text);
        if (!mIsNewNote)
            displayNote(mSpinnerCourses, mTextNoteTitle, mTextNoteText);
    }

    private void saveOriginalNoteValues() {
        if(!mIsNewNote){
            mViewModel.setOriginalNoteCourseId( mNote.getCourse().getCourseId());
            mViewModel.setOriginalNoteTitle( mNote.getTitle());
            mViewModel.setOriginalNoteText( mNote.getText());
        }
    }


    private void readDisplayStateValues() {
        Intent intent = getIntent();
        int position = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);
        mIsNewNote = position == POSITION_NOT_SET;
        if (mIsNewNote) {
            createNewNote();
        } else {
            mNote = DataManager.getInstance().getNotes().get(position);
        }
    }

    private void createNewNote() {
        mNotePosition = DataManager.getInstance().createNewNote();
        mNote = DataManager.getInstance().getNotes().get(mNotePosition);
    }

    private void displayNote(Spinner spinnerCourses, EditText textNoteTitle, EditText textNoteText) {
        int courseIndex = DataManager.getInstance().getCourses().indexOf(mNote.getCourse());
        spinnerCourses.setSelection(courseIndex);
        textNoteTitle.setText(mNote.getTitle());
        textNoteText.setText(mNote.getText());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_send_mail) {
            sendEmail();
            return true;
        } else if (item.getItemId() == R.id.action_cancel) {
            mIsCanceling = true;
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    private void sendEmail() {
        CourseInfo course = (CourseInfo) mSpinnerCourses.getSelectedItem();
        String subject = mTextNoteTitle.getText().toString();
        String text = "Checkout the notes I did for ths course "
                + course.getTitle() + ".\n"
                + mTextNoteText.getText();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);

        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mIsCanceling) {
            if (mIsNewNote) {
                DataManager.getInstance().removeNote(mNotePosition);
            }else{
                restorePreviousNoteValues();
            }
        } else {
            saveNote();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void restorePreviousNoteValues() {
        CourseInfo course = DataManager.getInstance().getCourse(mViewModel.getOriginalNoteCourseId());
        mNote.setCourse(course);
        mNote.setTitle(mViewModel.getOriginalNoteTitle());
        mNote.setText(mViewModel.getOriginalNoteText());
    }

    private void saveNote() {
        int selectedCourseIndex = mSpinnerCourses.getSelectedItemPosition();
        String noteTitle = mTextNoteTitle.getText().toString();
        String noteText = mTextNoteText.getText().toString();

        if (!noteText.isEmpty() && !noteTitle.isEmpty()) {
            CourseInfo course = DataManager.getInstance()
                    .getCourses().get(selectedCourseIndex);
            mNote.setCourse(course);
            mNote.setTitle(noteTitle);
            mNote.setText(noteText);
        }else{
            DataManager.getInstance().removeNote(mNotePosition);
        }
    }
}
