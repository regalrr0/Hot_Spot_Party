package theregaltreatment.hotspotparty;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class getEvents2 extends AppCompatActivity {

    httpUrlConn mAuthTask = null;
    private Button disBooty;
    private Intent i;
    private TextView usernView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_get_events2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        i = getIntent();


        usernView.setText(i.getStringExtra("username") + ", ARE YOU READY TO HAVE THE NIGHT OF YOUR LIFE???");




        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }


    public void showEvents(){
        mAuthTask = new httpUrlConn(null, "http://hive.sewanee.edu/evansdb0/android/getEvents.php");

        mAuthTask.execute();

        try {
                String r = mAuthTask.get();
                Log.i("response", "dsfdsf means we inserted shit");
                Toast.makeText(getApplicationContext(), r, Toast.LENGTH_LONG).show();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public class httpUrlConn extends AsyncTask<Void, Void, String> {
        protected String response;
        private HashMap<String, String> map = null;
        private String url;
        private final String USER_AGENT = "Mozilla/5.0";

        public httpUrlConn() {
        }

        public httpUrlConn(HashMap<String, String> _map, String _url) {
            map = _map;
            url = _url;
        }

        // HTTP POST request
        public String sendPost() throws Exception {

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            //add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            String params = "";

            // key=value&key=value

            // make the url params that will be sent
            if (map != null) {
                Iterator it = map.entrySet().iterator();
                int size = map.size();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    if (size > 1)
                        params += pair.getKey() + "=" + pair.getValue() + "&";
                    else
                        params += pair.getKey() + "=" + pair.getValue();
                    size--;
                }
            }
            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(params);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            Log.i("Request to URL : ", url);
            Log.i("Post parameters : ", params);
            Log.i("Response Code : ", Integer.valueOf(responseCode).toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer r = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                r.append(inputLine);
            }
            in.close();

            // store the result in the instance variable response

            Log.i("Send Post:", r.toString());
            return r.toString();

        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                response = sendPost();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(final String success) {
            mAuthTask = null;
        }

    }
}
