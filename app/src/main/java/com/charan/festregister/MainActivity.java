package com.charan.festregister;

import android.annotation.TargetApi;
import android.app.VoiceInteractor;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import okhttp3.FormBody;
/*import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;*/

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.*;

import java.net.URLEncoder;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity {
    Button button,reset,upload_button;
    String name,branch,sem,event,phone,college,message,code;
    EditText namefield,phonefield,collegefield;
    TextView tester;
    final String myTag = "DocsUpload";
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
    public class PostData extends AsyncTask<Void,Void,Void>{
        HttpURLConnection connection ;
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected Void doInBackground(Void... params) {
            String posting_name = name;
            String posting_branch = branch;
            String posting_sem = sem;
            String posting_event = event;
            String posting_phone= phone;
            String posting_college= college;
            String posting_code= code;
            String fullUrl = "https://docs.google.com/forms/d/1fy3QFvgLPVFHhC5FwFSHmyMtFgZNPbKyjoMW2UF9hIw/formResponse";
            try {
                URL url = new URL(fullUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");

                List<NameValuePair> data = new ArrayList<NameValuePair>();
                data.add(new BasicNameValuePair("entry_184103295", posting_name));
//                data.add(new BasicNameValuePair("secondParam", paramValue2));
//                data.add(new BasicNameValuePair("thirdParam", paramValue3));

                OutputStream os = connection.getOutputStream();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }




            String data = null;

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(),"Upload Complete !",Toast.LENGTH_LONG).show();
        }
    }

}
