package com.example.daekyo.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class WriteBoard extends Activity {
    protected EditText title = null;
    protected EditText storename = null;
    protected TextView job = null;
    protected TextView start_date = null;
    protected TextView start_time = null;
    protected TextView end_date = null;
    protected TextView end_time = null;
    protected EditText urgency = null;
    protected EditText condition = null;
    protected TextView local = null;

    protected int[] date = new int[3];
    protected int[] time = new int[2];

    protected Button submit = null;
    protected Button cancel = null;
    protected String[] data = null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wrtie_board);

        title = (EditText) findViewById(R.id.title);
        storename = (EditText) findViewById(R.id.storename);
        job = (TextView) findViewById(R.id.job);
        start_date = (TextView) findViewById(R.id.start_date);
        start_time = (TextView) findViewById(R.id.start_time);
        end_date = (TextView) findViewById(R.id.end_date);
        end_time = (TextView) findViewById(R.id.end_time);
        urgency = (EditText) findViewById(R.id.urgency);
        condition = (EditText) findViewById(R.id.condition);
        local = (TextView) findViewById(R.id.local);

        submit = (Button) findViewById(R.id.button);
        cancel = (Button) findViewById(R.id.button2);

        start_date.setOnClickListener(selectStartDate);
        end_date.setOnClickListener(selectEndDate);
        start_time.setOnClickListener(selectStartTime);
        end_time.setOnClickListener(selectEndTime);
        job.setOnClickListener(selectJob);

        submit.setOnClickListener(submitter);
        cancel.setOnClickListener(calcelSubmittingBoard);
    }

    TextView.OnClickListener selectJob = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            final List<String> ListItems = new ArrayList<>();
            ListItems.add("매장관리");
            ListItems.add("서빙");
            ListItems.add("주방");
            ListItems.add("배달");
            ListItems.add("사무보조");
            ListItems.add("노무");
            final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);

            final List SelectedItems = new ArrayList();
            int defaultItem = 0;
            SelectedItems.add(defaultItem);

            AlertDialog.Builder itemSelect = new AlertDialog.Builder(WriteBoard.this);
            itemSelect.setTitle("직종을 선택하세요.");
            itemSelect.setSingleChoiceItems(items, defaultItem,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SelectedItems.clear();
                            SelectedItems.add(which);
                        }
                    });
            itemSelect.setPositiveButton("저장",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String msg = "";

                            if (!SelectedItems.isEmpty()) {
                                int index = (int) SelectedItems.get(0);
                                msg = ListItems.get(index);
                            }
                            job.setText(msg);
                        }
                    });
            itemSelect.setNegativeButton("취소",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            itemSelect.show();
        }
    };

    TextView.OnClickListener selectStartDate = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            String start;
            DatePickerDialog dpd = new DatePickerDialog(WriteBoard.this, mDateSetListener, 2018, 5, 8);
            dpd.show();

            start = Integer.toString(date[0])+"."+Integer.toString(date[1])+"."+Integer.toString(date[2]);

            if(start_time.length() != 0){
                TimePickerDialog tpd = new TimePickerDialog(WriteBoard.this, mTimeSetListener, 12, 0, false);
                tpd.show();
                start += " " + Integer.toString(date[3]) +":" + Integer.toString(date[4]);
            }
            start_date.setText(start);
        }
    };

    TextView.OnClickListener selectStartTime = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            String start;

            TimePickerDialog tpd = new TimePickerDialog(WriteBoard.this, mTimeSetListener, 12, 0, false);
            tpd.show();
            start = Integer.toString(time[0]) +":" + Integer.toString(time[1]);

            start_time.setText(start);
        }
    };

    TextView.OnClickListener selectEndDate = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            String end_time;
            DatePickerDialog dpd = new DatePickerDialog(WriteBoard.this, mDateSetListener, 2018, 5, 8);
            dpd.show();

            end_time = Integer.toString(date[0])+"."+Integer.toString(date[1])+"."+Integer.toString(date[2]);

            if(end_time.length() != 0){
                TimePickerDialog tpd = new TimePickerDialog(WriteBoard.this, mTimeSetListener, 12, 0, false);
                tpd.show();
                end_time += " " + Integer.toString(date[3]) +":" + Integer.toString(date[4]);
            }

            end_date.setText(end_time);
        }
    };

    TextView.OnClickListener selectEndTime = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            String end;

            TimePickerDialog tpd = new TimePickerDialog(WriteBoard.this, mTimeSetListener, 12, 0, false);
            tpd.show();
            end = Integer.toString(time[0]) +":" + Integer.toString(time[1]);

            end_time.setText(end);
        }
    };

    TextView.OnClickListener selectLocal = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
            int width = dm.widthPixels;
            int height = dm.heightPixels;

            LocalDialog ld = new LocalDialog(WriteBoard.this);
            WindowManager.LayoutParams wm = ld.getWindow().getAttributes();
            wm.copyFrom(ld.getWindow().getAttributes());
            wm.width = width / 2;
            wm.height = height / 2;
            ld.show();
        }
    };

    Button.OnClickListener submitter = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            data = new String[10];
            boolean flag = false; // To ckeck what don't write one or many contents in board.

            getDataFromSubmitter();

            for(int i=0; i<data.length; i++)
                if (data[i].length() == 0 ) {
                    flag = true;
                    break;
                }

            if(flag){ // If empty contents exist..
                Toast.makeText(getApplicationContext(),
                        "비어있는 항목이 있습니다.", Toast.LENGTH_SHORT).show();
            } else { // If not (full contents)
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setTitle("게시글 작성");
                builder.setMessage("게시글을 올리시겠습니까?");
                builder.setPositiveButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                builder.setNegativeButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                /* Server parts */

                                Toast.makeText(getApplicationContext(),"게시글을 작성하였습니다.",Toast.LENGTH_LONG).show();
                                goToMainActivity();
                            }
                        });
                builder.show();
            }
        }
    };

    Button.OnClickListener calcelSubmittingBoard = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
            builder.setTitle("게시글 작성 취소");
            builder.setMessage("게시글 작성을 취소하시겠습니까?");
            builder.setPositiveButton("아니오",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            builder.setNegativeButton("예",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(),"게시글 작성을 취소하셨습니다.",Toast.LENGTH_LONG).show();
                            goToMainActivity();
                        }
                    });
            builder.show();
        }
    };

    DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    // TODO Auto-generated method stub
                    date[0] = year;
                    date[1] = monthOfYear;
                    date[2] = dayOfMonth;
                }
            };

    TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                @Override

                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    // TODO Auto-generated method stub
                    time[0] = hourOfDay;
                    time[1] = minute;
                }
            };

    public void goToMainActivity(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void getDataFromSubmitter(){
        data[0] = title.getText().toString();
        data[1] = storename.getText().toString();
        data[2] = job.getText().toString();
        data[3] = start_date.getText().toString();
        data[4] = start_time.getText().toString();
        data[5] = end_date.getText().toString();
        data[6] = end_time.getText().toString();
        data[7] = urgency.getText().toString();
        data[8] = condition.getText().toString();
        data[9] = local.getText().toString();
    }
}