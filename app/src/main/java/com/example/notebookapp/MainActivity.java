package com.example.notebookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    TextView tv_password;
    Button b_password;
    String password = "123";
    String given_password="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b_password = (Button) findViewById(R.id.b_password);
        tv_password = (TextView) findViewById(R.id.tv_password);

        b_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                given_password = tv_password.getText().toString();

                Toast toast;
                if(given_password.equals(password)){
                    toast = Toast.makeText(getApplicationContext(), "correct", Toast.LENGTH_LONG);
                    openNotebookActivity();
                }
                else{
                    toast = Toast.makeText(getApplicationContext(), "nope", Toast.LENGTH_LONG);

                }
                toast.show();
            }
        });
    }
    public void openNotebookActivity(){
        Intent intent = new Intent(this,NotebookActivity.class);
        startActivity(intent);
    }
}