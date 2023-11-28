package com.example.notebookapp;
//PS:112233445
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


public class MainActivity extends AppCompatActivity {
    TextView tv_password;
    Button b_password;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String PASSWORD = "password";
    public static final String SALT ="salt";
    String password_hash = "";
    String salt_s ="";
    public static String given_password="";
    int badTries=0;

    byte[] salt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b_password = (Button) findViewById(R.id.b_password);
        tv_password = (TextView) findViewById(R.id.tv_password);

        // czyszczenie shared prefs
//        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.clear();
//        editor.apply();

        loadData();


        b_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast;
                given_password = tv_password.getText().toString();
                b_password.setEnabled(false);   //guzik po klikniecu 'mrozi' na sekunde
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        b_password.setEnabled(true);
                    }
                },1000);

                boolean are_equal = false;
                try {
                    are_equal = Hashing.verifyPassword(given_password, password_hash, salt);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                } catch (InvalidKeySpecException e) {
                    throw new RuntimeException(e);
                }

                if(are_equal){
                //if(given_password.equals(password_hash)){
                    toast = Toast.makeText(getApplicationContext(), "correct", Toast.LENGTH_LONG);
                    openActivity();
                }
                else{
                    toast = Toast.makeText(getApplicationContext(), "password is not correct", Toast.LENGTH_LONG);
                    badTries=badTries+1;
                    if(badTries>=5){
                        //cos zrobic jak za duzo zlych prob usun dane ://
                        badTries=0;
                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        toast = Toast.makeText(getApplicationContext(), "5 bad tries, all data deleted", Toast.LENGTH_LONG);

                    }
                }
                toast.show();

            }
        });
    }

    public static String getGiven_password() {
        return given_password;
    }

    public static void setGiven_password(String updated_password){
        given_password=updated_password;
    }

    public void openActivity(){
        Intent intent = new Intent(this,NotebookActivity.class);
        startActivity(intent);
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        password_hash = sharedPreferences.getString(PASSWORD, "AA0bmfcKC9opGRK362q7regVwNWwJSUDE5EzOhe5nxM="); //default password to 123 hash
        //password_hash = sharedPreferences.getString(PASSWORD, "123"); //default password to 123 hash
        salt_s = sharedPreferences.getString(SALT,"9NfgJm0Sx5Y0i/9MisTktg==");
        salt = Base64.decode(salt_s, Base64.DEFAULT);
    }
}
