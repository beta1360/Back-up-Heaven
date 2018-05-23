package ga.daeta.daetaheaven.daetaheaven;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;
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

public class DetailBoard extends Activity {
    private String ID = null;
    protected int no;
    protected RequestQueue mQueue = null;
    protected JSONObject mResult = null;
    protected String SERVER_BOARD = "http://daeta.ga/board?no=";

    protected TextView title = null;
    protected TextView local = null;
    protected TextView storename = null;
    protected TextView phone = null;
    protected TextView start_time = null;
    protected TextView end_time = null;
    protected TextView urgency = null;
    protected TextView sex = null;
    protected TextView condition = null;
    protected TextView job = null;
    protected TextView favor = null;
    protected TextView add_info = null;
    protected TextView detail_main = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_board);

        Bundle bundle = getIntent().getExtras();

        if( bundle != null) {
            no = bundle.getInt("no");
            ID = bundle.getString("ID");
        }

        title = (TextView) findViewById(R.id.detail_title);
        local = (TextView) findViewById(R.id.detail_position);
        storename = (TextView) findViewById(R.id.detail_storename);
        phone = (TextView) findViewById(R.id.detail_telephone);
        start_time = (TextView) findViewById(R.id.detail_start_time);
        end_time = (TextView) findViewById(R.id.detail_end_time);
        urgency = (TextView) findViewById(R.id.detail_money);
        sex = (TextView) findViewById(R.id.detail_gender);
        condition = (TextView) findViewById(R.id.detail_condition);
        job = (TextView) findViewById(R.id.detail_job);
        favor = (TextView) findViewById(R.id.detail_plus);
        add_info = (TextView) findViewById(R.id.detail_addinformation);
        detail_main = (TextView) findViewById(R.id.detail_main);

        SERVER_BOARD = SERVER_BOARD + Integer.toString(no);
        mQueue = Volley.newRequestQueue(this);
        getDetailBoard();
    }

    protected void getDetailBoard() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                SERVER_BOARD, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mResult = response;
                drawBoard();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetailBoard.this,
                                "서버로부터 대타구인글을 받지 못했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
        mQueue.add(request);
    }

    @SuppressLint("SetTextI18n")
    protected void drawBoard() {
        try {
            int res_no = mResult.getInt("no");
            String res_title = mResult.getString("title");
            String res_storename = mResult.getString("storename");
            String res_start_time = mResult.getString("start_time");
            String res_end_time = mResult.getString("end_time");
            String res_urgency = Integer.toString(mResult.getInt("urgency"));
            String res_job_condition = mResult.getString("job_condition") ;
            String res_job = mResult.getString("job");
            String res_favor = mResult.getString("favorable_condition");
            String res_detail = mResult.getString("detail");
            String res_address = mResult.getString("address");
            String res_id = mResult.getString("id");
            String res_phone = mResult.getString("phone");

            title.setText(res_title);
            local.setText(res_address);
            storename.setText(res_storename);
            phone.setText(res_phone);
            start_time.setText(res_start_time);
            end_time.setText(res_end_time);
            urgency.setText(res_urgency);
            condition.setText(res_job_condition);
            job.setText(res_job);
            favor.setText(res_favor);
            add_info.setText(res_detail);
            phone.setText(res_phone);
            detail_main.setText(res_storename +"의 구인정보");
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
            mResult = null;
        }
    }
}


