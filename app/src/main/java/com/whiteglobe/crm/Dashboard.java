package com.whiteglobe.crm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Dashboard extends AppCompatActivity {

    FloatingActionButton userAccount,logout,leads,meetings;
    SharedPreferences sessionDashboard;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        sessionDashboard = getSharedPreferences("user_details",MODE_PRIVATE);

        getSupportActionBar().hide();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(sessionDashboard.getString("username",null));
        //Toast.makeText(getApplicationContext(),sessionDashboard.getString("uname",null), Toast.LENGTH_SHORT).show();

        //To go in User Account Activity
        userAccount();

        //For Logging Out User
        logout();

        //To go in Leads Activity
        leads();

        //To go in Meetings Activity
        meetings();
    }

    private void userAccount()
    {
        userAccount = findViewById(R.id.btnUserAccount);

        userAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iUserAccount = new Intent(Dashboard.this, UserAccount.class);
                startActivity(iUserAccount);
            }
        });

    }

    private void logout()
    {
        logout = findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iLogout = new Intent(Dashboard.this, MainActivity.class);
                SharedPreferences.Editor editor = sessionDashboard.edit();
                editor.clear();
                editor.commit();
                startActivity(iLogout);
                finish();
            }
        });
    }

    private void leads()
    {
        leads = findViewById(R.id.leads);

        leads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iLeads = new Intent(Dashboard.this,Leads.class);
                startActivity(iLeads);
            }
        });
    }

    private void meetings()
    {
        meetings= findViewById(R.id.meetings);

        meetings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iMeetings = new Intent(Dashboard.this,Meetings.class);
                startActivity(iMeetings);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.d(this.getClass().getName(), "back button pressed");
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
