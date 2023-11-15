package com.example.notebookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class NotebookActivity extends AppCompatActivity {

    Button b_change_password;
    Button b_save_note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notebook);

        b_change_password = (Button) findViewById(R.id.b_change_password);
        b_save_note = (Button) findViewById(R.id.b_save_note);

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
                System.out.println("b_save_note");
            }
        });


    }
    public void openActivity(){
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        startActivity(intent);
    }
}