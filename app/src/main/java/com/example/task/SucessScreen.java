package com.example.task;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SucessScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucess_screen);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if (fab.getVisibility() == View.GONE)
//            fab.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
