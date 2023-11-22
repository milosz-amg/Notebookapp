package com.example.notebookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText et_old_password;
    private EditText et_new_password;
    private EditText et_confirm_new_password;
    private String old_password;
    private  String new_password;
    private String confirm_new_password;
    private Button b_confirm;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String PASSWORD = "password";

    public String shp_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        et_old_password = (EditText) findViewById(R.id.et_old_password);
        et_new_password = (EditText) findViewById(R.id.et_new_password);
        et_confirm_new_password = (EditText) findViewById(R.id.et_confirm_new_password);

        b_confirm = (Button) findViewById(R.id.b_confirm);

        loadData();

        b_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                old_password = et_old_password.getText().toString();
                new_password = et_new_password.getText().toString();
                confirm_new_password = et_confirm_new_password.getText().toString();

                if(old_password.equals(shp_password)){
                    if(new_password.equals(confirm_new_password)){
                        updateData();
                    }
                    else {
                        System.out.println("n_ps!=c_n_ps");
                    }
                }
                else {
//                    Toast.makeText(this, "er",Toast.LENGTH_SHORT).show();
                    System.out.println("old_ps_error");
                }
            }
        });
    }
    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        shp_password = sharedPreferences.getString(PASSWORD, "123");
    }

    public void updateData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(PASSWORD,et_new_password.getText().toString());
        editor.apply();
        Toast.makeText(this, "Password updated", Toast.LENGTH_SHORT).show();
    }
}