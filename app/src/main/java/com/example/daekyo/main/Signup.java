package com.example.daekyo.main;

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

import java.util.ArrayList;

public class Signup extends AppCompatActivity {
    protected EditText IDText = null;
    protected EditText PassText = null;
    protected EditText ConfirmText = null;
    protected EditText NameText = null;
    protected Spinner sex = null;
    protected EditText BirthText = null;
    protected EditText PhoneText = null;
    protected TextView UpperLocal = null;
    protected TextView LowerLocal = null;
    protected EditText AddressText = null;
    protected Spinner JobText = null;

    protected ArrayList<String> sexList = new ArrayList<>();
    protected ArrayAdapter<String> sexSpinnerAdapter = null;

    protected ArrayList<String> jobList = new ArrayList<>();
    protected ArrayAdapter<String> jobSpinnerAdapter = null;

    protected String selectedSex = null;
    protected String selectedUpperLocal = null;
    protected String selectedLowerLocal = null;
    protected String selectedJob = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        IDText = (EditText) findViewById(R.id.IDText);
        PassText = (EditText) findViewById(R.id.PassText);
        ConfirmText = (EditText) findViewById(R.id.ConfirmText);
        NameText = (EditText) findViewById(R.id.NameText);
        sex = (Spinner) findViewById(R.id.sex);
        BirthText = (EditText) findViewById(R.id.BirthText);
        PhoneText = (EditText) findViewById(R.id.PhoneText);
        UpperLocal = (TextView) findViewById(R.id.UpperLocal);
        LowerLocal = (TextView) findViewById(R.id.LowerLocal);
        AddressText = (EditText) findViewById(R.id.AddressText);
        JobText = (Spinner) findViewById(R.id.JobText);

        sexList.add("남"); sexList.add("여");
        sexSpinnerAdapter = new ArrayAdapter<String>(Signup.this, android.R.layout.simple_spinner_item, sexList);
        sex.setAdapter(sexSpinnerAdapter);
        sex.setOnItemSelectedListener(sexSelecter);

        jobList.add("매장관리"); jobList.add("서빙"); jobList.add("주방");
        jobList.add("배달"); jobList.add("사무보조"); jobList.add("노무");
        jobSpinnerAdapter = new ArrayAdapter<String>(Signup.this, android.R.layout.simple_spinner_item, jobList);
        JobText.setAdapter(jobSpinnerAdapter);
        JobText.setOnItemSelectedListener(jobSelecter);
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
            String pass1 = PassText.getText().toString();
            String pass2 = ConfirmText.getText().toString();

            if(!pass1.equals(pass2))
                Toast.makeText(Signup.this,"비밀번호가 같지 않습니다.",Toast.LENGTH_SHORT).show();
            else if(IDText.getText().toString().length() == 0)
                Toast.makeText(Signup.this,"ID를 입력하지 않으셨습니다.",Toast.LENGTH_SHORT).show();
            else if(PassText.getText().toString().length() == 0)
                Toast.makeText(Signup.this,"패스워드를 입력하지 않으셨습니다.",Toast.LENGTH_SHORT).show();
            else if(ConfirmText.getText().toString().length() == 0)
                Toast.makeText(Signup.this,"확인용 패스워드를 입력하지 않으셨습니다.",Toast.LENGTH_SHORT).show();
            else if(NameText.getText().toString().length() == 0)
                Toast.makeText(Signup.this,"이름을 입력하지 않으셨습니다.",Toast.LENGTH_SHORT).show();
            else if(selectedSex.length() == 0)
                Toast.makeText(Signup.this,"성별을 선택하지 않으셨습니다.",Toast.LENGTH_SHORT).show();
            else if(BirthText.getText().toString().length() == 0)
                Toast.makeText(Signup.this,"생년월일을 입력하지 않으셨습니다.",Toast.LENGTH_SHORT).show();
            else if(PhoneText.getText().toString().length() == 0)
                Toast.makeText(Signup.this,"연락처를 입력하지 않으셨습니다.",Toast.LENGTH_SHORT).show();
            else if(selectedUpperLocal.length() == 0)
                Toast.makeText(Signup.this,"상위 주소를 선택하지 않으셨습니다.",Toast.LENGTH_SHORT).show();
            else if(selectedLowerLocal.length() == 0)
                Toast.makeText(Signup.this,"하위 주소를 선택하지 않으셨습니다.",Toast.LENGTH_SHORT).show();
            else if(AddressText.getText().toString().length() == 0)
                Toast.makeText(Signup.this,"상세 주소를 입력하지 않으셨습니다.",Toast.LENGTH_SHORT).show();
            else if(selectedJob.length() == 0)
                Toast.makeText(Signup.this,"주직종을 선택하지 않으셨습니다.",Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(Signup.this,"회원 가입이 완료되었습니다!",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        }
    };

}
