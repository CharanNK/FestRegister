package com.charan.festregister;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

import java.net.URLEncoder;


public class MainActivity extends AppCompatActivity {
    Button button,reset,upload_button;
    String name,branch="",sem="",event="",phone,college,message,code,time,coordno,roomno;
    EditText namefield,phonefield,collegefield;
    TextView branchfield,semfield,eventfield,codefield;

    public static final String URL= "https://docs.google.com/forms/d/1jwjhqapa6ZJG-fTDAfhemO2NPNxoAg7m0BtcjQtuqOs/formResponse";
    public static final MediaType FORM_DATA_TYPE
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    public static final String NAME_KEY="entry_2014583043";
    public static final String BRANCH_KEY="entry_1543322964";
    public static final String SEM_KEY="entry_1355186410";
    public static final String EVENT_KEY="entry_941354977";
    public static final String PHONE_KEY="entry_1966262782";
    public static final String COLLEGE_KEY="entry_907711248";
    public static final String CODE_KEY="entry_144009659";

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

        branchfield = (TextView) findViewById(R.id.branch_field);
        semfield = (TextView) findViewById(R.id.sem_field);
        eventfield = (TextView) findViewById(R.id.event_field);
        codefield = (TextView) findViewById(R.id.code_field);

        code = new RandomString(6).nextString();
        codefield.setText(code);

        event_spinner.setOnItemSelectedListener(new OnEventSpinnerItemSelected());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = namefield.getText().toString();
                phone = phonefield.getText().toString();
                college = collegefield.getText().toString();

                //Make sure all the fields are filled with values
                if(TextUtils.isEmpty(namefield.getText().toString()) ||
                        TextUtils.isEmpty(phonefield.getText().toString()) ||
                        TextUtils.isEmpty(collegefield.getText().toString())||
                        TextUtils.isEmpty(branch)||
                        TextUtils.isEmpty(sem)||
                        TextUtils.isEmpty(event))
                {
                    Toast.makeText(getApplicationContext(),"All fields are mandatory.",Toast.LENGTH_LONG).show();
                    return;
                }

                message = "Name-"+name+",Event-"+event+",Code-"+code+",Time-"+time+",Room-"+roomno+",Co-ord :"+coordno;

                //Call default SMS application
 /*               SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(phone,null,message,null,null);
                Toast.makeText(getApplicationContext(),"Message Sent!",Toast.LENGTH_LONG).show();

                //Disable Button
                button.setEnabled(false);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        button.setEnabled(true);
                    }
                },5000); */

                Toast.makeText(getApplicationContext(),"Message Sent!",Toast.LENGTH_LONG).show();

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
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(namefield.getText().toString()) ||
                        TextUtils.isEmpty(phonefield.getText().toString()) ||
                        TextUtils.isEmpty(collegefield.getText().toString())||
                        TextUtils.isEmpty(branch)||
                        TextUtils.isEmpty(sem)||
                        TextUtils.isEmpty(event))
                {
                    Toast.makeText(getApplicationContext(),"All fields are mandatory.",Toast.LENGTH_LONG).show();
                    return;
                }
                PostDataTask postDataTask = new PostDataTask();

                //execute asynctask
                postDataTask.execute(URL,namefield.getText().toString(),
                        branchfield.getText().toString(),
                        semfield.getText().toString(),
                        eventfield.getText().toString()
                        ,phonefield.getText().toString(),
                        collegefield.getText().toString(),
                        codefield.getText().toString());
            }
        });
    }
    public class OnBranchSpinnerItemSelected implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position){
                case 1 : branch = "CIVIL";
                    branchfield.setText(branch);
                    break;
                case 2 : branch = "CSE";
                    branchfield.setText(branch);
                    break;
                case 3 : branch = "ECE";
                    branchfield.setText(branch);
                    break;
                case 4 : branch = "EEE";
                    branchfield.setText(branch);
                    break;
                case 5 : branch = "ISE";
                    branchfield.setText(branch);
                    break;
                case 6 : branch = "IT";
                    branchfield.setText(branch);
                    break;
                case 7 : branch = "MECH";
                    branchfield.setText(branch);
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
                    semfield.setText(sem);
                    break;
                case 2 : sem = "4";
                    semfield.setText(sem);
                    break;
                case 3 : sem = "6";
                    semfield.setText(sem);
                    break;
                case 4 : sem = "8";
                    semfield.setText(sem);
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
                    time = "All Day";
                    roomno = "LAB2";
                    coordno = "Gagan-8553438893";
                    eventfield.setText(event);
                    break;
                case 2:
                    event = "MINI MILITIA";
                    time = "1PM";
                    roomno="103";
                    coordno="Gagan-8553438893";
                    eventfield.setText(event);
                    break;
                case 3:
                    event = "MAZE RUN";
                    time= "9:30";
                    roomno = "Hitech lab";
                    coordno = "Avinash-9880430068";
                    eventfield.setText(event);
                    break;
                case 4:
                    event = "MIND IT";
                    time = "12:00";
                    roomno="102";
                    coordno = "Ashwin-8892664963";
                    eventfield.setText(event);
                    break;
                case 5:
                    event = "TECHQUIZIT";
                    time = "9AM";
                    roomno="104";
                    coordno = "Aditya-8884541594";
                    eventfield.setText(event);
                    break;
                case 6:
                    event = "REVERSE CODING";
                    time = "2PM";
                    roomno="LAB3";
                    coordno = "Mahesh-9742053664";
                    eventfield.setText(event);
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
    private class PostDataTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... uploadData) {
            Boolean result = true;
            String url = uploadData[0];
            String name = uploadData[1];
            String branch = uploadData[2];
            String sem = uploadData[3];
            String event = uploadData[4];
            String phone = uploadData[5];
            String college = uploadData[6];
            String code = uploadData[7];
            String postBody="";

            try {
                //all values must be URL encoded to make sure that special characters like & | ",etc.
                //do not cause problems
                postBody = NAME_KEY+"=" + URLEncoder.encode(name,"UTF-8") +
                        "&" +BRANCH_KEY + "=" + URLEncoder.encode(branch,"UTF-8")+
                        "&" + SEM_KEY + "=" + URLEncoder.encode(sem,"UTF-8")+
                        "&" + EVENT_KEY + "=" + URLEncoder.encode(event,"UTF-8")+
                        "&" + PHONE_KEY + "=" + URLEncoder.encode(phone,"UTF-8")+
                        "&" + COLLEGE_KEY + "=" + URLEncoder.encode(college,"UTF-8") +
                        "&" + CODE_KEY + "=" + URLEncoder.encode(code,"UTF-8");
            } catch (UnsupportedEncodingException ex) {
                result=false;
            } catch (NullPointerException e){
                result = false;
            }

            /*
            //If you want to use HttpRequest class from http://stackoverflow.com/a/2253280/1261816
            try {
			HttpRequest httpRequest = new HttpRequest();
			httpRequest.sendPost(url, postBody);
		}catch (Exception exception){
			result = false;
		}
            */

            try{
                //Create OkHttpClient for sending request
                OkHttpClient client = new OkHttpClient();
                //Create the request body with the help of Media Type
                RequestBody body = RequestBody.create(FORM_DATA_TYPE, postBody);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                //Send the request
                Response response = client.newCall(request).execute();
            }catch (IOException exception){
                result=false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result){
            //Print Success or failure message accordingly
            Toast.makeText(getApplicationContext(),result?"Uploaded to Drive!":"There was some error in sending message. Please try again after some time.",Toast.LENGTH_LONG).show();
        }

    }
}
