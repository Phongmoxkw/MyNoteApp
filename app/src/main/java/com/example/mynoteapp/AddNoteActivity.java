package com.example.mynoteapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mynoteapp.adaptes.NoteAdapter;
import com.example.mynoteapp.objects.Note;
import com.example.mynoteapp.sqlite.DatabaseHandler;

public class AddNoteActivity extends AppCompatActivity {
    private Button btnCancel;
    private ImageButton btnSave;
    private EditText txtTitle, txtContent;
    private DatabaseHandler db;
    private String flag;
    private int idEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add);
        db = new DatabaseHandler(this);
        init();
        //lay ve gia tri ben activity cha
        Intent i = getIntent();

        flag = i.getStringExtra("flag");
        if (flag.equals("1")) {//neu la nut sua thi day gia tri can sua len giao dien
            Note c = (Note) i.getSerializableExtra("contact");
            txtTitle.setText(c.get_title());
            txtContent.setText(c.get_content());
            idEdit = c.get_id();
        }

        //bat su kien nhan nut
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = txtTitle.getText().toString();
                String content = txtContent.getText().toString();
                //add contact
                //Lưu vào sqlite

                if (flag.equals("1")) {

                    //Lưu vào sqlite
                    Note c = new Note(idEdit, title, content);
                    db.updateNote(c);
                    //day tra ve mainactivity
                    Intent i = new Intent();
                    i.putExtra("contact", c);
                    setResult(RESULT_OK, i);
                    finish();
                } else {
                    //lay ve ten, id, phonenumber
                    try {
                        //Lưu vào sqlite
                        Note c = new Note(1, title, content);
                        //goi ham addcontactl
                        long newid = db.addNote(c);

                        c.set_id((int) newid);
                        Intent i = new Intent();
                        i.putExtra("contact", c);
                        setResult(RESULT_OK, i);
                        finish();
                    } catch (Exception ex) {
                        Log.e("Ham them moi contact", ex.getMessage());
                    }
                }
            }
        });
    }

    public void init() {
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        txtTitle = findViewById(R.id.txtTitle);
        txtContent = findViewById(R.id.txtContent);
    }
}

