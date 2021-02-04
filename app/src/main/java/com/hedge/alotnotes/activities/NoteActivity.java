package com.hedge.alotnotes.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hedge.alotnotes.R;
import com.hedge.alotnotes.adapters.NoteAdapter;
import com.hedge.alotnotes.models.Board;
import com.hedge.alotnotes.models.Note;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;


public class NoteActivity extends AppCompatActivity implements RealmChangeListener<Board> {

    private ListView listView;
    private FloatingActionButton fab;

    private NoteAdapter adapter;
    private RealmList<Note> notes;
    private Realm realm;

    private int boardID;
    private Board board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        realm = Realm.getDefaultInstance();

        if(getIntent().getExtras()!=null)
            boardID = getIntent().getExtras().getInt("id");
        board = realm.where(Board.class).equalTo("id",boardID).findFirst();

        board.addChangeListener(this);

        notes = board.getNotes();

        this.setTitle(board.getTitle());

        fab = (FloatingActionButton)findViewById(R.id.fabAddNote);
        listView = (ListView)findViewById(R.id.listViewNote);
        adapter = new NoteAdapter(this,notes,R.layout.list_view_note_item);
        listView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertForCreatingNote("Add new note","Type a note next");
            }
        });

        registerForContextMenu(listView);

    }

    //AlertDialog para el fab
    private void showAlertForCreatingNote (String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if(title!=null)builder.setTitle(title);
        if(message!=null)builder.setMessage(message);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_note,null);
        builder.setView(viewInflated);

        final EditText input = (EditText) viewInflated.findViewById(R.id.edtiTextNewNote);

        builder.setPositiveButton("add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String note = input.getText().toString().trim();
                if(note.length()>0)
                    createNewBoard(note);
                else
                    Toast.makeText(getApplicationContext(),"The note can't be empty",Toast.LENGTH_LONG).show();
            }
        });

        builder.create().show();
    }


    /***
     * CRUD Note
     * ****/
    private void createNewBoard(String note) {
        realm.beginTransaction();
        Note _note = new Note(note);
        realm.copyToRealm(_note);
        board.getNotes().add(_note);
        realm.commitTransaction();

    }

    private void editNote(String newName,Note note){
        realm.beginTransaction();
        note.setDescription(newName);
        realm.copyToRealmOrUpdate(note);
        realm.commitTransaction();
        adapter.notifyDataSetChanged();

    }

    private void deleteNote(Note note){
        realm.beginTransaction();
        note.deleteFromRealm();
        realm.commitTransaction();
    }

    private void deleteAll(){
        realm.beginTransaction();
        board.getNotes().clear();
        realm.copyToRealmOrUpdate(board);
        realm.commitTransaction();
    }


    //Alert dialog para editar
    private void showAlertForEditingNote (String title, String message,final Note note){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if(title!=null)builder.setTitle(title);
        if(message!=null)builder.setMessage(message);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_note,null);
        builder.setView(viewInflated);

        final EditText input = (EditText) viewInflated.findViewById(R.id.edtiTextNewNote);
        input.setText(note.getDescription());

        builder.setPositiveButton("save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newNote = input.getText().toString().trim();
                if (newNote.length() == 0){
                    Toast.makeText(getApplicationContext(), "The name is required to edit the current note", Toast.LENGTH_LONG).show();
                }else if(newNote.equals(note.getDescription())) {
                    Toast.makeText(getApplicationContext(), "The name is same than it was before", Toast.LENGTH_LONG).show();
                }else{
                    editNote(newNote,note);
                }

            }
        });

        builder.create().show();
    }

    /* Events */

    //Options Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_activity,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all_notes:
                deleteAll();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Context Menu

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu_note_activity,menu);

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.delete_note:
                deleteNote(notes.get(info.position));
                return true;
            case R.id.edit_note:
                showAlertForEditingNote("Edit note","Change the description of the note",notes.get(info.position));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onChange(Board board) {
        adapter.notifyDataSetChanged();
    }


}