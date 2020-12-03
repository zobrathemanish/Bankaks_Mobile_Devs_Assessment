package com.example.task;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SecondScreen extends AppCompatActivity {

    private String category;
    public static TextView title;
    public static LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_screen);
        Bundle bundle = getIntent().getExtras();
        category = bundle.getString("options");
        linearLayout = findViewById(R.id.llayout);
        title = findViewById(R.id.screen_title);
        fetchData process = new fetchData(category,SecondScreen.this);
        process.execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
