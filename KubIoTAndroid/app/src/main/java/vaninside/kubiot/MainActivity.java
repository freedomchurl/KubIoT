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
                                               /*
                                               // Open the connection
                                               URL url = new URL("http://localhost:8080/test");
                                               HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                               conn.setRequestMethod("GET");
                                               InputStream is = conn.getInputStream();

                                               // Get the stream
                                               StringBuilder builder = new StringBuilder();
                                               BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                                               String line;
                                               while ((line = reader.readLine()) != null) {
                                                   builder.append(line);
                                               }

                                               // Set the result
                                               result = builder.toString();
*/
                                               String str = "{\"value\":[\"DEVICE 01 ERROR\", \"DEVICE 03 ERROR\", \"DEVICE 05 ERROR\"]}";
                                               JSONObject obj = new JSONObject(str);
                                               JSONArray rs = (JSONArray)obj.get("value");

                                               pushArray.clear();
                                               for(int i=0; i<rs.length(); i++){
                                                   pushArray.add(rs.get(i).toString());
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
                                Log.d("FCM LOG", "FCM 토큰" + token);
                                //Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                            }
                        });

        mListView = (ListView) findViewById(R.id.listview);

        // 데이터 생성
        pushArray = new ArrayList<>();
        pushArray.add("test01");
        pushArray.add("test02");

        // 어댑터 연결
        mListAdapter = new ListAdapter(getApplicationContext(), pushArray);
        mListView.setAdapter(mListAdapter);
    }
}
