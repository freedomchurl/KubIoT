package vaninside.kubiot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> pushArray;
    private ListView mListView;
    private ListAdapter mListAdapter;
    Button refreshButton;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        refreshButton = findViewById(R.id.listbutton);

        refreshButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                       public void onClick(View v) {
                                           try {
                                               HttpUtil util = new HttpUtil("http://101.101.219.90:7878/push/message/getpushlist");
                                               String str = util.execute().get();
                                               Log.d("notice", str);

                                               JSONObject obj = new JSONObject(str);
                                               JSONArray rs = (JSONArray)obj.get("payload");

                                               pushArray.clear();
                                               for(int i=0; i<rs.length(); i++){
                                                   JSONObject objj = new JSONObject(rs.get(i).toString());
                                                   pushArray.add(objj.get("dName") + " 기기에서 이상을 감지하였습니다.");

                                               }
                                               mListAdapter.notifyDataSetChanged();
                                           }
                                           catch (Exception e) {
                                               // Error calling the rest api
                                               Log.e("REST_API", "GET method failed: " + e.getMessage());
                                               e.printStackTrace();
                                           }
                                       }
                                   });

                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (!task.isSuccessful()) {
                                    Log.w("FCM Log", "getInstanceId failed", task.getException());
                                    return;
                                }

                                String token = task.getResult().getToken();

                                new HttpUtil("http://101.101.219.90:7878/push/tokenadd?token="+token).execute();
                                //new HttpUtil("http://101.101.219.90:7878/push/message/addpushlist").execute();
                                Log.d("FCM LOG", "FCM 토큰" + token);
                                //Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                            }
                        });

        mListView = (ListView) findViewById(R.id.listview);

        pushArray = new ArrayList<>();

        // Connect to Adpater
        mListAdapter = new ListAdapter(getApplicationContext(), pushArray);
        mListView.setAdapter(mListAdapter);
    }
}
