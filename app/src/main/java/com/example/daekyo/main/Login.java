package com.example.daekyo.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    protected EditText IDText = null;
    protected EditText PassText = null;
    protected Button BtnLogin= null;
    protected Button FindPass = null;
    protected Button LoginSignup = null;
    protected String id;
    protected String pwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        IDText = (EditText) findViewById(R.id.LoginIDText);
        PassText = (EditText) findViewById(R.id.LoginPassText);

        BtnLogin = (Button) findViewById(R.id.BtnLogin);
        FindPass = (Button) findViewById(R.id.FindPass);
        LoginSignup = (Button) findViewById(R.id.LoginSingup);

        BtnLogin.setOnClickListener(loginEvent);
        FindPass.setOnClickListener(findPassEvent);
        LoginSignup.setOnClickListener(loginSignupEvent);
    }

    Button.OnClickListener loginEvent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            id = IDText.getText().toString();
            pwd = PassText.getText().toString();

            if(id.length() == 0 || pwd.length() == 0){
                Toast.makeText(Login.this,"아이디 혹은 비밀번호를 입력하시지 않았습니다.", Toast.LENGTH_SHORT).show();
            } else {
                /*
                 Server part
                 */
                Toast.makeText(Login.this, "환영합니다." + id + "님!", Toast.LENGTH_SHORT).show();
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
}
