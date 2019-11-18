package com.whiteglobe.crm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SelectMap extends AppCompatActivity {

    AppCompatButton btnViewAllUsers,btnViewPerticularUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_map);
        getSupportActionBar().hide();

        btnViewAllUsers = findViewById(R.id.btnViewAllUsers);
        btnViewPerticularUser = findViewById(R.id.btnViewPerticularUser);

        btnViewAllUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iAllUsers = new Intent(SelectMap.this,UsersOnMap.class);
                String url = WebName.weburl+"usersonmap.php";
                iAllUsers.putExtra("url",url);
                startActivity(iAllUsers);
            }
        });

        btnViewPerticularUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iPerticularUser = new Intent(SelectMap.this,SelectUserOnMap.class);
                startActivity(iPerticularUser);
            }
        });
    }
}
