package com.example.daekyo.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected ArrayList<SimpleBoard> mArray = new ArrayList<SimpleBoard>();
    protected SimpleBoardAdapter mAdapter = null;
    protected RequestQueue mQueue = null;
    protected ListView mList = null;
    protected JSONObject mResult = null;

    private String TESTTAG = "TEST=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton imagebutton =(ImageButton)findViewById(R.id.imagebutton); // 대타를 구하고 계신가요? 이미지버튼
                                                                                // 버튼 바꾸고싶으면 사진만 수정하면 됨
        imagebutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WriteBoard.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mList = (ListView) findViewById(R.id.boards);
        mAdapter = new SimpleBoardAdapter(this, R.layout.simple_board);
        mList.setAdapter(mAdapter);

        mQueue = Volley.newRequestQueue(this);
        requestJSON();
    }

    protected void requestJSON(){/*
        mArray.clear();
        mArray.add(new SimpleBoard("5","분식빠","2018.05.09","15:00","2018.05.09","21:00","8000"));
        mArray.add(new SimpleBoard("4","쨈나pc방","2018.05.10","12:00","2018.05.11","21:00","8500"));
        mArray.add(new SimpleBoard("3","세븐일레븐","2018.05.15","11:00","2018.05.15","18:00","8000"));
        mArray.add(new SimpleBoard("2","CU 금오공대점","2018.05.10","10:00","2018.05.10","18:00","8300"));
        mArray.add(new SimpleBoard("1","GS 금오공대점","2018.05.11","18:00","2018.05.11","23:00","8100"));
        mAdapter.notifyDataSetChanged();*/
        String url = "http://192.168.0.103:8000/home";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse (JSONObject response){
                mResult = response;
                drawList();
            }
        }
                ,new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(MainActivity.this,
                        "서버에서 예기치 못한 오류가 발생하였습니다.",
                        Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.login) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
        } else if (id == R.id.join) {
            Intent intent = new Intent(getApplicationContext(), Signup.class);
            startActivity(intent);
        } else if (id == R.id.settings) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void drawList() {
        mArray.clear();
        try {
            JSONArray list = mResult.getJSONArray("boards");
            for(int i=0;i<list.length();i++){
                JSONObject node = list.getJSONObject(i);
                String no = node.getString("no");
                String storename = node.getString("storename");
                String start_date = node.getString("start_date");
                String start_time = node.getString("start_time");
                String end_date = node.getString("end_date");
                String end_time = node.getString("end_time");
                String urgency = node.getString("urgency");

                mArray.add(new SimpleBoard(no, storename, start_date,start_time, end_date, end_time, urgency));
                Log.i(TESTTAG, "storename=" + storename + "start_date=" + start_date + "start_time="
                        + start_time + "end_date=" + end_date + "end_time=" + end_time +"urgency=" + urgency);
            }
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(this,"Error: "+ e.toString(), Toast.LENGTH_SHORT).show();
            mResult = null;
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mQueue != null)
            mQueue.cancelAll(TESTTAG);
    }

    static class SimpleBoardViewHolder {
        TextView VHno;
        TextView VHstorename;
        TextView VHstart_date;
        TextView VHstart_time;
        TextView VHend_date;
        TextView VHend_time;
        TextView VHurgency;
    }

    public class SimpleBoard {
        String no = null;
        String storename = null;
        String start_date = null;
        String start_time = null;
        String end_date = null;
        String end_time = null;
        String urgency = null;

        public SimpleBoard(String no, String storename, String start_date, String start_time,
                           String end_date, String end_time, String urgency){
            this.no = no;
            this.storename = storename;
            this.start_date = start_date;
            this.start_time = start_time;
            this.end_date = end_date;
            this.end_time = end_time;
            this.urgency = urgency;
        }
        public String getNo() {
            return no;
        }
        public String getStorename() {
            return storename;
        }
        public String getStart_date() {
            return start_date;
        }
        public String getStart_time() {
            return start_time;
        }
        public String getEnd_date() {
            return end_date;
        }
        public String getEnd_time() {
            return end_time;
        }
        public String getUrgency() {
            return urgency;
        }
    }

    public class SimpleBoardAdapter extends ArrayAdapter<SimpleBoard> {
        LayoutInflater mInflater = null;

        public SimpleBoardAdapter(Context context, int resource){
            super(context,resource);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mArray.size();
        }

        @Override
        public View getView(int position, View v, ViewGroup parent ) {
            SimpleBoardViewHolder viewHolder;

            if (v == null) {
                v = mInflater.inflate(R.layout.simple_board ,parent, false);

                viewHolder = new SimpleBoardViewHolder();
                viewHolder.VHno = (TextView) v.findViewById(R.id.simple_board_no);
                viewHolder.VHstorename = (TextView) v.findViewById(R.id.simple_board_storename);
                viewHolder.VHstart_date = (TextView) v.findViewById(R.id.simple_board_start_date);
                viewHolder.VHstart_time = (TextView) v.findViewById(R.id.simple_board_start_time);
                viewHolder.VHend_date = (TextView) v.findViewById(R.id.simple_board_end_date);
                viewHolder.VHend_time = (TextView) v.findViewById(R.id.simple_board_end_time);
                viewHolder.VHurgency = (TextView) v.findViewById(R.id.simple_board_urgency);
                v.setTag(viewHolder);
            }
            else {
                viewHolder = (SimpleBoardViewHolder)v.getTag();
            }

            SimpleBoard info = mArray.get(position);

            if(info != null){
                viewHolder.VHno.setText(info.getNo());
                viewHolder.VHstorename.setText(info.getStorename());
                viewHolder.VHstart_date.setText(info.getStart_date()+" ");
                viewHolder.VHstart_time.setText(info.getStart_time());
                viewHolder.VHend_date.setText(info.getEnd_date()+" ");
                viewHolder.VHend_time.setText(info.getEnd_time());
                viewHolder.VHurgency.setText(info.getUrgency());
            }
            return v;
        }
    }
}