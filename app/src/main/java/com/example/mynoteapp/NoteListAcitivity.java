package com.example.mynoteapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynoteapp.adaptes.NoteAdapter;
import com.example.mynoteapp.objects.Note;
import com.example.mynoteapp.sqlite.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class NoteListAcitivity extends AppCompatActivity {

    private ArrayList<Note> arrayList;
    private NoteAdapter noteAdapter;
    private RecyclerView recyclerView;
    private DatabaseHandler db;
    private FloatingActionButton btnAdd;
    private ActivityResultLauncher<Intent> launcherforAdd;
    private ActivityResultLauncher<Intent> launcherforEdit;
    private int iPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_note);

        db = new DatabaseHandler(this);
        initResultLauncher();
        getAllNotes();
        //su dung recyclerview de hien thi du lieu
        //do du lieu len adapter
        noteAdapter = new NoteAdapter(this, arrayList, new NoteAdapter.ClickListeners() {
            @Override
            public void onItemClick(int position, View v) {
                //gui du lieu sang AddContactActivity de edit du lieu
                iPosition = position;
                Note c = arrayList.get(position);
                //tao intent va gui sang
                Intent i = new Intent(NoteListAcitivity.this, AddNoteActivity.class);
                i.putExtra("contact", c);
                i.putExtra("flag", "1");//danh dau flag la chuc nang sua
                launcherforEdit.launch(i);
            }

            @Override
            public void onItemLongClick(int position, View v) {
                //code here
                String title = arrayList.get(position).get_title();
                Toast.makeText(getBaseContext(), title, Toast.LENGTH_SHORT).show();
                btnAdd.setVisibility(View.VISIBLE);
            }
        });
        //set du lieu cho recyclerview
        recyclerView = findViewById(R.id.rcListContacts);
        recyclerView.setAdapter(noteAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //
        btnAdd = findViewById(R.id.floatingActionButton);
        btnAdd.setOnClickListener(v -> {
            //open add contact activity
            Intent intent = new Intent(NoteListAcitivity.this, AddNoteActivity.class);
            intent.putExtra("flag", "0");//danh dau flag la chuc nang them moi
            launcherforAdd.launch(intent);
        });
        //swipe to delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                // this method is called
                // when the item is moved.
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // this method is called when we swipe our item to right direction.
                // on below line we are getting the item at a particular position.
                Note deletedCourse = arrayList.get(viewHolder.getAdapterPosition());

                // below line is to get the position
                // of the item at that position.
                int position = viewHolder.getAdapterPosition();

                // this method is called when item is swiped.
                // below line is to remove item from our array list.
                arrayList.remove(viewHolder.getAdapterPosition());
                // below line is to notify our item is removed from adapter.
                noteAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                db.deleteNote(deletedCourse.get_id());

                // below line is to display our snackbar with action.
                Snackbar.make(recyclerView, deletedCourse.get_title(), Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // adding on click listener to our action of snack bar.
                        // below line is to add our item to array list with a position.
                        arrayList.add(position, deletedCourse);

                        // below line is to notify item is
                        // added to our adapter class.
                        noteAdapter.notifyItemInserted(position);
                        try {
                            db.addNote(deletedCourse);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).show();
            }
            // at last we are adding this
            // to our recycler view.

        }).attachToRecyclerView(recyclerView);
    }

    private void getAllNotes() {
        try {
            arrayList = new ArrayList<>();
            arrayList = db.getAllNotes();
        } catch (Exception ex) {
            Log.e("getAllContact", ex.getMessage());
        }
    }

    private void initResultLauncher() {
        try {
            launcherforAdd = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result != null && result.getResultCode() == RESULT_OK) {

                    Note c = (Note) result.getData().getSerializableExtra("contact");

                    arrayList.add(0,c);
                    noteAdapter.notifyItemInserted(0);
                }
            });
            launcherforEdit = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {
                //edit data
                if (result != null && result.getResultCode() == RESULT_OK) {

                    Note c = (Note) result.getData().getSerializableExtra("contact");

                    arrayList.set(iPosition, c);
                    noteAdapter.notifyItemChanged(iPosition);
                }
            });
        } catch (Exception ex) {
            Log.e("initResultLauncher", ex.getMessage());
        }
    }

}
