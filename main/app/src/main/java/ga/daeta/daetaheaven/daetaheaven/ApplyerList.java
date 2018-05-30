package ga.daeta.daetaheaven.daetaheaven;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class ApplyerList extends Activity{
    private String ID = null;

    protected String SERVER_APPLYING = "http://daeta.ga/supporters?id=";
    protected RequestQueue mQueue = null;
    protected ArrayList<Applyer> mArray = new ArrayList<Applyer>();
    protected ApplyerAdapter mAdapter = null;
    protected ListView mList = null;
    protected JSONObject mResult = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applyer_list);

        mList = (ListView)findViewById(R.id.applying_list);
        mAdapter = new ApplyerAdapter(this, R.layout.applyer_list_item);
        mList.setAdapter(mAdapter);

        mQueue = Volley.newRequestQueue(this);
        requestJSON();
    }
    protected void requestJSON(){
        String url = SERVER_APPLYING + ID;

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
                Toast.makeText(ApplyerList.this,
                        "지원 중인 대타목록을 받아오는데 실패하였습니다.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        mQueue.add(request);
    }

    protected void drawList() {
        mArray.clear();
        try {
            JSONArray list = mResult.getJSONArray("applyer");
            for(int i=0;i<list.length();i++){
                JSONObject node = list.getJSONObject(i);
                int no = Integer.parseInt(node.getString("no"));
                String storename = node.getString("storename");
                String id = node.getString("id");
                String gender = node.getString("gender");
                String local = node.getString("local");
                String phone = node.getString("phone");

                mArray.add(new Applyer(no, storename, id, gender, local, phone));
            }
        } catch (JSONException | NullPointerException e) {
            Toast.makeText(this,"대타 지원자 목록 받아오기에 실패했습니다.", Toast.LENGTH_SHORT).show();
            mResult = null;
        }
        mAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onStop() {
        super.onStop();

        if(mQueue != null)
            mQueue.cancelAll("hello:");
    }

    public class Applyer {
        int no;
        String storename;
        String id;
        String gender;
        String local;
        String phone;

        public Applyer(int no, String storename, String id, String gender, String local, String phone) {
            this.no = no;
            this.storename = storename;
            this.id = id;
            this.gender = gender;
            this.local = local;
            this.phone = phone;
        }

        public int getNo() { return no; }
        public String getStorename() { return storename; }
        public String get_id(){ return id; }
        public String getGender() { return gender; }
        public String getLocal() { return local; }
        public String get_phone() { return phone; }
    }

    static class ApplyerViewHolder {
        TextView txStorename;
        TextView txID;
        TextView txGender;
        TextView txLocal;
        TextView txPhone;
    }

    public class ApplyerAdapter extends ArrayAdapter<Applyer> {
        LayoutInflater mInflater = null;

        public ApplyerAdapter(Context context, int resource){
            super(context,resource);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mArray.size();
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {
            ApplyerViewHolder viewHolder;

            if (v == null) {
                v = mInflater.inflate(R.layout.applying_list_item ,parent, false);

                viewHolder = new ApplyerViewHolder();
                viewHolder.txStorename = (TextView)v.findViewById(R.id.applyer_storename);
                viewHolder.txID = (TextView)v.findViewById(R.id.applyer_id);
                viewHolder.txGender = (TextView)v.findViewById(R.id.applyer_gender);
                viewHolder.txLocal = (TextView)v.findViewById(R.id.applyer_local);
                viewHolder.txPhone = (TextView)v.findViewById(R.id.applyer_phone);
                v.setTag(viewHolder);
            }
            else {
                viewHolder = (ApplyerViewHolder) v.getTag();
            }

            Applyer info = mArray.get(position);
            if(info != null){
                viewHolder.txStorename.setText(info.getStorename());
                viewHolder.txID.setText(info.get_id());
                viewHolder.txGender.setText(info.getGender());
                viewHolder.txLocal.setText(info.getLocal());
                viewHolder.txPhone.setText(info.get_phone());
            }
            return v;
        }
    }
}
