package ga.daeta.daetaheaven.daetaheaven;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BoardWriter extends Activity {
    private String ID = null;
    private final int CAMERA_CODE = 1111;
    private final int GALLERY_CODE = 1112;
    private int board_num;

    protected RequestQueue mQueue = null;
    protected JSONObject mResult = null;
    protected final String SERVER_LOCAL_FILTER = "http://daeta.ga/locallist?filter=";
    protected final String SERVER_JOB_FILTER = "http://daeta.ga/joblist";
    protected final String SERVER_REGIST_BOARD = "http://daeta.ga/board_regist";
    protected final String SERVER_IMAGE_UPLOAD = "http://daeta.ga/upload?no=";
    protected final String CAPTURE_IMAGE = "사진 촬영";
    protected final String GET_GALLARY = "갤러리에서 가져오기";
    protected String upper_local_text = null;
    protected String upper_local = "대한민국";
    protected String lower_local = null;
    protected ArrayList<String> upperLocalList = new ArrayList<String>();
    protected ArrayList<String> lowerLocalList = new ArrayList<String>();

    private EditText board_title = null;
    private EditText store_name = null;
    private Button local_selector = null;
    private EditText local_sub = null;
    private EditText phone = null;

    private Spinner start_year = null;
    private Spinner start_mon = null;
    private Spinner start_day = null;
    private Spinner start_hour = null;
    private Spinner start_min = null;

    private Spinner end_year = null;
    private Spinner end_mon = null;
    private Spinner end_day = null;
    private Spinner end_hour = null;
    private Spinner end_min = null;

    private EditText urgency = null;
    private Spinner gender = null;

    private EditText condition = null;
    private Button job = null;
    private EditText favorable_condition = null;
    private EditText detail = null;
    private Button image_selector = null;

    private Button ok_button = null;
    private Button cancel_button = null;
    private ImageView selected_image = null;
    private TextView image_path = null;

    protected String st_year = null;
    protected String st_month = null;
    protected String st_day = null;
    protected String st_hour = null;
    protected String st_min = null;

    protected String ed_year = null;
    protected String ed_month = null;
    protected String ed_day = null;
    protected String ed_hour = null;
    protected String ed_min = null;

    protected String sex = null;

    protected ArrayList<String> st_yearList = new ArrayList<>();
    protected ArrayAdapter<String> st_yearSpinnerAdapter = null;
    protected ArrayList<String> st_monthList = new ArrayList<>();
    protected ArrayAdapter<String> st_monthSpinnerAdapter = null;
    protected ArrayList<String> st_dayList = new ArrayList<>();
    protected ArrayAdapter<String> st_daySpinnerAdapter = null;
    protected ArrayList<String> st_hourList = new ArrayList<>();
    protected ArrayAdapter<String> st_hourSpinnerAdapter = null;
    protected ArrayList<String> st_minList = new ArrayList<>();
    protected ArrayAdapter<String> st_minSpinnerAdapter = null;

    protected ArrayList<String> ed_yearList = new ArrayList<>();
    protected ArrayAdapter<String> ed_yearSpinnerAdapter = null;
    protected ArrayList<String> ed_monthList = new ArrayList<>();
    protected ArrayAdapter<String> ed_monthSpinnerAdapter = null;
    protected ArrayList<String> ed_dayList = new ArrayList<>();
    protected ArrayAdapter<String> ed_daySpinnerAdapter = null;
    protected ArrayList<String> ed_hourList = new ArrayList<>();
    protected ArrayAdapter<String> ed_hourSpinnerAdapter = null;
    protected ArrayList<String> ed_minList = new ArrayList<>();
    protected ArrayAdapter<String> ed_minSpinnerAdapter = null;

    protected ArrayList<String> genderList = new ArrayList<>();
    protected ArrayAdapter<String> genderSpinnerAdapter = null;

    ArrayList<String> jobList = new ArrayList<String>();
    protected String selected_job = null;

    private JSONObject request_board_register = null;

    private Uri photoUri;
    private String currentPhotoPath;//실제 사진 파일 경로
    String mImageCaptureName;//이미지 이름

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            ID = bundle.getString("ID");

        setContentView(R.layout.write_board);
        mQueue = Volley.newRequestQueue(this);

        this.initializeView();
        this.intializeSpinner();
        this.setEventToComponents();
    }

    Spinner.OnItemSelectedListener selected_start_year = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            st_year = start_year.getItemAtPosition(position).toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {  }
    };
    Spinner.OnItemSelectedListener selected_start_mon = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            st_month = start_mon.getItemAtPosition(position).toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {  }
    };
    Spinner.OnItemSelectedListener selected_start_day = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            st_day = start_day.getItemAtPosition(position).toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {  }
    };
    Spinner.OnItemSelectedListener selected_start_hour = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            st_hour = start_hour.getItemAtPosition(position).toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {  }
    };
    Spinner.OnItemSelectedListener selected_start_min = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            st_min = start_min.getItemAtPosition(position).toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {  }
    };

    Spinner.OnItemSelectedListener selected_end_year = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ed_year = end_year.getItemAtPosition(position).toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {  }
    };
    Spinner.OnItemSelectedListener selected_end_mon = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ed_month = end_mon.getItemAtPosition(position).toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {  }
    };
    Spinner.OnItemSelectedListener selected_end_day = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ed_day = end_day.getItemAtPosition(position).toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {  }
    };
    Spinner.OnItemSelectedListener selected_end_hour = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ed_hour = end_hour.getItemAtPosition(position).toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {  }
    };
    Spinner.OnItemSelectedListener selected_end_min = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ed_min = end_min.getItemAtPosition(position).toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {  }
    };

    Spinner.OnItemSelectedListener selected_gender = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            sex = gender.getItemAtPosition(position).toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {  }
    };

    Button.OnClickListener ok_event = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(BoardWriter.this);
            alert_confirm.setMessage("게시물을 올리시겠습니까?")
                    .setCancelable(false).setPositiveButton("예",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                sendBoardInfo();
                                sendImage();
                                Toast.makeText(BoardWriter.this,"게시물 작성에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                Intent go_main = new Intent(BoardWriter.this, MainActivity.class);
                                go_main.putExtra("ID", ID);
                                startActivity(go_main);
                            } catch(Exception e) {
                                Toast.makeText(BoardWriter.this,"게시물 작성에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }).setNegativeButton("아니오",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
            AlertDialog alert = alert_confirm.create();
            alert.show();
        }
    };

    Button.OnClickListener cancel_event = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(BoardWriter.this);
            alert_confirm.setMessage("작성을 취소하시겠습니까?")
                    .setCancelable(false).setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent go_main = new Intent(BoardWriter.this, MainActivity.class);
                            go_main.putExtra("id", ID);
                            startActivity(go_main);
                        }
                    }).setNegativeButton("취소",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
            AlertDialog alert = alert_confirm.create();
            alert.show();
        }
    };

    Button.OnClickListener findLocal = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getUpperLocalFilter();
        }
    };

    Button.OnClickListener jobFilter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getJobFilter();
        }
    };

    Button.OnClickListener selectImage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showImageDialog();
        }
    };

    void showImageDialog() {
        final List<String> ListItems = new ArrayList<>();
        ListItems.add(CAPTURE_IMAGE);
        ListItems.add(GET_GALLARY);
        final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("사진 가져오기");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                String selectedText = items[pos].toString();
                if(selectedText.equals(CAPTURE_IMAGE)){
                    getImageByCapture();
                } else if (selectedText.equals(GET_GALLARY)){
                    getImageInGallery();
                } else {
                    Toast.makeText(BoardWriter.this,"Dialog Error",Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();
    }

    private void getImageByCapture(){
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {

                }
                if (photoFile != null) {
                    photoUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(intent, CAMERA_CODE);
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        File dir = new File(Environment.getExternalStorageDirectory() + "/path/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mImageCaptureName = timeStamp + ".png";

        File storageDir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/path/"
                + mImageCaptureName);
        currentPhotoPath = storageDir.getAbsolutePath();

        return storageDir;
    }

    private void getPictureForPhoto() {
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(currentPhotoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation;
        int exifDegree;

        if (exif != null) {
            exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            exifDegree = exifOrientationToDegrees(exifOrientation);
        } else {
            exifDegree = 0;
        }
        selected_image.setImageBitmap(rotate(bitmap, exifDegree));
        image_path.setText(currentPhotoPath);
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap src, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
    }

    private void getImageInGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GALLERY_CODE:
                    sendPicture(data.getData()); //갤러리에서 가져오기
                    break;
                case CAMERA_CODE:
                    getPictureForPhoto(); //카메라에서 가져오기
                    break;
                default:
                    break;
            }

        }
    }

    private void sendPicture(Uri imgUri) {
        String imagePath = getRealPathFromURI(imgUri); // path 경로
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int exifDegree = exifOrientationToDegrees(exifOrientation);

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        selected_image.setImageBitmap(rotate(bitmap, exifDegree));
        image_path.setText(currentPhotoPath);
    }

    private String getRealPathFromURI(Uri contentUri) {
        int column_index=0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }

        return cursor.getString(column_index);
    }

    protected void sendBoardInfo()  {
        sendRegistBoardStream();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                SERVER_REGIST_BOARD, request_board_register, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse (JSONObject response){
                try {
                    board_num = response.getInt("no");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return;
            }
        }
                ,new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(BoardWriter.this,
                        "서버와 연결이 되지 않습니다. 지역 정보 받아오기에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(request);
    }

    protected void sendImage(){
        String url = SERVER_IMAGE_UPLOAD + Integer.toString(board_num);
        // 파일을 서버로 보내는 부분
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        File glee = new File(currentPhotoPath);
        FileBody bin = new FileBody(glee);

        MultipartEntityBuilder meb = MultipartEntityBuilder.create();
        meb.setCharset(Charset.forName("UTF-8"));
        meb.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        meb.addPart("database", bin);
        HttpEntity entity = meb.build();

        post.setEntity(entity);

        try {
            HttpResponse reponse = client.execute(post);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // post 형식의 데이터를 서버로 전달
    }

    protected void sendRegistBoardStream() {
        StringBuffer start = new StringBuffer();
        StringBuffer end = new StringBuffer();

        start.append(st_year).append(".").append(st_month).append(".").append(st_day).append(" ")
                .append(st_hour).append(":").append(st_min);
        end.append(ed_year).append(".").append(ed_month).append(".").append(ed_day).append(" ")
                .append(ed_hour).append(":").append(ed_min);

        try {
            request_board_register = new JSONObject();
            request_board_register.put("id",ID);
            request_board_register.put("title", board_title.getText().toString());
            request_board_register.put("storename", store_name.getText().toString());
            request_board_register.put("start_time", start.toString());
            request_board_register.put("end_time", end.toString());
            request_board_register.put("urgency", urgency.getText().toString());
            request_board_register.put("job_condition", condition.getText().toString());
            request_board_register.put("job", selected_job);
            request_board_register.put("favorable_condition", favorable_condition.getText().toString());
            request_board_register.put("detail", detail.getText().toString());
            request_board_register.put("local", lower_local);
            request_board_register.put("local_sub", local_sub.getText().toString());
            request_board_register.put("phone", phone.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void getUpperLocalFilter()  {
        String url = SERVER_LOCAL_FILTER;
        try {
            String temp = URLEncoder.encode("대한민국", "utf-8");
            url = SERVER_LOCAL_FILTER + temp;
        } catch (UnsupportedEncodingException e){
            Toast.makeText(BoardWriter.this,e.toString(),Toast.LENGTH_SHORT).show();
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
                Toast.makeText(BoardWriter.this,
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
            Toast.makeText(BoardWriter.this,e.toString(),Toast.LENGTH_SHORT).show();
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
                Toast.makeText(BoardWriter.this,
                        "서버와 연결이 되지 않습니다. 지역 정보 받아오기에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        mQueue.add(request);
    }

    protected void getUpperLocalDialog(){
        final CharSequence[] items =  upperLocalList.toArray(new String[upperLocalList.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(BoardWriter.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(BoardWriter.this);
        builder.setTitle("지역 선택");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                lower_local = items[pos].toString();
                upper_local_text += " "+ lower_local;
                local_selector.setText(upper_local_text);
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
                Toast.makeText(BoardWriter.this,
                        "서버와 연결이 되지 않습니다. 지역 정보 받아오기에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        mQueue.add(request);
    }

    protected void getJobList() throws JSONException {
        jobList.clear();
        JSONArray upperLocal = mResult.getJSONArray("job_list");
        for(int i=0; i<upperLocal.length(); i++){
            String node = upperLocal.get(i).toString();
            jobList.add(node);
        }
    }

    protected void getJobDialog(){
        final CharSequence[] items = jobList.toArray(new String[jobList.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(BoardWriter.this);
        builder.setTitle("직종 선택");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                selected_job = items[pos].toString();
            }
        });
        builder.show();
    }

    protected void getLowerLocalList() throws JSONException {
        lowerLocalList.clear();
        JSONArray upperLocal = mResult.getJSONArray("local_list");
        for(int i=0; i<upperLocal.length(); i++){
            String node = upperLocal.get(i).toString();
            lowerLocalList.add(node);
        }
    }

    private void intializeSpinner(){
        for(int i=2018; i<=2030; i++){
            st_yearList.add(Integer.toString(i));
            ed_yearList.add(Integer.toString(i));
        }

        st_yearSpinnerAdapter = new ArrayAdapter<String>(BoardWriter.this, android.R.layout.simple_spinner_item, st_yearList);
        ed_yearSpinnerAdapter = new ArrayAdapter<String>(BoardWriter.this, android.R.layout.simple_spinner_item, ed_yearList);
        start_year.setAdapter(st_yearSpinnerAdapter);
        end_year.setAdapter(ed_yearSpinnerAdapter);

        for(int i=1; i<=12; i++){
            st_monthList.add(Integer.toString(i));
            ed_monthList.add(Integer.toString(i));
        }

        st_monthSpinnerAdapter = new ArrayAdapter<String>(BoardWriter.this, android.R.layout.simple_spinner_item, st_monthList);
        ed_monthSpinnerAdapter = new ArrayAdapter<String>(BoardWriter.this, android.R.layout.simple_spinner_item, ed_monthList);
        start_mon.setAdapter(st_monthSpinnerAdapter);
        end_mon.setAdapter(ed_monthSpinnerAdapter);

        for(int i=1; i<=31; i++){
            st_dayList.add(Integer.toString(i));
            ed_dayList.add(Integer.toString(i));
        }

        st_daySpinnerAdapter = new ArrayAdapter<String>(BoardWriter.this, android.R.layout.simple_spinner_item, st_dayList);
        ed_daySpinnerAdapter = new ArrayAdapter<String>(BoardWriter.this, android.R.layout.simple_spinner_item, ed_dayList);
        start_day.setAdapter(st_daySpinnerAdapter);
        end_day.setAdapter(ed_daySpinnerAdapter);

        for(int i=0; i<=23; i++){
            st_hourList.add(Integer.toString(i));
            ed_hourList.add(Integer.toString(i));
        }

        st_hourSpinnerAdapter = new ArrayAdapter<String>(BoardWriter.this, android.R.layout.simple_spinner_item, st_hourList);
        ed_hourSpinnerAdapter = new ArrayAdapter<String>(BoardWriter.this, android.R.layout.simple_spinner_item, ed_hourList);
        start_hour.setAdapter(st_hourSpinnerAdapter);
        end_hour.setAdapter(ed_hourSpinnerAdapter);

        for(int i=0; i<=59; i++){
            st_minList.add(Integer.toString(i));
            ed_minList.add(Integer.toString(i));
        }

        st_minSpinnerAdapter = new ArrayAdapter<String>(BoardWriter.this, android.R.layout.simple_spinner_item, st_minList);
        ed_minSpinnerAdapter = new ArrayAdapter<String>(BoardWriter.this, android.R.layout.simple_spinner_item, ed_minList);
        start_min.setAdapter(st_minSpinnerAdapter);
        end_min.setAdapter(ed_minSpinnerAdapter);

        genderList.add("남자");
        genderList.add("여자");
        genderSpinnerAdapter = new ArrayAdapter<String>(BoardWriter.this, android.R.layout.simple_spinner_item, genderList);
        gender.setAdapter(genderSpinnerAdapter);
    }

    private void setEventToComponents(){
        local_selector.setOnClickListener(findLocal);
        job.setOnClickListener(jobFilter);
        gender.setOnItemSelectedListener(selected_gender);

        start_year.setOnItemSelectedListener(selected_start_year);
        start_mon.setOnItemSelectedListener(selected_start_mon);
        start_day.setOnItemSelectedListener(selected_start_day);
        start_hour.setOnItemSelectedListener(selected_start_hour);
        start_min.setOnItemSelectedListener(selected_start_min);

        end_year.setOnItemSelectedListener(selected_end_year);
        end_mon.setOnItemSelectedListener(selected_end_mon);
        end_day.setOnItemSelectedListener(selected_end_day);
        end_hour.setOnItemSelectedListener(selected_end_hour);
        end_min.setOnItemSelectedListener(selected_end_min);
        image_selector.setOnClickListener(selectImage);

        ok_button.setOnClickListener(ok_event);
        cancel_button.setOnClickListener(cancel_event);
    }

    private void initializeView(){
        board_title = (EditText)findViewById(R.id.bw_title);
        store_name = (EditText)findViewById(R.id.bw_storename);
        local_selector = (Button)findViewById(R.id.bw_sido);
        local_sub = (EditText)findViewById(R.id.bw_positiondetail);
        phone = (EditText)findViewById(R.id.bw_telephone);

        start_year = (Spinner)findViewById(R.id.bw_year);
        start_mon = (Spinner)findViewById(R.id.bw_month);
        start_day = (Spinner)findViewById(R.id.bw_day);
        start_hour = (Spinner)findViewById(R.id.bw_hour);
        start_min = (Spinner)findViewById(R.id.bw_min);

        end_year = (Spinner)findViewById(R.id.bw_end_year);
        end_mon = (Spinner)findViewById(R.id.bw_end_month);
        end_day = (Spinner)findViewById(R.id.bw_end_day);
        end_hour = (Spinner)findViewById(R.id.bw_end_hour);
        end_min = (Spinner)findViewById(R.id.bw_end_min);

        urgency = (EditText)findViewById(R.id.bw_money);
        gender = (Spinner)findViewById(R.id.bw_gender);

        condition = (EditText)findViewById(R.id.bw_condition);
        job = (Button)findViewById(R.id.bw_job);
        favorable_condition = (EditText)findViewById(R.id.bw_plus);
        detail = (EditText)findViewById(R.id.bw_addinformation);
        image_selector = (Button)findViewById(R.id.board_img_select);

        ok_button = (Button)findViewById(R.id.bw_okButton);
        cancel_button = (Button)findViewById(R.id.bw_cancelButton);
        selected_image = (ImageView)findViewById(R.id.selected_image);
        image_path = (TextView)findViewById(R.id.img_path);
    }
}