package com.example.notebookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NotebookActivity extends AppCompatActivity {

    private EditText edit_note;
    private String note_text;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    public static final String PASSWORD = "password";

    public static final String SALT ="salt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notebook);

        Button b_change_password = (Button) findViewById(R.id.b_change_password);
        Button b_save_note = (Button) findViewById(R.id.b_save_note);
        edit_note = (EditText) findViewById(R.id.edit_note);

        loadData();
        updateViews();

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

    }

    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        note_text = edit_note.getText().toString();
        String key = MainActivity.getGiven_password();
//        String key = sharedPreferences.getString(PASSWORD,"AA0bmfcKC9opGRK362q7regVwNWwJSUDE5EzOhe5nxM=");
        String salt_s=sharedPreferences.getString(SALT,"9NfgJm0Sx5Y0i/9MisTktg==");
        byte[] salt = Base64.decode(salt_s,Base64.DEFAULT);
        String note_text_encrypted = Hashing.encryptNote(note_text,key,salt);

        editor.putString(TEXT, note_text_encrypted);
        editor.apply();
        Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
    }
    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
//        String key = sharedPreferences.getString(PASSWORD,"AA0bmfcKC9opGRK362q7regVwNWwJSUDE5EzOhe5nxM=
        String key = MainActivity.getGiven_password();
        String note_text_encrypted = sharedPreferences.getString(TEXT,"Tu będzie twoja notatka");
        String salt_s=sharedPreferences.getString(SALT,"9NfgJm0Sx5Y0i/9MisTktg==");
        byte[] salt = Base64.decode(salt_s,Base64.DEFAULT);
        //CZY LEPIEJ UŻYĆ HASLA STRING CZY JEGO HASH??
        note_text = Hashing.decryptNote(note_text_encrypted,key,salt);
    }
    public void updateViews(){
        edit_note.setText(note_text);
    }
    public void openActivity(){
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        startActivity(intent);
    }
}
