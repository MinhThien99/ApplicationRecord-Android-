package com.example.thien_record;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    EditText edtid , edtpassword;
    Button btnlogin , btnregister;
    Database database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        database = new Database(this);

        addViews();

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = edtid.getText().toString();
                String pass = edtpassword.getText().toString();

                Boolean checkemailpass = database.emailPass(id, pass);

                if(checkemailpass == true){
                    Toast.makeText(getApplicationContext(), "Successful Login" , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, start.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Wrong email or password " , Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void addViews() {

        edtid = findViewById(R.id.edtid);
        edtpassword = findViewById(R.id.edtpassword);

        btnlogin = findViewById(R.id.btnlogin);
        btnregister = findViewById(R.id.btnregister);

    }
}