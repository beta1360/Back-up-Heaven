package kr.ac.kumoh.s20130870.test_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;

public class MainActivity extends AppCompatActivity {
    private static final String ISONCLICK = "아무요청 없음";
    private static final String RESULT = "Hello world!!";
    private static final String SENDINGTOSERVER = "서버에 요청을 보냅니다.";
    private static final String MAIN_SERVER = "https://daeta.ga/test";

    public TextView testTextView = null;
    protected Button testButton = null;
    protected String test_result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testTextView = (TextView)findViewById(R.id.test_text);
        testButton = (Button)findViewById(R.id.test_button);

        testButton.setOnClickListener(mClickListener);
    }

    Button.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), SENDINGTOSERVER, Toast.LENGTH_SHORT).show();
            Thread thread = new Thread(){
                @Override
                public void run() {
                    HttpClient request = getHttpClient();

                    HttpParams timeout = request.getParams();
                    HttpConnectionParams.setConnectionTimeout(timeout, 5000);
                    HttpConnectionParams.setSoTimeout(timeout, 5000);

                    HttpPost httpPost = new HttpPost(MAIN_SERVER);

                    StringEntity postToJSON = null;

                    try {
                        postToJSON = new StringEntity("{\"user\":\"tester\",\"text\":\"Hello world! Hello server!\"}");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    httpPost.addHeader("content-type", "application/json;charset=UTF-8");
                    httpPost.setEntity(postToJSON);

                    try {
                        HttpResponse response = request.execute(httpPost);
                        String responseString = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                        JSONObject data = new JSONObject(responseString);

                        test_result = data.getString("test");
                        Log.i("test:",responseString);
                        Log.i("test:",test_result);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            testTextView.setText(test_result);
                        }
                    });
                }
            };
            thread.start();

        }
    };

    private HttpClient getHttpClient(){
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new SFSSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }
}
