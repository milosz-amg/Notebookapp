package com.example.notebookapp;
//PS:123
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    TextView tv_password;
    Button b_password;
    public static final String SHARED_PREFS = "sharedPrefs";

    public static final String PASSWORD = "password";
    String password = "";
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
                Toast toast;
                given_password = tv_password.getText().toString();
                if(given_password.equals(password)){
                    toast = Toast.makeText(getApplicationContext(), "correct", Toast.LENGTH_LONG);
                    openActivity();
                }
                else{
                    toast = Toast.makeText(getApplicationContext(), "password is not correct", Toast.LENGTH_LONG);

                }
                toast.show();
            }
        });
        loadData();
    }


    public void openActivity(){
        Intent intent = new Intent(this,NotebookActivity.class);
        startActivity(intent);
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        password = sharedPreferences.getString(PASSWORD, "123"); //default password to 123
    }
}
