package com.example.task;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jaredrummler.materialspinner.MaterialSpinner;

//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;

    ScaleAnimation shrinkAnim;
    private StaggeredGridLayoutManager mLayoutManager;
    private TextView tvNoMovies;

//    //Getting reference to Firebase Database
//    FirebaseDatabase database = FirebaseDatabase.getInstance();
//    DatabaseReference mDatabaseReference = database.getReference();

    private String category;
    private MaterialSpinner spinner;
    private TextView cattv;
    private Button bSubmit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Toolbar toolbar = findViewById(R.id.);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        //Initializing our Recyclerview
        cattv = findViewById(R.id.cattext);
//        tvNoMovies = (TextView) findViewById(R.id.tv_no_cards);
        category = "Option1";
        spinner = findViewById(R.id.categoryspinner);
        bSubmit = findViewById(R.id.b_submit);

        spinner.setItems("Option1", "Option2", "Option3");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                category = item;
            }
        });


        //scale animation to shrink floating actionbar
        shrinkAnim = new ScaleAnimation(1.15f, 0f, 1.15f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.b_submit) {
                    System.out.println("Category is " + category);
//                    fetchData process = new fetchData(category);
//                    process.execute();

                    Intent intent = new Intent(MainActivity.this, SecondScreen.class);
                    intent.putExtra("options", category);
                    startActivity(intent);


                }
            }
        });




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
