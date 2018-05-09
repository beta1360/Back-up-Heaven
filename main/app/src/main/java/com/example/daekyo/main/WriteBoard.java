package com.example.daekyo.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
    protected TextView upper_local = null;
    protected TextView lower_local = null;

    protected int[] date = new int[3];
    protected int[] time = new int[2];

    protected Button submit = null;
    protected Button cancel = null;
    protected String[] data = null;

    private static final String MAIN_SERVER = "https://192.168.0.103:8000/regitst";
    private String sender = null;
    protected List<String> localList = null;
    private int localIndex = -1;
    private String selectedUpperLocal = null;
    private String selectedLowerLocal = null;
    protected RequestQueue mQueue = null;
    protected JSONObject mResult = null;

    GregorianCalendar calendar = new GregorianCalendar();
    protected int year = calendar.get(Calendar.YEAR);
    protected int month = calendar.get(Calendar.MONTH);
    protected int day= calendar.get(Calendar.DAY_OF_MONTH);
    protected int hour = calendar.get(Calendar.HOUR_OF_DAY);
    protected int minute = calendar.get(Calendar.MINUTE);

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
        upper_local = (TextView) findViewById(R.id.upper_local);
        lower_local = (TextView) findViewById(R.id.lower_local);

        submit = (Button) findViewById(R.id.button);
        cancel = (Button) findViewById(R.id.button2);

        start_date.setOnClickListener(selectStartDate);
        end_date.setOnClickListener(selectEndDate);
        start_time.setOnClickListener(selectStartTime);
        end_time.setOnClickListener(selectEndTime);
        job.setOnClickListener(selectJob);

        submit.setOnClickListener(submitter);
        cancel.setOnClickListener(calcelSubmittingBoard);
        upper_local.setOnClickListener(selectUpperLocal);
        lower_local.setOnClickListener(selectLowerLocal);
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

    private String start_date_msg;
    DatePickerDialog.OnDateSetListener start_dateSetListener= new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            date[0] = year;
            date[1] = monthOfYear+1;
            date[2] = dayOfMonth;
            start_date_msg = Integer.toString(date[0])+"."+Integer.toString(date[1])+"."+Integer.toString(date[2]);
            start_date.setText(start_date_msg);
        }
    };

    TextView.OnClickListener selectStartDate = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            new DatePickerDialog(WriteBoard.this, start_dateSetListener , year, month, day).show();
        }
    };

    private String start_time_msg;
    TimePickerDialog.OnTimeSetListener start_timeSetListener= new TimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // TODO Auto-generated method stub
            time[0] = hourOfDay;
            time[1] = minute;
            start_time_msg = Integer.toString(time[0])+":"+Integer.toString(time[1]);
            start_time.setText(start_time_msg);
        }
    };

    TextView.OnClickListener selectStartTime = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            new TimePickerDialog(WriteBoard.this, start_timeSetListener, hour, minute, false).show();
        }
    };

    private String end_date_msg;
    DatePickerDialog.OnDateSetListener end_dateSetListener= new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            date[0] = year;
            date[1] = monthOfYear+1;
            date[2] = dayOfMonth;
            end_date_msg = Integer.toString(date[0])+"."+Integer.toString(date[1])+"."+Integer.toString(date[2]);
            end_date.setText(end_date_msg);
        }
    };

    TextView.OnClickListener selectEndDate = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            new DatePickerDialog(WriteBoard.this, end_dateSetListener , year, month, day).show();
        }
    };

    private String end_time_msg;
    TimePickerDialog.OnTimeSetListener end_timeSetListener= new TimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // TODO Auto-generated method stub
            time[0] = hourOfDay;
            time[1] = minute;
            end_time_msg = Integer.toString(time[0])+":"+Integer.toString(time[1]);
            end_time.setText(end_time_msg);
        }
    };

    TextView.OnClickListener selectEndTime = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            new TimePickerDialog(WriteBoard.this, end_timeSetListener, hour, minute, false).show();
        }
    };

    TextView.OnClickListener selectUpperLocal = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            final CharSequence[] items = { "서울시", "대구시", "경상북도" };
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    WriteBoard.this);

            alertDialogBuilder.setTitle("지역 선택");
            alertDialogBuilder.setItems(items,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int id) {
                            localIndex = id;
                            selectedUpperLocal = items[id].toString();
                            upper_local.setText(selectedUpperLocal);
                            dialog.dismiss();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    };

    TextView.OnClickListener selectLowerLocal = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    WriteBoard.this);

            alertDialogBuilder.setTitle("지역 선택");
            if(localIndex == 0) {
                final CharSequence[] items = { "강남구","서초구","강동구","강북구","종로구" };
                alertDialogBuilder.setItems(items,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {

                                selectedLowerLocal = items[id].toString();
                                lower_local.setText(selectedLowerLocal);
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            } else if(localIndex == 1){
                final CharSequence[] items = { "달서구", "수성구", "달성군", "중구", "북구" };
                alertDialogBuilder.setItems(items,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {

                                selectedLowerLocal = items[id].toString();
                                lower_local.setText(selectedLowerLocal);
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            } else if(localIndex == 2){
                final CharSequence[] items = { "포항시", "구미시", "안동시", "칠곡군", "김천시" };
                alertDialogBuilder.setItems(items,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {

                                selectedLowerLocal = items[id].toString();
                                lower_local.setText(selectedLowerLocal);
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            } else {
                Toast.makeText(WriteBoard.this, "상위지역부터 선택해주세요",Toast.LENGTH_SHORT).show();
            }
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
                                /*JSONObject request = new JSONObject();
                                try {
                                    request.put("title",data[0]);
                                    request.put("storename",data[1]);
                                    request.put("job",data[2]);
                                    request.put("start_date",data[3]);
                                    request.put("start_time",data[4]);
                                    request.put("end_date",data[5]);
                                    request.put("end_time",data[6]);
                                    request.put("urgency",data[7]);
                                    request.put("condition",data[8]);
                                    request.put("upper_local",data[9]);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,
                                        MAIN_SERVER, request, new Response.Listener<JSONObject>(){
                                    @Override
                                    public void onResponse (JSONObject response){
                                        mResult = response;
                                    }
                                }
                                        ,new Response.ErrorListener(){
                                    @Override
                                    public void onErrorResponse(VolleyError error){
                                        Toast.makeText(WriteBoard.this,
                                                error.toString(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });

                                mQueue.add(req);
                                */
                                Toast.makeText(getApplicationContext(), "게시글을 작성하였습니다.", Toast.LENGTH_LONG).show();
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
            AlertDialog.Builder builder = new AlertDialog.Builder(WriteBoard.this);
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
                            Toast.makeText(WriteBoard.this,"게시글 작성을 취소하셨습니다.",Toast.LENGTH_LONG).show();
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
        data[9] = upper_local.getText().toString();
    }
}