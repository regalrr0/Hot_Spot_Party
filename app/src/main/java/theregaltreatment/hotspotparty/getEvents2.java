package theregaltreatment.hotspotparty;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.MalformedInputException;
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

        HashMap<String, String> map = new HashMap<>();

        if (i.getStringExtra("club") != null) {
            getString = i.getStringExtra("club");
            map.put("club", getString);
        } else if (i.getStringExtra("festival") != null) {
            getString = i.getStringExtra("festival");
            map.put("festival", getString);
        } else if (i.getStringExtra("sport") != null) {
            getString = i.getStringExtra("sport");
            map.put("sport", getString);
        }




        mAuthTask = new httpUrlConn(map, "http://hive.sewanee.edu/evansdb0/android1/scripts/getEvents.php");

        mAuthTask.execute();

        try {
            Log.i("mAuthTask.get is:", mAuthTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        try {
            if (mAuthTask.get().contains("")) {
                int rows;
                String[] r = mAuthTask.get().split(" /_/ ");

                Log.i("length", Integer.valueOf(r.length).toString());

                // num rows from database
//                rows = Integer.valueOf(r[0]);

               String[] textForList = new String[r.length/2];

                String[] urls = new String[r.length/2];


                // to be populate after async task

                Bitmap[] image = new Bitmap[r.length/2];

                Integer [] imageid = new Integer[r.length/2];

                int x = 0;
                int y=0;

                //trying this

                //Bitmap preview  = BitmapFactory.decodeStream(null, options);

                Drawable d []=  new Drawable[r.length/2];
                ImageDownloader task[] = new ImageDownloader[r.length/2];
                for(int j = 0; j < r.length; j+=2 ) {

                    textForList[x] = r[j];
                    urls[x] = r[j+1];
                    task[x] = new ImageDownloader();
                    image [x] = task[x].execute(urls[x]).get();
                    d[x] = new BitmapDrawable(getResources(), image[x]);
                    Log.i("TextForList", textForList[x]);
                    Log.i("URLS", urls[x]);
                    Log.i("J=", Integer.valueOf(x).toString());
                    x++;

                }


                eventListings = new customList(this, textForList, image);

                listEvents.setAdapter(eventListings);

                //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, r);

              //  listEvents.setAdapter(eventListings);

                //Log.i("response", "dsfdsf means we inserted shit");
                for (String j : r) {
                    //TODO: Create array of strings for array adaptor
                    Toast.makeText(getApplicationContext(), j, Toast.LENGTH_LONG).show();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (mAuthTask == null || mAuthTask.getStatus().equals(AsyncTask.Status.FINISHED))
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


    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.connect();

                InputStream iStream = conn.getInputStream();
                //BitmapFactory.Options options = new BitmapFactory.Options();
                //options.inSampleSize = 8;

                Bitmap myBitmap = BitmapFactory.decodeStream(iStream);//,null, options);

                return myBitmap;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
