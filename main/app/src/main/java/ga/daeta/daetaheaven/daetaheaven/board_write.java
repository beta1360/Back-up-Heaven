package ga.daeta.daetaheaven.daetaheaven;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class board_write extends Activity {
    private String ID = null;

    ArrayAdapter<CharSequence> adspin;
    private static final int PICK_FROM_CAMERA=0;
    private static final int PICK_FROM_ALBUM=1;
    private static final int CROP_FROM_IMAGE=2;

    private Uri mImageCaptureUri;
    private ImageView storeImage;
    private int id_view;
    private String absoultePath;

    private TextView sidocheck;
    private TextView sigungucheck;
    private TextView yearcheck;
    private TextView monthcheck;
    private TextView daycheck;
    private TextView deyearcheck;
    private TextView demonthcheck;
    private TextView dedaycheck;
    private RadioGroup rg;
    private RadioButton genderxx;
    private RadioButton genderxy;
    private RadioButton genderxxy;
    private TextView tgender;
    Bitmap photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.wrtie_board);
        if (savedInstanceState != null)
            ID = savedInstanceState.getString("ID");

        yearcheck = (TextView)findViewById(R.id.yearcheck);
        monthcheck = (TextView)findViewById(R.id.monthcheck);
        daycheck = (TextView)findViewById(R.id.daycheck);
        deyearcheck = (TextView)findViewById(R.id.deyearcheck);
        demonthcheck = (TextView)findViewById(R.id.demonthcheck);
        dedaycheck = (TextView)findViewById(R.id.dedaycheck);
        genderxx=(RadioButton)findViewById(R.id.gender_xx);
        genderxy=(RadioButton)findViewById(R.id.gender_xy);
        genderxxy=(RadioButton)findViewById(R.id.gender_xxy);
        rg=(RadioGroup)findViewById(R.id.gender);
        tgender = (TextView)findViewById(R.id.tgender);

        genderxxy.setChecked(true);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.gender_xx){
                    tgender.setText("남자");
                }
                else if(checkedId==R.id.gender_xy){
                    tgender.setText("여자");
                }
                else{
                    tgender.setText("무관");
                }
            }
        });

        sidocheck = (TextView)findViewById(R.id.sidocheck);

        storeImage = (ImageView)this.findViewById(R.id.storeimage);
        Button picturebutton = (Button)this.findViewById(R.id.picturebutton);
        picturebutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onPicture(v);
            }
        });


        Spinner spinner_sido = (Spinner) findViewById(R.id.sido);
        spinner_sido.setPrompt("시/도 선택");

        adspin=ArrayAdapter.createFromResource(this, R.array.sido, android.R.layout.simple_spinner_item);

        adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sido.setAdapter(adspin);
        spinner_sido.setOnItemSelectedListener(new OnItemSelectedListener(){

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){

                if(parent.getItemAtPosition(position).equals("서울")){ sigungu(0); sidocheck.setText("서울");}
                else if(parent.getItemAtPosition(position).equals("경기도")){ sigungu(1);sidocheck.setText("경기도"); }
                else if(parent.getItemAtPosition(position).equals("인천")){ sigungu(2); sidocheck.setText("인천"); }
                else if(parent.getItemAtPosition(position).equals("강원도")){ sigungu(3);sidocheck.setText("강원도"); }
                else if(parent.getItemAtPosition(position).equals("충청남도")){ sigungu(4); sidocheck.setText("충청남도");}
                else if(parent.getItemAtPosition(position).equals("대전")){ sigungu(5);sidocheck.setText("대전"); }
                else if(parent.getItemAtPosition(position).equals("충청북도")){ sigungu(6);sidocheck.setText("충청북도"); }
                else if(parent.getItemAtPosition(position).equals("세종")){ sigungu(7); sidocheck.setText("세종");}
                else if(parent.getItemAtPosition(position).equals("부산")){ sigungu(8);sidocheck.setText("부산"); }
                else if(parent.getItemAtPosition(position).equals("울산")){ sigungu(9);sidocheck.setText("울산"); }
                else if(parent.getItemAtPosition(position).equals("대구")){ sigungu(10);sidocheck.setText("대구"); }
                else if(parent.getItemAtPosition(position).equals("경상북도")){ sigungu(11);sidocheck.setText("경상북도"); }
                else if(parent.getItemAtPosition(position).equals("경상남도")){ sigungu(12); sidocheck.setText("경상남도");}
                else if(parent.getItemAtPosition(position).equals("전라남도")){ sigungu(13);sidocheck.setText("전라남도"); }
                else if(parent.getItemAtPosition(position).equals("광주")){ sigungu(14); sidocheck.setText("광주");}
                else if(parent.getItemAtPosition(position).equals("전라북도")){ sigungu(15);sidocheck.setText("전라북도"); }
                else if(parent.getItemAtPosition(position).equals("제주도")){ sigungu(16);sidocheck.setText("제주도"); }

            }
            public void onNothingSelected(AdapterView<?> parent){ }
        });


        Spinner spinner_year = (Spinner) findViewById(R.id.year);
        spinner_year.setPrompt("년도를 선택하세요.");
        adspin=ArrayAdapter.createFromResource(this, R.array.year, android.R.layout.simple_spinner_item);
        adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_year.setAdapter(adspin);
        spinner_year.setOnItemSelectedListener(new OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){ yearcheck.setText(parent.getItemAtPosition(position)+"");
            Log.d("test",parent.getItemAtPosition(position)+" ");
            }
            public void onNothingSelected(AdapterView<?> parent){
            }
        });

        Spinner spinner_month = (Spinner) findViewById(R.id.month);
        spinner_month.setPrompt("달을 선택하세요.");

        adspin=ArrayAdapter.createFromResource(this, R.array.month, android.R.layout.simple_spinner_item);

        adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_month.setAdapter(adspin);
        spinner_month.setOnItemSelectedListener(new OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){monthcheck.setText(parent.getItemAtPosition(position)+""); }
            public void onNothingSelected(AdapterView<?> parent){
            }
        });

        Spinner spinner_day = (Spinner) findViewById(R.id.day);
        spinner_day.setPrompt("일을 선택하세요.");

        adspin=ArrayAdapter.createFromResource(this, R.array.day, android.R.layout.simple_spinner_item);

        adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_day.setAdapter(adspin);
        spinner_day.setOnItemSelectedListener(new OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){daycheck.setText(parent.getItemAtPosition(position)+""); }
            public void onNothingSelected(AdapterView<?> parent){
            }
        });

        Spinner spinner_deyear = (Spinner) findViewById(R.id.deyear);
        spinner_deyear.setPrompt("년도를 선택하세요.");

        adspin=ArrayAdapter.createFromResource(this, R.array.year, android.R.layout.simple_spinner_item);
        adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_deyear.setAdapter(adspin);
        spinner_deyear.setOnItemSelectedListener(new OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){deyearcheck.setText(parent.getItemAtPosition(position)+"");
           }
            public void onNothingSelected(AdapterView<?> parent){
            }
        });

        Spinner spinner_demonth = (Spinner) findViewById(R.id.demonth);
        spinner_demonth.setPrompt("달을 선택하세요.");
        adspin=ArrayAdapter.createFromResource(this, R.array.month, android.R.layout.simple_spinner_item);
        adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_demonth.setAdapter(adspin);
        spinner_demonth.setOnItemSelectedListener(new OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){demonthcheck.setText(parent.getItemAtPosition(position)+"");
                }
            public void onNothingSelected(AdapterView<?> parent){
            }
        });

        Spinner spinner_deday = (Spinner) findViewById(R.id.deday);
        spinner_deday.setPrompt("일을 선택하세요.");
        adspin=ArrayAdapter.createFromResource(this, R.array.day, android.R.layout.simple_spinner_item);
        adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_deday.setAdapter(adspin);
        spinner_deday.setOnItemSelectedListener(new OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){dedaycheck.setText(parent.getItemAtPosition(position)+"");
            }
            public void onNothingSelected(AdapterView<?> parent){
            }
        });
    }
    public void sigungu(int position){
        Spinner spinner_sigungu = (Spinner) findViewById(R.id.sigungu);
        spinner_sigungu.setPrompt("시/군/구를 선택하세요.");
        sigungucheck = (TextView)findViewById(R.id.sigungucheck);

        if(position==0){    //서울
            adspin=ArrayAdapter.createFromResource(this, R.array.sido_seoul, android.R.layout.simple_spinner_item);
            adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_sigungu.setAdapter(adspin);
            spinner_sigungu.setOnItemSelectedListener(new OnItemSelectedListener(){
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id){sigungucheck.setText(parent.getItemAtPosition(position)+""); }
                public void onNothingSelected(AdapterView<?> parent){ }});
        }
        else if(position==1){   //경기도
            adspin=ArrayAdapter.createFromResource(this, R.array.sido_gyeonggido, android.R.layout.simple_spinner_item);
            adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_sigungu.setAdapter(adspin);
            spinner_sigungu.setOnItemSelectedListener(new OnItemSelectedListener(){
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id){ sigungucheck.setText(parent.getItemAtPosition(position)+"");}
                public void onNothingSelected(AdapterView<?> parent){ }});
        }
        else if(position==2){   //인천
            adspin=ArrayAdapter.createFromResource(this, R.array.sido_incheon, android.R.layout.simple_spinner_item);
            adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_sigungu.setAdapter(adspin);
            spinner_sigungu.setOnItemSelectedListener(new OnItemSelectedListener(){
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id){ sigungucheck.setText(parent.getItemAtPosition(position)+"");}
                public void onNothingSelected(AdapterView<?> parent){ }});
        }
        else if(position==3){   //강원도
            adspin=ArrayAdapter.createFromResource(this, R.array.sido_gangwondo, android.R.layout.simple_spinner_item);
            adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_sigungu.setAdapter(adspin);
            spinner_sigungu.setOnItemSelectedListener(new OnItemSelectedListener(){
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id){ sigungucheck.setText(parent.getItemAtPosition(position)+"");}
                public void onNothingSelected(AdapterView<?> parent){ }});
        }
        else if(position==4){   //충남
            adspin=ArrayAdapter.createFromResource(this, R.array.sido_chungnam, android.R.layout.simple_spinner_item);
            adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_sigungu.setAdapter(adspin);
            spinner_sigungu.setOnItemSelectedListener(new OnItemSelectedListener(){
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id){ sigungucheck.setText(parent.getItemAtPosition(position)+"");}
                public void onNothingSelected(AdapterView<?> parent){ }});
        }
        else if(position==5){   //대전
            adspin=ArrayAdapter.createFromResource(this, R.array.sido_daejeon, android.R.layout.simple_spinner_item);
            adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_sigungu.setAdapter(adspin);
            spinner_sigungu.setOnItemSelectedListener(new OnItemSelectedListener(){
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id){sigungucheck.setText(parent.getItemAtPosition(position)+""); }
                public void onNothingSelected(AdapterView<?> parent){ }});
        }
        else if(position==6){   //충북
            adspin=ArrayAdapter.createFromResource(this, R.array.sido_chungbuk, android.R.layout.simple_spinner_item);
            adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_sigungu.setAdapter(adspin);
            spinner_sigungu.setOnItemSelectedListener(new OnItemSelectedListener(){
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id){sigungucheck.setText(parent.getItemAtPosition(position)+""); }
                public void onNothingSelected(AdapterView<?> parent){ }});
        }
        else if(position==7){   //세종
            adspin=ArrayAdapter.createFromResource(this, R.array.sido_seajong, android.R.layout.simple_spinner_item);
            adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_sigungu.setAdapter(adspin);
            spinner_sigungu.setOnItemSelectedListener(new OnItemSelectedListener(){
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id){ sigungucheck.setText(parent.getItemAtPosition(position)+"");}
                public void onNothingSelected(AdapterView<?> parent){ }});
        }
        else if(position==8){   //부산
            adspin=ArrayAdapter.createFromResource(this, R.array.sido_busan, android.R.layout.simple_spinner_item);
            adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_sigungu.setAdapter(adspin);
            spinner_sigungu.setOnItemSelectedListener(new OnItemSelectedListener(){
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id){ sigungucheck.setText(parent.getItemAtPosition(position)+"");}
                public void onNothingSelected(AdapterView<?> parent){ }});
        }
        else if(position==9){   //울산
            adspin=ArrayAdapter.createFromResource(this, R.array.sido_ulsan, android.R.layout.simple_spinner_item);
            adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_sigungu.setAdapter(adspin);
            spinner_sigungu.setOnItemSelectedListener(new OnItemSelectedListener(){
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id){sigungucheck.setText(parent.getItemAtPosition(position)+""); }
                public void onNothingSelected(AdapterView<?> parent){ }});
        }
        else if(position==10){  //대구
            adspin=ArrayAdapter.createFromResource(this, R.array.sido_daegu, android.R.layout.simple_spinner_item);
            adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_sigungu.setAdapter(adspin);
            spinner_sigungu.setOnItemSelectedListener(new OnItemSelectedListener(){
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id){ sigungucheck.setText(parent.getItemAtPosition(position)+"");}
                public void onNothingSelected(AdapterView<?> parent){ }});
        }
        else if(position==11){  //경북
            adspin=ArrayAdapter.createFromResource(this, R.array.sido_gyeongbuk, android.R.layout.simple_spinner_item);
            adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_sigungu.setAdapter(adspin);
            spinner_sigungu.setOnItemSelectedListener(new OnItemSelectedListener(){
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id){sigungucheck.setText(parent.getItemAtPosition(position)+""); }
                public void onNothingSelected(AdapterView<?> parent){ }});
        }
        else if(position==12){  //경남
            adspin=ArrayAdapter.createFromResource(this, R.array.sido_gyeongnam, android.R.layout.simple_spinner_item);
            adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_sigungu.setAdapter(adspin);
            spinner_sigungu.setOnItemSelectedListener(new OnItemSelectedListener(){
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id){ sigungucheck.setText(parent.getItemAtPosition(position)+"");}
                public void onNothingSelected(AdapterView<?> parent){ }});
        }
        else if(position==13){  //전남
            adspin=ArrayAdapter.createFromResource(this, R.array.sido_jeonnam, android.R.layout.simple_spinner_item);
            adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_sigungu.setAdapter(adspin);
            spinner_sigungu.setOnItemSelectedListener(new OnItemSelectedListener(){
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id){ sigungucheck.setText(parent.getItemAtPosition(position)+"");}
                public void onNothingSelected(AdapterView<?> parent){ }});
        }
        else if(position==14){  //광주
            adspin=ArrayAdapter.createFromResource(this, R.array.sido_gwangju, android.R.layout.simple_spinner_item);
            adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_sigungu.setAdapter(adspin);
            spinner_sigungu.setOnItemSelectedListener(new OnItemSelectedListener(){
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id){sigungucheck.setText(parent.getItemAtPosition(position)+""); }
                public void onNothingSelected(AdapterView<?> parent){ }});
        }
        else if(position==15){  //전북
            adspin=ArrayAdapter.createFromResource(this, R.array.sido_jeonbuk, android.R.layout.simple_spinner_item);
            adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_sigungu.setAdapter(adspin);
            spinner_sigungu.setOnItemSelectedListener(new OnItemSelectedListener(){
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id){sigungucheck.setText(parent.getItemAtPosition(position)+""); }
                public void onNothingSelected(AdapterView<?> parent){ }});
        }
        else if(position==16){  //제주
            adspin=ArrayAdapter.createFromResource(this, R.array.sido_jeju, android.R.layout.simple_spinner_item);
            adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_sigungu.setAdapter(adspin);
            spinner_sigungu.setOnItemSelectedListener(new OnItemSelectedListener(){
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id){ sigungucheck.setText(parent.getItemAtPosition(position)+"");}
                public void onNothingSelected(AdapterView<?> parent){ }});
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) {

            switch (requestCode) {
                case PICK_FROM_ALBUM: {
                    mImageCaptureUri = data.getData();
                    Log.d("Smartwheel", mImageCaptureUri.getPath().toString());
                }
                case PICK_FROM_CAMERA: {
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(mImageCaptureUri, "image/*");
                    intent.putExtra("outputX", 150);
                    intent.putExtra("outputY", 150);
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                    intent.putExtra("scale", true);
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, CROP_FROM_IMAGE);
                    break;
                }

                case CROP_FROM_IMAGE: {
                    if (resultCode == RESULT_OK) {

                        final Bundle extras = data.getExtras();

                        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                                "/SmartWheel/" + System.currentTimeMillis() + ".jpg";

                        if (extras != null) {
                             photo = data.getExtras().getParcelable("data");
                            storeImage.setImageBitmap(photo);


                            storeCropImage(photo, filePath);
                            absoultePath = filePath;
                            break;
                        }
                        //임시 파일 삭제
                        File f = new File(mImageCaptureUri.getPath());
                        if (f.exists()) {
                            f.delete();
                        }
                    }
                }
            }
        }
    }

    public void onWrite(View v){    //작성하기 버튼 눌렀을 때

        EditText title = (EditText)findViewById(R.id.title);
        EditText positiondetail = (EditText)findViewById(R.id.positiondetail);
        EditText storename = (EditText)findViewById(R.id.storename);
        EditText telephone = (EditText)findViewById(R.id.telephone);
        EditText money = (EditText)findViewById(R.id.money);
        EditText condition = (EditText)findViewById(R.id.condition);
        EditText job = (EditText)findViewById(R.id.job);
        EditText addinformation = (EditText)findViewById(R.id.addinformation);
        EditText plus = (EditText)findViewById(R.id.plus);

        if(title.getText().toString().equals(null)||title.getText().toString().equals("")) {
            Toast.makeText(board_write.this, "게시글 제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(positiondetail.getText().toString().equals(null)||positiondetail.getText().toString().equals("")) {
            Toast.makeText(board_write.this, "지점 상세주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(storename.getText().toString().equals(null)||storename.getText().toString().equals("")) {
            Toast.makeText(board_write.this, "지점명을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(positiondetail.getText().toString().equals(null)||positiondetail.getText().toString().equals("")) {
            Toast.makeText(board_write.this, "지점 상세주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(telephone.getText().toString().equals(null)||telephone.getText().toString().equals("")) {
            Toast.makeText(board_write.this, "연락처를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String str = telephone.getText().toString().trim();
        } catch(NumberFormatException e){
            Toast.makeText(this, "숫자만 입력하세요", Toast.LENGTH_SHORT).show();
        }

        if(money.getText().toString().equals(null)||money.getText().toString().equals("")) {
            Toast.makeText(board_write.this, "급여정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(job.getText().toString().equals(null)||job.getText().toString().equals("")) {
            Toast.makeText(board_write.this, "모집 직종을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent writeresult = new Intent(board_write.this, board_write_result.class);
        writeresult.putExtra("title", title.getText().toString());
        writeresult.putExtra("sidocheck", sidocheck.getText().toString());
        writeresult.putExtra("sigungucheck", sigungucheck.getText().toString());
        writeresult.putExtra("positiondetail", positiondetail.getText().toString());
        writeresult.putExtra("storename", storename.getText().toString());
        writeresult.putExtra("telephone", telephone.getText().toString());
        writeresult.putExtra("yearcheck", yearcheck.getText().toString());
        writeresult.putExtra("monthcheck", monthcheck.getText().toString());
        writeresult.putExtra("daycheck", daycheck.getText().toString());
        writeresult.putExtra("deyearcheck", deyearcheck.getText().toString());
        writeresult.putExtra("demonthcheck", demonthcheck.getText().toString());
        writeresult.putExtra("dedaycheck", dedaycheck.getText().toString());
        writeresult.putExtra("money", money.getText().toString());
        writeresult.putExtra("gender", tgender.getText().toString());
        writeresult.putExtra("condition", condition.getText().toString());
        writeresult.putExtra("job", job.getText().toString());
        writeresult.putExtra("addinformation", addinformation.getText().toString());
        writeresult.putExtra("plus", plus.getText().toString());
        writeresult.putExtra("photo", (Bitmap)photo);


       startActivity(writeresult);
  //      finish();
    }

    public void onCancel(View v){
        Intent writecancel = new Intent(this, MainActivity.class);
        startActivity(writecancel);
        finish();
    }

                public void onPicture(View v){

        DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(board_write.this, "카메라 실행", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String url = "tmp_"+String.valueOf(System.currentTimeMillis())+".jpg";
                mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),url));
                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);
            }
        };
        DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        };
        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };

        new AlertDialog.Builder(this)
                .setTitle("등록할 이미지 선택")
                .setPositiveButton("사진촬영", cameraListener)
                .setNeutralButton("앨범선택",albumListener)
                .setNegativeButton("취소", cancelListener)
                .show();

    }

    private void storeCropImage(Bitmap bitmap, String filePath){
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/SmartWheel";
        File directory_SmartWheel = new File(dirPath);

        if(!directory_SmartWheel.exists())
            directory_SmartWheel.mkdir();

        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try{
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.fromFile(copyFile)));

            out.flush();
            out.close();


        }catch(Exception e){
            e.printStackTrace();
        }
    }
}