package com.inawi.notekeeper;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.navigation.ui.AppBarConfiguration;

import com.inawi.notekeeper.databinding.ActivityNoteListBinding;

import java.util.List;

public class NoteListActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityNoteListBinding binding;
    private ArrayAdapter<NoteInfo> mNotesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNoteListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);


        initializeDisplayContent();
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NoteListActivity.this,NoteActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mNotesAdapter.notifyDataSetChanged();
    }

    private void initializeDisplayContent() {
        ListView listNotes = (ListView) findViewById(R.id.list_notes);

        List<NoteInfo> notes = DataManager.getInstance().getNotes();
        mNotesAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,notes);

        listNotes.setAdapter(mNotesAdapter);
        listNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("item "+position+" clicked");
                Intent intent = new Intent(NoteListActivity.this,NoteActivity.class);
                //NoteInfo note = (NoteInfo) listNotes.getItemAtPosition(position);
                intent.putExtra(NoteActivity.NOTE_POSITION, position);
                startActivity(intent);
            }
        });

    }

}