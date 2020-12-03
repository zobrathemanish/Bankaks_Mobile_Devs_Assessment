package com.example.task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.PatternSyntaxException;


public class fetchData extends AsyncTask<Void, Void, Void> {
    private String data= "";
    private String category;
    private String value;
    private String screen_title;
    private String no_of_fields;
    EditText myEditText;
    TextView textView;
    private Activity activity;
    final KProgressHUD progressDialog;
    private EditText[] editTextArray = new EditText[10];
    private String[] regexArray = new String[10];
    private int fieldLength;

    public fetchData(String category,Activity activity) {
        this.category = category;
        this.activity = activity;
        switch (category) {
            case "Option1":
                value = "1";
                break;
            case "Option2":
                value = "2";
                break;
            default:
                value = "3";
                break;
        }
         progressDialog = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @SuppressLint("WrongThread")
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            System.out.println("Value is " + value);
            URL url = new URL("https://api-staging.bankaks.com/task/"+value);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line ="";
            while(line!=null){
                line = bufferedReader.readLine();
                data = data + line;
            }

            JSONObject JO = new JSONObject(data);
            JSONObject Jresult = (JSONObject) JO.get("result");
            screen_title = (String) Jresult.get("screen_title");

            JSONArray fields = (JSONArray) Jresult.get("fields");

            fieldLength = fields.length();

            SecondScreen.title.setText(screen_title);

            for(int i = 0; i < fieldLength; i++){
                JSONObject fieldObjects = (JSONObject) fields.get(i);
                JSONObject ui_typeobject = (JSONObject) fieldObjects.get("ui_type");
                String placeHolder = (String) fieldObjects.get("placeholder");
                String hint_text = (String) fieldObjects.get("hint_text");
                String regex = (String) fieldObjects.get("regex");
                String type = (String) ui_typeobject.get("type");
                JSONObject Jtype = (JSONObject) fieldObjects.get("type");
                String dataType = (String) Jtype.get("data_type");

                if(type.equals("textfield")){

                    int finalI = i;
                    activity.runOnUiThread(new Runnable() {
                        public void run()
                        {
                            textView = new TextView(activity);
                            textView.setText(placeHolder);
                            textView.setTextColor(Color.parseColor("#ecf0f1"));
                            SecondScreen.linearLayout.addView(textView);
                            myEditText = new EditText(activity);
                            myEditText.setId(finalI);
                            myEditText.setTextColor(Color.parseColor("#ecf0f1"));
                            myEditText.setHint(hint_text);
                            myEditText.setHintTextColor(Color.parseColor("#bdc3c7"));
                            if(dataType.equals("int")){
                                myEditText.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED | InputType.TYPE_CLASS_NUMBER);
                            }
                            SecondScreen.linearLayout.addView(myEditText);

                            editTextArray[finalI] = myEditText;
                            regexArray[finalI] = regex;




                        }
                    });


                }

                if(type.equals("dropdown")){
                    ArrayList<String> spinnerArray = new ArrayList<String>();
                    JSONArray values = (JSONArray) ui_typeobject.get("values");
                    for(int a = 0; a < values.length(); a++) {
                        JSONObject valueObject = (JSONObject) values.get(a);
                        String month = (String) valueObject.get("name");
                        spinnerArray.add(month);

                    }

                    activity.runOnUiThread(new Runnable() {
                        public void run()
                        {
                            textView = new TextView(activity);
                            textView.setText(placeHolder);
                            textView.setTextColor(Color.parseColor("#ecf0f1"));
                            SecondScreen.linearLayout.addView(textView);
                            Spinner spinner = new Spinner(activity);
                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
                            spinner.setAdapter(spinnerArrayAdapter);
                            spinner.setBackgroundColor(Color.parseColor("#ecf0f1"));
                            SecondScreen.linearLayout.addView(spinner);

                        }
                    });

                    }

            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;


    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        Button proceedbtn = new Button(activity);
        proceedbtn.setText("Proceed");
        proceedbtn.setBackgroundResource(R.drawable.dark_rounded_shape);
        proceedbtn.setTextColor(Color.parseColor("#ffffff"));
        SecondScreen.linearLayout.addView(proceedbtn);

        progressDialog.dismiss();

        proceedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                proceedbtn.setClickable(true);  //was false

                hideKeyboard(activity);

                if(validateInputs()){
                    Intent intent = new Intent(activity, SucessScreen.class);
                    intent.putExtra("options", category);
                    activity.startActivity(intent);
                }
            }

            private boolean validateInputs() {
                for(int i = 0; i<fieldLength; i++){
                    if(editTextArray[i]!=null) {

                        //Could have changed the value of regex to suit the example given, as the regex given in the api had error.
                        //But sticked to the rules given in the question

//                        if(value.equals("3")) {
//                            regexArray[1] = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
//                            regexArray[2] = "^[0-9]{10}$";
//                        }
                        try{
                        if (editTextArray[i].getText().toString().trim().length() == 0) {
                            Toast.makeText(activity.getApplicationContext(), "Please fill up the fields", Toast.LENGTH_SHORT).show();
                            return false;
                        } else {
                            if (editTextArray[i].getText().toString().trim().matches(regexArray[i]) || regexArray[i].equals("")) {
                                System.out.println("validation success!!");
                                Toast.makeText(activity.getApplicationContext(), "Validation Successful!!", Toast.LENGTH_SHORT).show();
                                return true;
                            } else {
                                System.out.println("validation error!!"+ editTextArray[i].getText().toString().trim());
                                Toast.makeText(activity.getApplicationContext(), " Validation error!!"+ editTextArray[i].getText().toString().trim(), Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        }
                    }
                        catch (PatternSyntaxException ex) {
                            Toast.makeText(activity.getApplicationContext(), "The Regex couldn't Compile " + ex.getPattern(), Toast.LENGTH_SHORT).show();
                            System.out.println("This regex could not compile: "+ex.getPattern());
                            System.out.println(ex.getMessage());
                            return false;
                        }
                    }
                }
                return true;
            }
        });
    }
    }

