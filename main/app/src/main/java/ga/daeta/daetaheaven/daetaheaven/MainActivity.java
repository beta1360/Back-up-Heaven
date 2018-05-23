package ga.daeta.daetaheaven.daetaheaven;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {
    private String ID = null;

    protected String upper_local_filter = "대한민국";
    protected String lower_local_filter;
    protected String job_filter = null;
    protected RequestQueue mQueue = null;
    protected JSONObject mResult = null;
    protected Button upper_local = null;
    protected Button lower_local = null;
    protected Button job = null;
    protected ListView boards = null;
    protected ArrayList<SimpleStoreInfo> mArray = new ArrayList<SimpleStoreInfo>();
    protected ArrayList<String> upperLocalList = new ArrayList<String>();
    protected ArrayList<String> lowerLocalList = new ArrayList<String>();
    protected ArrayList<String> jobList = new ArrayList<String>();
    protected SimpleStoreInfoAdapter mAdapter = null;

    protected final String SERVER_HOME = "http://daeta.ga/home";
    protected final String SERVER_LOCAL_FILTER = "http://daeta.ga/locallist?filter=";
    protected final String SERVER_JOB_FILTER = "http://daeta.ga/joblist";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null)
            ID = savedInstanceState.getString("ID");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        upper_local = (Button) findViewById(R.id.upper_local);
        lower_local = (Button) findViewById(R.id.lower_local);
        job = (Button) findViewById(R.id.job);

        boards = (ListView) findViewById(R.id.boards);
        mAdapter = new SimpleStoreInfoAdapter(this, R.layout.simple_board);
        boards.setAdapter(mAdapter);

        mQueue = Volley.newRequestQueue(this);
        requestBoardList(null, null);

        boards.setOnItemClickListener(this);
        upper_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUpperLocalFilter();
            }
        });
        lower_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLowerLocalFilter();
            }
        });
        job.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getJobFilter();
            }
        });
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
            Intent intent = new Intent(MainActivity.this, Login.class);
            intent.putExtra("id", ID);
            startActivity(intent);
        } else if (id == R.id.join) {
            Intent intent = new Intent(MainActivity.this, Signup.class);
            intent.putExtra("id", ID);
            startActivity(intent);
        } else if (id == R.id.settings) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void getUpperLocalFilter()  {
        String url = SERVER_LOCAL_FILTER;
        try {
            String temp = URLEncoder.encode("대한민국", "utf-8");
            url = SERVER_LOCAL_FILTER + temp;
        } catch (UnsupportedEncodingException e){
            Toast.makeText(MainActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this,
                        "서버와 연결이 되지 않습니다. 지역 정보 받아오기에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        mQueue.add(request);
    }

    protected void getLowerLocalFilter()  {
        String url = SERVER_LOCAL_FILTER;
        try {
            String temp = URLEncoder.encode(upper_local_filter, "utf-8");
            url = SERVER_LOCAL_FILTER + temp;
        } catch (UnsupportedEncodingException e){
            Toast.makeText(MainActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this,
                        "서버와 연결이 되지 않습니다. 지역 정보 받아오기에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        mQueue.add(request);
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
                Toast.makeText(MainActivity.this,
                        "서버와 연결이 되지 않습니다. 직종 정보 받아오기에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        mQueue.add(request);
    }

    protected void getUpperLocalDialog(){
        final CharSequence[] items =  upperLocalList.toArray(new String[upperLocalList.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("지역 선택");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                upper_local_filter = items[pos].toString();
                upper_local.setText(upper_local_filter);
                upper_local.setTextColor(Color.BLUE);
                upper_local.setTypeface(Typeface.DEFAULT_BOLD);
                requestBoardList(upper_local_filter, job_filter);
                mAdapter.notifyDataSetChanged();

                lower_local.setText("시/군/구");
                lower_local.setTextColor(Color.BLACK);
                lower_local.setTypeface(Typeface.DEFAULT);
                lower_local_filter = null;
            }
        });
        builder.show();
    }

    protected void getLowerLocalDialog(){
        final CharSequence[] items =  lowerLocalList.toArray(new String[lowerLocalList.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("지역 선택");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                lower_local_filter = items[pos].toString();
                lower_local.setText(lower_local_filter);
                lower_local.setTextColor(Color.BLUE);
                lower_local.setTypeface(Typeface.DEFAULT_BOLD);
                requestBoardList(lower_local_filter, job_filter);
                mAdapter.notifyDataSetChanged();
            }
        });
        builder.show();
    }

    protected void getJobDialog(){
        final CharSequence[] items =  jobList.toArray(new String[jobList.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("직종 선택");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                job_filter = items[pos].toString();
                job.setText(job_filter);
                job.setTextColor(Color.BLUE);
                job.setTypeface(Typeface.DEFAULT_BOLD);
                if(lower_local_filter != null)
                    requestBoardList(lower_local_filter, job_filter);
                else
                    requestBoardList(upper_local_filter, job_filter);
                mAdapter.notifyDataSetChanged();
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

    protected void getLowerLocalList() throws JSONException {
        lowerLocalList.clear();
        JSONArray upperLocal = mResult.getJSONArray("local_list");
        for(int i=0; i<upperLocal.length(); i++){
            String node = upperLocal.get(i).toString();
            lowerLocalList.add(node);
        }
    }

    protected void getJobList() throws JSONException {
        jobList.clear();
        JSONArray upperLocal = mResult.getJSONArray("job_list");
        for(int i=0; i<upperLocal.length(); i++){
            String node = upperLocal.get(i).toString();
            jobList.add(node);
        }
    }

    protected void requestBoardList(String local_filter, String job_filter){
        String SERVER_URL = SERVER_HOME;
        if(local_filter != null && job_filter == null){
            try {
                local_filter = URLEncoder.encode(local_filter, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            SERVER_URL = SERVER_URL +"?local="+local_filter;
        } else if(local_filter == null && job_filter != null){
            try {
                job_filter = URLEncoder.encode(job_filter, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            SERVER_URL = SERVER_URL +"?job="+job_filter;
        } else if(local_filter != null && job_filter != null){
            try {
                local_filter = URLEncoder.encode(local_filter, "utf-8");
                job_filter = URLEncoder.encode(job_filter, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            SERVER_URL = SERVER_URL +"?local="+local_filter+"&job="+job_filter;
        } else {
            SERVER_URL = SERVER_HOME;
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                SERVER_URL, null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse (JSONObject response){
                mResult = response;
                drawBoard();
            }
        }
        ,new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(MainActivity.this,
                        "서버와 연결이 되지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        mQueue.add(request);
    }

    protected void drawBoard() {
        mArray.clear();
        try{
            JSONArray list = mResult.getJSONArray("boards");
            for(int i=0; i<list.length(); i++){
                JSONObject node = list.getJSONObject(i);
                String storename = node.getString("storename");
                String address = node.getString("address");
                String start_time = node.getString("start_time");
                String end_time = node.getString("end_time");
                String urgency = node.getString("urgency");
                int no = node.getInt("no");

                mArray.add(new SimpleStoreInfo(storename, address, start_time, end_time, urgency, no));
                Log.i("list :","storename : "+storename);
            }
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(this,
                    "서버로부터 잘못된 데이터를 수신하였습니다.",Toast.LENGTH_SHORT).show();
            mResult = null;
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent detail = new Intent(this, DetailBoard.class);
        detail.putExtra("id", ID);
        detail.putExtra("no", mArray.get(i).getNo());
        startActivity(detail);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mQueue != null)
            mQueue.cancelAll("onStop(): ");
    }

    public class SimpleStoreInfo {
        String storename;
        String address;
        String start_time;
        String end_time;
        String urgency;
        int no;

        public SimpleStoreInfo(String storename, String address, String start_time, String end_time, String urgency, int no){
            this.storename = storename;
            this.address = address;
            this.start_time = start_time;
            this.end_time = end_time;
            this.urgency = urgency;
            this.no = no;
        }

        public String getStorename() { return storename; }
        public String getAddress() { return address; }
        public String getStart_time() { return start_time; }
        public String getEnd_time() { return end_time; }
        public String getUrgency() { return urgency; }
        public int getNo() { return no; }
    }

    static class SimpleStoreViewHolder {
        TextView vhStorename;
        TextView vhAddress;
        TextView vhStartTime;
        TextView vhEndTime;
        TextView vhUrgency;
    }

    public class SimpleStoreInfoAdapter extends ArrayAdapter<SimpleStoreInfo>{
        LayoutInflater mInflater = null;

        public SimpleStoreInfoAdapter(Context context, int resource) {
            super(context, resource);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mArray.size();
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {
            SimpleStoreViewHolder viewHolder;

            if(v == null){
                v = mInflater.inflate(R.layout.simple_board, parent, false);

                viewHolder = new SimpleStoreViewHolder();
                viewHolder.vhStorename = (TextView)v.findViewById(R.id.textView_list_storename);
                viewHolder.vhAddress = (TextView)v.findViewById(R.id.textView_list_address);
                viewHolder.vhStartTime = (TextView)v.findViewById(R.id.textView_start_time);
                viewHolder.vhEndTime = (TextView)v.findViewById(R.id.textView_end_time);
                viewHolder.vhUrgency = (TextView)v.findViewById(R.id.textView_list_money);
                v.setTag(viewHolder);
            }
            else {
                viewHolder = (SimpleStoreViewHolder) v.getTag();
            }

            SimpleStoreInfo info = mArray.get(position);
            if(info != null) {
                viewHolder.vhStorename.setText(info.getStorename());
                viewHolder.vhAddress.setText(info.getAddress());
                viewHolder.vhStartTime.setText(info.getStart_time());
                viewHolder.vhEndTime.setText("~"+info.getEnd_time());
                viewHolder.vhUrgency.setText("시급 : " + info.getUrgency() +"원");
            }
            return v;
        }
    }
}