package ga.daeta.daetaheaven.daetaheaven;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class Signup extends AppCompatActivity {
    private String ID = null;

    protected String upper_local_text = null;
    protected String upper_local = "대한민국";
    protected String lower_local = null;
    protected RequestQueue mQueue = null;
    protected JSONObject mResult = null;
    protected final String SERVER_SIGNUP = "http://daeta.ga/signup";
    protected final String SERVER_LOCAL_FILTER = "http://daeta.ga/locallist?filter=";
    protected JSONObject request_sign_up = null;

    protected ArrayList<String> upperLocalList = new ArrayList<String>();
    protected ArrayList<String> lowerLocalList = new ArrayList<String>();

    protected EditText IDText = null;
    protected EditText PassText = null;
    protected EditText ConfirmText = null;
    protected EditText NameText = null;
    protected Spinner sex = null;
    protected EditText BirthText = null;
    protected EditText PhoneText = null;
    protected TextView local = null;
    protected Button select_local = null;
    protected EditText local_sub = null;
    protected Spinner JobText = null;
    protected Button buttonok = null;

    protected ArrayList<String> sexList = new ArrayList<>();
    protected ArrayAdapter<String> sexSpinnerAdapter = null;
    protected ArrayList<String> jobList = new ArrayList<>();
    protected ArrayAdapter<String> jobSpinnerAdapter = null;
    protected String id = null;
    protected String pwd = null;
    protected String name = null;
    protected String local_main = null;
    protected String local_sub_ = null;
    protected String birthday = null;
    protected String phone = null;
    protected String selectedSex = null;
    protected String selectedJob = null;

    //aa
    ArrayAdapter<CharSequence> adspin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            ID = bundle.getString("ID");

        IDText = (EditText) findViewById(R.id.IDText);
        PassText = (EditText) findViewById(R.id.PassText);
        ConfirmText = (EditText) findViewById(R.id.ConfirmText);
        NameText = (EditText) findViewById(R.id.NameText);
        sex = (Spinner) findViewById(R.id.SexText);
        BirthText = (EditText) findViewById(R.id.BirthText);
        PhoneText = (EditText) findViewById(R.id.PhoneText);
        JobText = (Spinner) findViewById(R.id.JobText);
        local_sub = (EditText) findViewById(R.id.signup_sub_addr);
        local = (TextView) findViewById(R.id.signup_addr);
        select_local = (Button) findViewById(R.id.selct_addr);

        sexList.add("남자");
        sexList.add("여자");
        sexSpinnerAdapter = new ArrayAdapter<String>(Signup.this, android.R.layout.simple_spinner_item, sexList);
        sex.setAdapter(sexSpinnerAdapter);
        sex.setOnItemSelectedListener(sexSelecter);

        jobList.add("매장관리");
        jobList.add("서빙");
        jobList.add("주방");
        jobList.add("배달");
        jobList.add("사무보조");
        jobList.add("노무");
        jobSpinnerAdapter = new ArrayAdapter<String>(Signup.this, android.R.layout.simple_spinner_item, jobList);
        JobText.setAdapter(jobSpinnerAdapter);
        JobText.setOnItemSelectedListener(jobSelecter);

        mQueue = Volley.newRequestQueue(this);

        buttonok = (Button) findViewById(R.id.BtnJoin);
        buttonok.setOnClickListener(SignOK);
        select_local.setOnClickListener(findLocal);
    }
    Spinner.OnItemSelectedListener sexSelecter = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectedSex = sex.getItemAtPosition(position).toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {  }
    };

    Spinner.OnItemSelectedListener jobSelecter = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectedJob = JobText.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {  }
    };

    Button.OnClickListener SignOK = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pwd = PassText.getText().toString();
            String pass2 = ConfirmText.getText().toString();
            id = IDText.getText().toString();
            name = NameText.getText().toString();
            birthday = BirthText.getText().toString();
            phone = PhoneText.getText().toString();
            local_sub_ = local_sub.getText().toString();
            local_main = local.getText().toString();

            if(!pwd.equals(pass2))
                Toast.makeText(Signup.this,"비밀번호가 같지 않습니다.",Toast.LENGTH_SHORT).show();
            else if(id.length() == 0)
                Toast.makeText(Signup.this,"ID를 입력하지 않으셨습니다.",Toast.LENGTH_SHORT).show();
            else if(pwd.length() == 0)
                Toast.makeText(Signup.this,"패스워드를 입력하지 않으셨습니다.",Toast.LENGTH_SHORT).show();
            else if(pass2.length() == 0)
                Toast.makeText(Signup.this,"확인용 패스워드를 입력하지 않으셨습니다.",Toast.LENGTH_SHORT).show();
            else if(name.length() == 0)
                Toast.makeText(Signup.this,"이름을 입력하지 않으셨습니다.",Toast.LENGTH_SHORT).show();
            else if(selectedSex.length() == 0)
                Toast.makeText(Signup.this,"성별을 선택하지 않으셨습니다.",Toast.LENGTH_SHORT).show();
            else if(birthday.length() == 0)
                Toast.makeText(Signup.this,"생년월일을 입력하지 않으셨습니다.",Toast.LENGTH_SHORT).show();
            else if(phone.length() == 0)
                Toast.makeText(Signup.this,"연락처를 입력하지 않으셨습니다.",Toast.LENGTH_SHORT).show();
            else if(local_sub_.length() == 0)
                Toast.makeText(Signup.this,"상세 주소를 입력하지 않으셨습니다.",Toast.LENGTH_SHORT).show();
            else if(local_main.length() == 0)
                Toast.makeText(Signup.this,"상위 주소를 선택하지 않으셨습니다.",Toast.LENGTH_SHORT).show();
            else {
                signUp();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        }
    };

    Button.OnClickListener findLocal = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getUpperLocalFilter();
        }
    };

    protected void getUpperLocalFilter()  {
        String url = SERVER_LOCAL_FILTER;
        try {
            String temp = URLEncoder.encode("대한민국", "utf-8");
            url = SERVER_LOCAL_FILTER + temp;
        } catch (UnsupportedEncodingException e){
            Toast.makeText(Signup.this,e.toString(),Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Signup.this,
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
            Toast.makeText(Signup.this,e.toString(),Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Signup.this,
                        "서버와 연결이 되지 않습니다. 지역 정보 받아오기에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        mQueue.add(request);
    }

    protected void getUpperLocalDialog(){
        final CharSequence[] items =  upperLocalList.toArray(new String[upperLocalList.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
        builder.setTitle("지역 선택");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                lower_local = items[pos].toString();
                upper_local_text += " "+ lower_local;
                local.setText(upper_local_text);
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

    protected void signUp()  {
        signUpDataStream();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                SERVER_SIGNUP, request_sign_up, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse (JSONObject response){
                mResult = response;
                parseResult();
            }
        }
                ,new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(Signup.this,
                        "서버와 연결이 되지 않습니다. 회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(request);
    }

    protected void signUpDataStream() {
        try {
            request_sign_up = new JSONObject();
            request_sign_up.put("id",id);
            request_sign_up.put("pwd",pwd);
            request_sign_up.put("name",name);
            request_sign_up.put("gender",selectedSex);
            request_sign_up.put("birthday",birthday);
            request_sign_up.put("phone",phone);
            request_sign_up.put("local_main",lower_local);
            request_sign_up.put("local_sub",local_sub_);
            request_sign_up.put("job",selectedJob);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void parseResult(){
        try {
            String getResult = mResult.getString("message");
            if(getResult.equals("Registed user"))
                Toast.makeText(Signup.this,"이미 등록된 유저입니다.",Toast.LENGTH_SHORT).show();
            else if(getResult.equals("Duplicated ID"))
                Toast.makeText(Signup.this,"중복된 ID입니다",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(Signup.this,"회원 가입이 완료되었습니다!",Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}