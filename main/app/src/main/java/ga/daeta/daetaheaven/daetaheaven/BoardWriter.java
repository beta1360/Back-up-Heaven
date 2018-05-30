package ga.daeta.daetaheaven.daetaheaven;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class BoardWriter extends Activity {
    private String ID = null;

    protected RequestQueue mQueue = null;
    protected JSONObject mResult = null;
    protected final String SERVER_LOCAL_FILTER = "http://daeta.ga/locallist?filter=";
    protected final String SERVER_JOB_FILTER = "http://daeta.ga/joblist";
    protected final String SERVER_REGIST_BOARD = "http://daeta.ga/board_regist";
    protected String upper_local_text = null;
    protected String upper_local = "대한민국";
    protected String lower_local = null;
    protected ArrayList<String> upperLocalList = new ArrayList<String>();
    protected ArrayList<String> lowerLocalList = new ArrayList<String>();

    private EditText board_title = null;
    private EditText store_name = null;
    private Button local_selector = null;
    private EditText local_sub = null;
    private EditText phone = null;

    private Spinner start_year = null;
    private Spinner start_mon = null;
    private Spinner start_day = null;
    private Spinner start_hour = null;
    private Spinner start_min = null;

    private Spinner end_year = null;
    private Spinner end_mon = null;
    private Spinner end_day = null;
    private Spinner end_hour = null;
    private Spinner end_min = null;

    private EditText urgency = null;
    private Spinner gender = null;

    private EditText condition = null;
    private Button job = null;
    private EditText favorable_condition = null;
    private EditText detail = null;

    private Button ok_button = null;
    private Button cancel_button = null;

    protected String st_year = null;
    protected String st_month = null;
    protected String st_day = null;
    protected String st_hour = null;
    protected String st_min = null;

    protected String ed_year = null;
    protected String ed_month = null;
    protected String ed_day = null;
    protected String ed_hour = null;
    protected String ed_min = null;

    protected String sex = null;

    protected ArrayList<String> st_yearList = new ArrayList<>();
    protected ArrayAdapter<String> st_yearSpinnerAdapter = null;
    protected ArrayList<String> st_monthList = new ArrayList<>();
    protected ArrayAdapter<String> st_monthSpinnerAdapter = null;
    protected ArrayList<String> st_dayList = new ArrayList<>();
    protected ArrayAdapter<String> st_daySpinnerAdapter = null;
    protected ArrayList<String> st_hourList = new ArrayList<>();
    protected ArrayAdapter<String> st_hourSpinnerAdapter = null;
    protected ArrayList<String> st_minList = new ArrayList<>();
    protected ArrayAdapter<String> st_minSpinnerAdapter = null;

    protected ArrayList<String> ed_yearList = new ArrayList<>();
    protected ArrayAdapter<String> ed_yearSpinnerAdapter = null;
    protected ArrayList<String> ed_monthList = new ArrayList<>();
    protected ArrayAdapter<String> ed_monthSpinnerAdapter = null;
    protected ArrayList<String> ed_dayList = new ArrayList<>();
    protected ArrayAdapter<String> ed_daySpinnerAdapter = null;
    protected ArrayList<String> ed_hourList = new ArrayList<>();
    protected ArrayAdapter<String> ed_hourSpinnerAdapter = null;
    protected ArrayList<String> ed_minList = new ArrayList<>();
    protected ArrayAdapter<String> ed_minSpinnerAdapter = null;

    protected ArrayList<String> genderList = new ArrayList<>();
    protected ArrayAdapter<String> genderSpinnerAdapter = null;

    ArrayList<String> jobList = new ArrayList<String>();
    protected String selected_job = null;

    private JSONObject request_board_register = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            ID = bundle.getString("ID");

        setContentView(R.layout.write_board);
        mQueue = Volley.newRequestQueue(this);

        this.initializeView();
        this.intializeSpinner();
        this.setEventToComponents();
    }

    Spinner.OnItemSelectedListener selected_start_year = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            st_year = start_year.getItemAtPosition(position).toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {  }
    };
    Spinner.OnItemSelectedListener selected_start_mon = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            st_month = start_mon.getItemAtPosition(position).toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {  }
    };
    Spinner.OnItemSelectedListener selected_start_day = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            st_day = start_day.getItemAtPosition(position).toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {  }
    };
    Spinner.OnItemSelectedListener selected_start_hour = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            st_hour = start_hour.getItemAtPosition(position).toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {  }
    };
    Spinner.OnItemSelectedListener selected_start_min = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            st_min = start_min.getItemAtPosition(position).toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {  }
    };

    Spinner.OnItemSelectedListener selected_end_year = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ed_year = end_year.getItemAtPosition(position).toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {  }
    };
    Spinner.OnItemSelectedListener selected_end_mon = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ed_month = end_mon.getItemAtPosition(position).toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {  }
    };
    Spinner.OnItemSelectedListener selected_end_day = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ed_day = end_day.getItemAtPosition(position).toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {  }
    };
    Spinner.OnItemSelectedListener selected_end_hour = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ed_hour = end_hour.getItemAtPosition(position).toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {  }
    };
    Spinner.OnItemSelectedListener selected_end_min = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ed_min = end_min.getItemAtPosition(position).toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {  }
    };

    Spinner.OnItemSelectedListener selected_gender = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            sex = gender.getItemAtPosition(position).toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {  }
    };

    Button.OnClickListener ok_event = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(BoardWriter.this);
            alert_confirm.setMessage("게시물을 올리시겠습니까?")
                    .setCancelable(false).setPositiveButton("예",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                sendBoardInfo();
                                Toast.makeText(BoardWriter.this,"게시물 작성에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                Intent go_main = new Intent(BoardWriter.this, MainActivity.class);
                                go_main.putExtra("id", ID);
                                startActivity(go_main);
                            } catch(Exception e) {
                                Toast.makeText(BoardWriter.this,"게시물 작성에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }).setNegativeButton("아니오",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
            AlertDialog alert = alert_confirm.create();
            alert.show();
        }
    };

    Button.OnClickListener cancel_event = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(BoardWriter.this);
            alert_confirm.setMessage("작성을 취소하시겠습니까?")
                    .setCancelable(false).setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent go_main = new Intent(BoardWriter.this, MainActivity.class);
                            go_main.putExtra("id", ID);
                            startActivity(go_main);
                        }
                    }).setNegativeButton("취소",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
            AlertDialog alert = alert_confirm.create();
            alert.show();
        }
    };

    Button.OnClickListener findLocal = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getUpperLocalFilter();
        }
    };

    Button.OnClickListener jobFilter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getJobFilter();
        }
    };

    protected void sendBoardInfo()  {
        sendRegistBoardStream();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                SERVER_REGIST_BOARD, request_board_register, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse (JSONObject response){
                return;
            }
        }
                ,new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(BoardWriter.this,
                        "서버와 연결이 되지 않습니다. 지역 정보 받아오기에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(request);
    }

    protected void sendRegistBoardStream() {
        StringBuffer start = new StringBuffer();
        StringBuffer end = new StringBuffer();

        start.append(st_year).append(".").append(st_month).append(".").append(st_day).append(" ")
                .append(st_hour).append(":").append(st_min);
        end.append(ed_year).append(".").append(ed_month).append(".").append(ed_day).append(" ")
                .append(ed_hour).append(":").append(ed_min);

        try {
            request_board_register = new JSONObject();
            request_board_register.put("id",ID);
            request_board_register.put("title", board_title.getText().toString());
            request_board_register.put("storename", store_name.getText().toString());
            request_board_register.put("start_time", start.toString());
            request_board_register.put("end_time", end.toString());
            request_board_register.put("urgency", urgency.getText().toString());
            request_board_register.put("job_condition", condition.getText().toString());
            request_board_register.put("job", selected_job);
            request_board_register.put("favorable_condition", favorable_condition.getText().toString());
            request_board_register.put("detail", detail.getText().toString());
            request_board_register.put("local", lower_local);
            request_board_register.put("local_sub", local_sub.getText().toString());
            request_board_register.put("phone", phone.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void getUpperLocalFilter()  {
        String url = SERVER_LOCAL_FILTER;
        try {
            String temp = URLEncoder.encode("대한민국", "utf-8");
            url = SERVER_LOCAL_FILTER + temp;
        } catch (UnsupportedEncodingException e){
            Toast.makeText(BoardWriter.this,e.toString(),Toast.LENGTH_SHORT).show();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse (JSONObject response){
                mResult = response;
                try {
                    getUpperLocalList();
                    getUpperLocalDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
                ,new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(BoardWriter.this,
                        "서버와 연결이 되지 않습니다. 지역 정보 받아오기에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(request);
    }

    protected void getLowerLocalFilter()  {
        String url = SERVER_LOCAL_FILTER;
        try {
            String temp = URLEncoder.encode(upper_local, "utf-8");
            url = SERVER_LOCAL_FILTER + temp;
        } catch (UnsupportedEncodingException e){
            Toast.makeText(BoardWriter.this,e.toString(),Toast.LENGTH_SHORT).show();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse (JSONObject response){
                mResult = response;
                try {
                    getLowerLocalList();
                    getLowerLocalDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
                ,new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(BoardWriter.this,
                        "서버와 연결이 되지 않습니다. 지역 정보 받아오기에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        mQueue.add(request);
    }

    protected void getUpperLocalDialog(){
        final CharSequence[] items =  upperLocalList.toArray(new String[upperLocalList.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(BoardWriter.this);
        builder.setTitle("지역 선택");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                upper_local = items[pos].toString();
                upper_local_text = upper_local;
                getLowerLocalFilter();
            }
        });
        builder.show();
    }

    protected void getLowerLocalDialog(){
        final CharSequence[] items =  lowerLocalList.toArray(new String[lowerLocalList.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(BoardWriter.this);
        builder.setTitle("지역 선택");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                lower_local = items[pos].toString();
                upper_local_text += " "+ lower_local;
                local_selector.setText(upper_local_text);
            }
        });
        builder.show();
    }

    protected void getUpperLocalList() throws JSONException {
        upperLocalList.clear();
        JSONArray upperLocal = mResult.getJSONArray("local_list");
        for(int i=0; i<upperLocal.length(); i++){
            String node = upperLocal.get(i).toString();
            upperLocalList.add(node);
        }
    }


    protected void getJobFilter()  {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                SERVER_JOB_FILTER, null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse (JSONObject response){
                mResult = response;
                try {
                    getJobList();
                    getJobDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
                ,new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(BoardWriter.this,
                        "서버와 연결이 되지 않습니다. 지역 정보 받아오기에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        mQueue.add(request);
    }

    protected void getJobList() throws JSONException {
        jobList.clear();
        JSONArray upperLocal = mResult.getJSONArray("job_list");
        for(int i=0; i<upperLocal.length(); i++){
            String node = upperLocal.get(i).toString();
            jobList.add(node);
        }
    }


    protected void getJobDialog(){
        final CharSequence[] items = jobList.toArray(new String[jobList.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(BoardWriter.this);
        builder.setTitle("직종 선택");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                selected_job = items[pos].toString();
            }
        });
        builder.show();
    }

    protected void getLowerLocalList() throws JSONException {
        lowerLocalList.clear();
        JSONArray upperLocal = mResult.getJSONArray("local_list");
        for(int i=0; i<upperLocal.length(); i++){
            String node = upperLocal.get(i).toString();
            lowerLocalList.add(node);
        }
    }

    private void intializeSpinner(){
        for(int i=1900; i<=2018; i++){
            st_yearList.add(Integer.toString(i));
            ed_yearList.add(Integer.toString(i));
        }

        st_yearSpinnerAdapter = new ArrayAdapter<String>(BoardWriter.this, android.R.layout.simple_spinner_item, st_yearList);
        ed_yearSpinnerAdapter = new ArrayAdapter<String>(BoardWriter.this, android.R.layout.simple_spinner_item, ed_yearList);
        start_year.setAdapter(st_yearSpinnerAdapter);
        end_year.setAdapter(ed_yearSpinnerAdapter);

        for(int i=1; i<=12; i++){
            st_monthList.add(Integer.toString(i));
            ed_monthList.add(Integer.toString(i));
        }

        st_monthSpinnerAdapter = new ArrayAdapter<String>(BoardWriter.this, android.R.layout.simple_spinner_item, st_monthList);
        ed_monthSpinnerAdapter = new ArrayAdapter<String>(BoardWriter.this, android.R.layout.simple_spinner_item, ed_monthList);
        start_mon.setAdapter(st_monthSpinnerAdapter);
        end_mon.setAdapter(ed_monthSpinnerAdapter);

        for(int i=1; i<=31; i++){
            st_dayList.add(Integer.toString(i));
            ed_dayList.add(Integer.toString(i));
        }

        st_daySpinnerAdapter = new ArrayAdapter<String>(BoardWriter.this, android.R.layout.simple_spinner_item, st_dayList);
        ed_daySpinnerAdapter = new ArrayAdapter<String>(BoardWriter.this, android.R.layout.simple_spinner_item, ed_dayList);
        start_day.setAdapter(st_daySpinnerAdapter);
        end_day.setAdapter(ed_daySpinnerAdapter);

        for(int i=0; i<=23; i++){
            st_hourList.add(Integer.toString(i));
            ed_hourList.add(Integer.toString(i));
        }

        st_hourSpinnerAdapter = new ArrayAdapter<String>(BoardWriter.this, android.R.layout.simple_spinner_item, st_hourList);
        ed_hourSpinnerAdapter = new ArrayAdapter<String>(BoardWriter.this, android.R.layout.simple_spinner_item, ed_hourList);
        start_hour.setAdapter(st_hourSpinnerAdapter);
        end_hour.setAdapter(ed_hourSpinnerAdapter);

        for(int i=0; i<=59; i++){
            st_minList.add(Integer.toString(i));
            ed_minList.add(Integer.toString(i));
        }

        st_minSpinnerAdapter = new ArrayAdapter<String>(BoardWriter.this, android.R.layout.simple_spinner_item, st_minList);
        ed_minSpinnerAdapter = new ArrayAdapter<String>(BoardWriter.this, android.R.layout.simple_spinner_item, ed_minList);
        start_min.setAdapter(st_minSpinnerAdapter);
        end_min.setAdapter(ed_minSpinnerAdapter);

        genderList.add("남자");
        genderList.add("여자");
        genderSpinnerAdapter = new ArrayAdapter<String>(BoardWriter.this, android.R.layout.simple_spinner_item, genderList);
        gender.setAdapter(genderSpinnerAdapter);
    }

    private void setEventToComponents(){
        local_selector.setOnClickListener(findLocal);
        job.setOnClickListener(jobFilter);
        gender.setOnItemSelectedListener(selected_gender);

        start_year.setOnItemSelectedListener(selected_start_year);
        start_mon.setOnItemSelectedListener(selected_start_mon);
        start_day.setOnItemSelectedListener(selected_start_day);
        start_hour.setOnItemSelectedListener(selected_start_hour);
        start_min.setOnItemSelectedListener(selected_start_min);

        end_year.setOnItemSelectedListener(selected_end_year);
        end_mon.setOnItemSelectedListener(selected_end_mon);
        end_day.setOnItemSelectedListener(selected_end_day);
        end_hour.setOnItemSelectedListener(selected_end_hour);
        end_min.setOnItemSelectedListener(selected_end_min);

        ok_button.setOnClickListener(ok_event);
        cancel_button.setOnClickListener(cancel_event);
    }

    private void initializeView(){
        board_title = (EditText)findViewById(R.id.bw_title);
        store_name = (EditText)findViewById(R.id.bw_storename);
        local_selector = (Button)findViewById(R.id.bw_sido);
        local_sub = (EditText)findViewById(R.id.bw_positiondetail);
        phone = (EditText)findViewById(R.id.bw_telephone);

        start_year = (Spinner)findViewById(R.id.bw_year);
        start_mon = (Spinner)findViewById(R.id.bw_month);
        start_day = (Spinner)findViewById(R.id.bw_day);
        start_hour = (Spinner)findViewById(R.id.bw_hour);
        start_min = (Spinner)findViewById(R.id.bw_min);

        end_year = (Spinner)findViewById(R.id.bw_end_year);
        end_mon = (Spinner)findViewById(R.id.bw_end_month);
        end_day = (Spinner)findViewById(R.id.bw_end_day);
        end_hour = (Spinner)findViewById(R.id.bw_end_hour);
        end_min = (Spinner)findViewById(R.id.bw_end_min);

        urgency = (EditText)findViewById(R.id.bw_money);
        gender = (Spinner)findViewById(R.id.bw_gender);

        condition = (EditText)findViewById(R.id.bw_condition);
        job = (Button)findViewById(R.id.bw_job);
        favorable_condition = (EditText)findViewById(R.id.bw_plus);
        detail = (EditText)findViewById(R.id.bw_addinformation);

        ok_button = (Button)findViewById(R.id.bw_okButton);
        cancel_button = (Button)findViewById(R.id.bw_cancelButton);
    }
}