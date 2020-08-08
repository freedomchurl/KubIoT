package vaninside.kubiot;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class HttpUtil extends AsyncTask<String, Void, String> {
   public String mURL;

    public HttpUtil(String url) {
        mURL = url;
    }

    @Override
    public String doInBackground(String... params) {
        try {
            String result = null;

            URL url = new URL(mURL);
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
            Log.d("Result", result);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
