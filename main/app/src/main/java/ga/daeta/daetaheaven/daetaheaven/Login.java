package ga.daeta.daetaheaven.daetaheaven;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    private String ID = null;

    protected RequestQueue mQueue = null;
    protected JSONObject mResult = null;
    protected final String SERVER_LOGIN = "http://daeta.ga/login";
    protected final String SERVER_LOGOUT = "http://daeta.ga/logout";
    protected EditText IDText = null;
    protected EditText PassText = null;
    protected Button BtnLogin= null;
    protected Button logout = null;
    protected Button FindPass = null;
    protected Button LoginSignup = null;

    protected JSONObject login_data = null;
    protected JSONObject logout_data = null;

    protected String id;
    protected String pwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            ID = bundle.getString("ID");

        mQueue = Volley.newRequestQueue(this);

        if(ID == null) {
            IDText = (EditText) findViewById(R.id.LoginIDText);
            PassText = (EditText) findViewById(R.id.LoginPassText);

            FindPass = (Button) findViewById(R.id.FindPass);
            LoginSignup = (Button) findViewById(R.id.LoginSingup);
            BtnLogin = (Button) findViewById(R.id.BtnLogin);

            BtnLogin.setOnClickListener(loginEvent);
            FindPass.setOnClickListener(findPassEvent);
            LoginSignup.setOnClickListener(loginSignupEvent);
        }
        else {
            logout = (Button) findViewById(R.id.BtnLogin);
            logout.setText("로그아웃");
        }
    }

    Button.OnClickListener loginEvent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            id = IDText.getText().toString();
            pwd = PassText.getText().toString();

            if(id.length() == 0 || pwd.length() == 0){
                Toast.makeText(Login.this,"아이디 혹은 비밀번호를 입력하시지 않았습니다.", Toast.LENGTH_SHORT).show();
            } else {
                logIn();
            }
        }
    };

    Button.OnClickListener findPassEvent = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Toast.makeText(Login.this,"현재 기능이 구현되지 않았습니다.", Toast.LENGTH_SHORT).show();
        }
    };

    Button.OnClickListener loginSignupEvent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), Signup.class);
            startActivity(intent);
        }
    };

    protected void logIn(){
        setStreamToLogin();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                SERVER_LOGIN, login_data, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse (JSONObject response){
                mResult = response;
                try {
                    boolean isSuccess = mResult.getBoolean("authonization");
                    if(isSuccess){
                        ID = id;
                        Toast.makeText(Login.this,"오늘도 환영합니다."+id+"님!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        intent.putExtra("ID", ID);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Login.this,"존재하지 않거나 잘못된 ID혹은 PW입니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
                ,new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(Login.this,
                        "서버와 연결이 되지 않습니다. 로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        mQueue.add(request);
    }

    protected void setStreamToLogin(){
        try {
            login_data = new JSONObject();
            login_data.put("id",id);
            login_data.put("pwd",pwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

