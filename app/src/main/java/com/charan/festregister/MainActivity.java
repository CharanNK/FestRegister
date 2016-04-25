package com.charan.festregister;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.HttpURLConnection;

import java.net.URLEncoder;


public class MainActivity extends AppCompatActivity {
    Button button,reset,upload_button;
    String name,branch,sem,event,phone,college,message,code;
    EditText namefield,phonefield,collegefield;
    TextView tester;

    public static final String URL="https://docs.google.com/forms/d/1fy3QFvgLPVFHhC5FwFSHmyMtFgZNPbKyjoMW2UF9hIw/formResponse";
    public static final MediaType FORM_DATA_TYPE
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    public static final String NAME_KEY="entry_184103295";
    public static final String BRANCH_KEY="entry_48649782";
    public static final String SEM_KEY="entry_1945317796";
    public static final String EVENT_KEY="entry_926540497";
    public static final String PHONE_KEY="entry_1535623085";
    public static final String COLLEGE_KEY="entry_717811992";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button)findViewById(R.id.button);
        reset = (Button)findViewById(R.id.reset_button);
        upload_button = (Button) findViewById(R.id.upload_button);

        //Spinner for Branch
        Spinner branch_spinner = (Spinner) findViewById(R.id.branch_spinner);
        ArrayAdapter<CharSequence> branch_adapter = ArrayAdapter.createFromResource(this,
                R.array.branch_array, R.layout.spinner_item);
        branch_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branch_spinner.setAdapter(branch_adapter);

        branch_spinner.setOnItemSelectedListener(new OnBranchSpinnerItemSelected());

        //Spinner for Sem
        Spinner sem_spinner = (Spinner) findViewById(R.id.sem_spinner);
        ArrayAdapter<CharSequence> sem_adapter = ArrayAdapter.createFromResource(this,
                R.array.sem_array, R.layout.spinner_item);
        sem_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sem_spinner.setAdapter(sem_adapter);

        sem_spinner.setOnItemSelectedListener( new OnSemSpinnerItemSelected());

        //Spinner for events
        Spinner event_spinner = (Spinner) findViewById(R.id.event_spinner);
        ArrayAdapter<CharSequence> event_adapter = ArrayAdapter.createFromResource(this,
                R.array.event_array, R.layout.spinner_item);
        event_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        event_spinner.setAdapter(event_adapter);

        namefield = (EditText) findViewById(R.id.namefield);
        phonefield = (EditText) findViewById(R.id.phonefield);
        collegefield = (EditText) findViewById(R.id.collegefield);

        tester = (TextView) findViewById(R.id.tester);

        event_spinner.setOnItemSelectedListener(new OnEventSpinnerItemSelected());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = namefield.getText().toString();
                phone = phonefield.getText().toString();
                college = collegefield.getText().toString();

                code = new RandomString(6).nextString();

                message = "NAME-"+name+" EVENT-"+event+" CODE-"+code+" TIME-";

                //Call default SMS application
                Uri sms_uri = Uri.parse("smsto:+91"+phone);
                Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
                sms_intent.putExtra("sms_body",message);
                startActivity(sms_intent);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = namefield.getText().toString();
                phone = phonefield.getText().toString();
                college = collegefield.getText().toString();

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
              //  new PostData().execute();
            }
        });

        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PostData().execute();
            }
        });
    }
    public class OnBranchSpinnerItemSelected implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position){
                case 1 : branch = "CIVIL";
                    break;
                case 2 : branch = "CSE";
                    break;
                case 3 : branch = "ECE";
                    break;
                case 4 : branch = "EEE";
                    break;
                case 5 : branch = "ISE";
                    break;
                case 6 : branch = "IT";
                    break;
                case 7 : branch = "MECH";
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    public class OnSemSpinnerItemSelected implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position){
                case 1 : sem = "2";
                    break;
                case 2 : sem = "4";
                    break;
                case 3 : sem = "6";
                    break;
                case 4 : sem = "8";
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    public class OnEventSpinnerItemSelected implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 1:
                    event = "COUNTER STRIKE";
                    break;
                case 2:
                    event = "MINI MILITIA";
                    break;
                case 3:
                    event = "MAZE RUN";
                    break;
                case 4:
                    event = "MIND IT";
                    break;
                case 5:
                    event = "TECHQUIZIT";
                    break;
                case 6:
                    event = "REVERSE CODING";
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
    public class PostData extends AsyncTask<Void, Void, Boolean> {
        HttpURLConnection connection ;
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected Boolean doInBackground(Void... params) {
            String posting_name = name;
            String posting_branch = branch;
            String posting_sem = sem;
            String posting_event = event;
            String posting_phone= phone;
            String posting_college= college;
            String posting_code= code;
            Boolean result = true;
            String postBody="";

            try {
                //all values must be URL encoded to make sure that special characters like & | ",etc.
                //do not cause problems
                postBody = NAME_KEY+"=" + URLEncoder.encode(posting_name,"UTF-8") +
                        "&" + BRANCH_KEY + "=" + URLEncoder.encode(posting_branch,"UTF-8") +
                        "&" + SEM_KEY + "=" + URLEncoder.encode(posting_sem,"UTF-8") +
                        "&" + EVENT_KEY + "=" + URLEncoder.encode(posting_event,"UTF-8") +
                        "&" + PHONE_KEY + "=" + URLEncoder.encode(posting_phone,"UTF-8") +
                        "&" + COLLEGE_KEY + "=" + URLEncoder.encode(posting_college,"UTF-8");
            } catch (Exception ex) {
                result=false;
            }

            try {
                HttpRequest httpRequest = new HttpRequest();
                httpRequest.sendPost(URL, postBody);
            }catch (Exception exception){
                result = false;
            }

//            try{
//                //Create OkHttpClient for sending request
//                OkHttpClient client = new OkHttpClient();
//                //Create the request body with the help of Media Type
//                RequestBody body = RequestBody.create(FORM_DATA_TYPE, postBody);
//                Request request = new Request.Builder()
//                        .url(URL)
//                        .post(body)
//                        .build();
//                //Send the request
//                Response response = client.newCall(request).execute();
//            }catch (IOException exception){
//                result=false;
//            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(),"Upload Complete !",Toast.LENGTH_LONG).show();
        }
    }

}
