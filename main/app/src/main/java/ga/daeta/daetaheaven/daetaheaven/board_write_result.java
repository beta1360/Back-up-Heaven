package ga.daeta.daetaheaven.daetaheaven;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class board_write_result extends Activity {
    private String ID = null;

    private TextView ttitle;
    private TextView tposition;
    private TextView tpositiondetail;
    private TextView tstorename;
    private TextView ttelephone;
    private TextView ttime;
    private TextView tmoney;
    private TextView tgender;
    private TextView tcondition;
    private TextView tjob;
    private TextView tplus;
    private TextView taddinformation;
    private TextView tcond;
    private TextView tjobb;
    private TextView taddinformationn;
    private TextView tpluss;
    private ImageView storeimage;

    private static String TAG = "phptest_MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_result);
        if (savedInstanceState != null)
            ID = savedInstanceState.getString("ID");

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String sido = intent.getStringExtra("sidocheck");
        String sigungu = intent.getStringExtra("sigungucheck");
        String positiondetail = intent.getStringExtra("positiondetail");
        String storename = intent.getStringExtra("storename");
        final String telephone = intent.getStringExtra("telephone");
        ImageView storeimage =(ImageView)findViewById(R.id.storeimage);
        String year = intent.getStringExtra("yearcheck");
        String month = intent.getStringExtra("monthcheck");
        String day = intent.getStringExtra("daycheck");
        String deyear = intent.getStringExtra("deyearcheck");
        String demonth = intent.getStringExtra("demonthcheck");
        String deday = intent.getStringExtra("dedaycheck");
        String money = intent.getStringExtra("money");
        String gender = intent.getStringExtra("gender");
        String condition = intent.getStringExtra("condition");
        String job = intent.getStringExtra("job");
        String addinformation = intent.getStringExtra("addinformation");
        final String plus = intent.getStringExtra("plus");
        Bitmap bphoto = (Bitmap)intent.getExtras().get("photo");

        ttitle = (TextView)findViewById(R.id.title);
        ttitle.setText(title);
        tposition = (TextView)findViewById(R.id.position);
        tposition.setText(sido+"   "+sigungu);
        tpositiondetail = (TextView)findViewById(R.id.positiondetail);
        tpositiondetail.setText(positiondetail);
        tstorename = (TextView)findViewById(R.id.storename);
        tstorename.setText(storename);
        storeimage = (ImageView)findViewById(R.id.storeimage);
        storeimage.setImageBitmap(bphoto);
        ttelephone = (TextView)findViewById(R.id.telephone);
        ttelephone.setText(telephone);
        ttime= (TextView)findViewById(R.id.time);
        ttime.setText(year+"년 "+month+"월 "+day+"일  ~  "+deyear+"년 "+demonth+"월 "+deday+"일");
        tmoney = (TextView)findViewById(R.id.money);
        tmoney.setText(money);
        tgender = (TextView)findViewById(R.id.gender);
        tgender.setText(gender);
        tcond = (TextView)findViewById(R.id.condition);
        tcond.setText(condition);
        tjobb = (TextView)findViewById(R.id.job);
        tjobb.setText(job);
        taddinformationn = (TextView)findViewById(R.id.addinformation);
        taddinformationn.setText(addinformation);
        tplus = (TextView)findViewById(R.id.plus);
        tplus.setText(plus);

        Button buttonInsert = (Button)findViewById(R.id.okButton);
        buttonInsert.setOnClickListener(new View.OnClickListener() { //작성버튼시
            @Override
            public void onClick(View v) {

            String ftitle=ttitle.getText().toString();         //게시글
            String fposition=tposition.getText().toString();     //지점 위치
            String fpositiondetail=tpositiondetail.getText().toString(); //상세 위치
            String fstorename=tstorename.getText().toString(); // 지점 명
            //String fstoreimage=storeimage.get
            String ftelephone=ttelephone.getText().toString(); // 연락처
            String ftime=ttime.getText().toString();          // 대타 기간
            String fmoney=tmoney.getText().toString();        //시급
            String fgender=tgender.getText().toString();    //성별
            String fcond=tcond.getText().toString();    //경력
             String fjobb=tjobb.getText().toString();     //직종
             String faddinformationn=taddinformationn.getText().toString();
             String fplus=tplus.getText().toString();


                InsertData task = new InsertData();
                task.execute(ftitle,fposition,fpositiondetail,fstorename, ftelephone,ftime,fmoney,fgender,fcond,fjobb,faddinformationn,fplus);


                ttitle.setText("");
                tposition.setText("");
                tpositiondetail.setText("");
                tstorename.setText("");
                ttelephone.setText("");
                ttime.setText("");
               tmoney.setText("");
                tgender.setText("");
                tcond.setText("");
                 tjobb.setText("");
                taddinformationn.setText("");
                tplus.setText("");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }


    class InsertData extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(board_write_result.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
           // mTextViewResult.setText(result);
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String title = (String)params[0];
            String position = (String)params[1];
            String positiondetail=(String)params[2];
            String storename=(String)params[3];
            String telephone=(String)params[4];
            String time=(String)params[5];
            String money=(String)params[6];
            String gender=(String)params[7];
            String cond=(String)params[8];
            String job=(String)params[9];
           String addinformation=(String)params[10];
           String plus=(String)params[11];

            String serverURL = "http://172.30.1.54/insert_board.php";
            String postParameters = "title=" + title + "&position=" + position
                    +"&positiondetail="+positiondetail+"&storename="+storename
                    +"&telephone="+telephone+"&time="+time
                    +"&money="+ money +"&gender="+gender
                    +"&cond="+ cond
                    +"&job="+job
                   +"&addinformation="+addinformation
                    +"&plus="+plus;

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString();

            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }

        public void onEdit(View v){

      //  Intent intenttip = new Intent(this, board_write.class);
      //  startActivity(intenttip);
        finish();
    }



}
