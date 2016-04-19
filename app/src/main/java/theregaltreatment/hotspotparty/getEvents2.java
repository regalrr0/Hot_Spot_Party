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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
    String getString, response;
    private ListView listEvents;
    customList eventListings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_get_events2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        i = getIntent();

        listEvents = (ListView) findViewById(R.id.listView);

        HashMap<String,String> map = new HashMap<>();

        if(i.getStringExtra("club") != null) {
            getString = i.getStringExtra("club");
            map.put("club",getString);
        }
        else if(i.getStringExtra("festival") != null) {
            getString = i.getStringExtra("festival");
            map.put("festival",getString);
        }
        else if(i.getStringExtra("sport") != null) {
            getString = i.getStringExtra("sport");
            map.put("sport",getString);
        }

        mAuthTask = new httpUrlConn(map,"http://hive.sewanee.edu/evansdb0/android1/scripts/getEvents.php");

        mAuthTask.execute();

        try {
            Log.i("mAuthTask.get is:", mAuthTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        try {
            if(mAuthTask.get().contains("")) {
                String [] r = mAuthTask.get().split(" /_/ ");
                Integer[] imageid = {
                        R.drawable.flamenight,
                        R.drawable.coolbackground
                                     };

                eventListings = new customList(this, r, imageid);

                listEvents.setAdapter(eventListings);

                //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, r);

                //listEvents.setAdapter(arrayAdapter);

                Log.i("response", "dsfdsf means we inserted shit");
                for(String j: r) {
                    //TODO: Create array of strings for array adaptor
                    Toast.makeText(getApplicationContext(), j, Toast.LENGTH_LONG).show();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(mAuthTask == null || mAuthTask.getStatus().equals(AsyncTask.Status.FINISHED))
            mAuthTask = null;
    }






        //usernView.setText(i.getStringExtra("username") + ", Welcome! Please select an event you are interested in and let us do the rest!");


    public void showEvents() {
        mAuthTask = new httpUrlConn(null, "http://hive.sewanee.edu/evansdb0/android1/scripts/getEvents.php");

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
