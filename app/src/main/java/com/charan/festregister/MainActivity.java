package com.charan.festregister;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button button,reset;
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
                namefield = (EditText) findViewById(R.id.namefield);
                phonefield = (EditText) findViewById(R.id.phonefield);
                collegefield = (EditText) findViewById(R.id.collegefield);

                tester = (TextView) findViewById(R.id.tester);

                name = namefield.getText().toString();
                phone = phonefield.getText().toString();
                college = collegefield.getText().toString();

                code = new RandomString(6).nextString();

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

    public class OnEventSpinnerItemSelected implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position){
                case 1 : event = "COUNTER STRIKE";
                    break;
                case 2 : event = "MINI MILITIA";
                    break;
                case 3 : event = "MAZE RUN";
                    break;
                case 4 : event = "MIND IT";
                    break;
                case 5 : event = "TECHQUIZIT";
                    break;
                case 6 : event = "REVERSE CODING";
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }


    
}
