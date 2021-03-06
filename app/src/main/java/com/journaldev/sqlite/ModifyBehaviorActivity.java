package com.journaldev.sqlite;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class ModifyBehaviorActivity extends Activity implements OnClickListener {

    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;

    private EditText studentidText;
    private EditText studentnameText;
    private EditText studentperText;
    private Button updateBtn, deleteBtn;

    private long _id;

    private DBBehaviorManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Modify Record");

        setContentView(R.layout.behavior_modify_record);

        dbManager = new DBBehaviorManager(this);
        dbManager.open();

        studentidText = (EditText) findViewById(R.id.studentid_edittext);
        //studentnameText = (EditText) findViewById(R.id.studentname_edittext);

        Spinner mySpinner=(Spinner) findViewById(R.id.studentname_edittext);
        String behaviorNameEditText = mySpinner.getSelectedItem().toString();

        studentperText = (EditText) findViewById(R.id.studentper_edittext);

        updateBtn = (Button) findViewById(R.id.btn_update);
        deleteBtn = (Button) findViewById(R.id.btn_delete);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String studentid = intent.getStringExtra("studentid");

        String name = intent.getStringExtra("studentname");
        Spinner spinner=(Spinner) findViewById(R.id.studentname_edittext);
        spinner.setSelection(getIndex(spinner, name));

        String period = intent.getStringExtra("studentper");
        //studentperText = (EditText) findViewById(R.id.studentper_edittext);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

        _id = Long.parseLong(id);

        studentidText.setText(studentid);
        //studentnameText.setText(name);
        studentperText.setText(period);

        updateBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
    }

    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update:
                String studentid = studentidText.getText().toString();
                //String  studentname = studentnameText.getText().toString();

                Spinner mySpinner=(Spinner) findViewById(R.id.studentname_edittext);
                String studentname = mySpinner.getSelectedItem().toString();

                String studentper = studentperText.getText().toString();

                dbManager.update(_id, studentid, studentname, studentper);
                this.returnHome();
                break;

            case R.id.btn_delete:
                dbManager.delete(_id);
                this.returnHome();
                break;
        }
    }

    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), BehaviorListActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        studentperText.setText(new StringBuilder().append(month).append("/")
                .append(day).append("/").append(year));
    }
}