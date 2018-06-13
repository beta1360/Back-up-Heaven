package ga.daeta.daetaheaven.daetaheaven;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ApplyingList  extends Activity implements AdapterView.OnItemClickListener{
    private String ID = null;

    protected String SERVER_APPLYING = "http://daeta.ga/applying?id=";
    protected RequestQueue mQueue = null;
    protected ArrayList<Applying> mArray = new ArrayList<Applying>();
    protected ApplyingAdapter mAdapter = null;
    protected ListView mList = null;
    protected JSONObject mResult = null;
    protected ImageLoader mImageLoader = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applying_daeta);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            ID = bundle.getString("ID");

        mList = (ListView)findViewById(R.id.applying_list);
        mAdapter = new ApplyingAdapter(this, R.layout.applying_list_item);
        mList.setAdapter(mAdapter);

        mQueue = Volley.newRequestQueue(this);
        mImageLoader = new ImageLoader(mQueue, new LruBitmapCache(this));
        requestJSON();

        mList.setOnItemClickListener(this);

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
                Toast.makeText(ApplyingList.this,
                        "지원 중인 대타목록을 받아오는데 실패하였습니다.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        mQueue.add(request);
    }

    protected void drawList() {
        mArray.clear();
        try {
            JSONArray list = mResult.getJSONArray("applying");
            for(int i=0;i<list.length();i++){
                JSONObject node = list.getJSONObject(i);
                int no = Integer.parseInt(node.getString("no"));
                String storename = node.getString("storename");
                String start_time = node.getString("start_time");
                String end_time = node.getString("end_time");
                String local = node.getString("local");

                mArray.add(new Applying(no, storename, start_time, end_time, local));
                Log.e("hello:", storename);
            }
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(this,"Error: "+ e.toString(), Toast.LENGTH_SHORT).show();
            mResult = null;
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent detail = new Intent(this, DetailBoard.class);
        detail.putExtra("ID", ID);
        detail.putExtra("no", mArray.get(position).getNo());
        startActivity(detail);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mQueue != null)
            mQueue.cancelAll("hello:");
    }

    public class Applying {
        int no;
        String storename;
        String start_time;
        String end_time;
        String local;

        public Applying(int no, String storename, String start_time, String end_time, String local) {
            this.no = no;
            this.storename = storename;
            this.start_time = start_time;
            this.end_time = end_time;
            this.local = local;
        }

        public int getNo() { return no; }
        public String getStorename() { return storename; }
        public String getStart_time() { return start_time; }
        public String getEnd_time() { return end_time; }
        public String getLocal() { return local; }
    }

    static class ApplyingViewHolder {
        NetworkImageView image;
        TextView txStorename;
        TextView txStart;
        TextView txEnd;
        TextView txLocal;
    }

    public class ApplyingAdapter extends ArrayAdapter<Applying> {
        LayoutInflater mInflater = null;

        public ApplyingAdapter(Context context, int resource){
            super(context,resource);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mArray.size();
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {
            ApplyingViewHolder viewHolder;

            if (v == null) {
                v = mInflater.inflate(R.layout.applying_list_item ,parent, false);

                viewHolder = new ApplyingViewHolder();
                viewHolder.image = (NetworkImageView)v.findViewById(R.id.applying_image);
                viewHolder.txStorename = (TextView)v.findViewById(R.id.applying_storename);
                viewHolder.txStart = (TextView)v.findViewById(R.id.applying_start_time);
                viewHolder.txEnd = (TextView)v.findViewById(R.id.applying_end_time);
                viewHolder.txLocal = (TextView)v.findViewById(R.id.applying_local);
                v.setTag(viewHolder);
            }
            else {
                viewHolder = (ApplyingViewHolder)v.getTag();
            }

            Applying info = mArray.get(position);
            if(info != null){
                String img = "http://daeta.ga/image/"+ Integer.toString(info.getNo()) +".png";
                viewHolder.image.setImageUrl(img, mImageLoader);
                viewHolder.txStorename.setText(info.getStorename());
                viewHolder.txStart.setText(info.getStart_time());
                viewHolder.txEnd.setText(info.getEnd_time());
                viewHolder.txLocal.setText(info.getLocal());
            }
            return v;
        }
    }
}
