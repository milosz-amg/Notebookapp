package com.example.notebookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NotebookActivity extends AppCompatActivity {

    private Button b_change_password;
    private Button b_save_note;
    private EditText edit_note;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    private String note_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notebook);

        b_change_password = (Button) findViewById(R.id.b_change_password);
        b_save_note = (Button) findViewById(R.id.b_save_note);
        edit_note = (EditText) findViewById(R.id.edit_note);

        b_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("b_change_password");
                openActivity();

            }
        });

        b_save_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

        loadData();
        updateViews();

    }

    public void saveData(){
        System.out.println("b_save_note");
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TEXT, edit_note.getText().toString()); //wrzucamy tekst notatki, trzeba dodac hash...
        editor.apply();
        Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
    }
    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        note_text = sharedPreferences.getString(TEXT,"Tu będzie twoja notatka");
    }
    public void updateViews(){
        edit_note.setText(note_text);
    }
    public void openActivity(){
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        startActivity(intent);
    }
}