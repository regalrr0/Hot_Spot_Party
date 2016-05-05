package theregaltreatment.hotspotparty;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class getEvents2 extends AppCompatActivity {

    private ListView listEvents;
    customList eventListings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_get_events2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();

        listEvents = (ListView) findViewById(R.id.listView);

        HashMap<String, String> map = new HashMap<>();

        map = loadMapParams(map,i);

        HttpUrlConn mAuthTask = new HttpUrlConn(map, getURL("getEvents.php"));

        mAuthTask.execute();
        try {
            // if we get a response from the server
            if (!mAuthTask.get().equals("")) {

                Log.i("mAuthTask.get is:", mAuthTask.get());

                // load each column into the r array
                String[] r = mAuthTask.get().split(" /_/ ");

                // create the arrays for the text displayed in the listView,
                // the urls that correspond to the images in the listView
                // and the bitMaps, which contain the info to set the Image view's resource
                String[] textForList = new String[r.length/2];
                String[] urls = new String[r.length/2];
                Bitmap[] image = new Bitmap[r.length/2];
                int[]    eventId = new int[r.length/2];


                Drawable d []=  new Drawable[r.length/2];
                // an array of extended async tasks to handle the download for each image
                ImageDownloader task[] = new ImageDownloader[r.length/2];
                int x = 0;
                for(int j = 0; j < r.length; j+=3 ) {

                        textForList[x] = r[j];
                        urls[x] = r[j + 1];
                        eventId[x] = Integer.parseInt(r[j + 2]);
                        task[x] = new ImageDownloader();
                        image[x] = task[x].execute(urls[x]).get();
                        d[x] = new BitmapDrawable(getResources(), image[x]);
                        debugInfo(textForList, x, urls,eventId);
                        x++;

                }
                // create custom listview array adapter
                eventListings = new customList(this, textForList, eventId, image);
                listEvents.setAdapter(eventListings);

                listEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
//                      Toast.makeText(getApplicationContext(), Integer.valueOf(eventListings.getId(position)).toString(), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getEvents2.this,displayinginfo.class);
                        HashMap<String,String> map = new HashMap<String,String>();

                        HttpUrlConn mAuthTask = new HttpUrlConn(map,getURL("displayEventInfo.php"));
                        map.put("eventId", Integer.valueOf(eventListings.getId(position)).toString());
                        mAuthTask.execute();

                        try {
                            i.putExtra("eventInfo", mAuthTask.get());
                            startActivity(i);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
       // if (mAuthTask == null || mAuthTask.getStatus().equals(AsyncTask.Status.FINISHED))
         //   mAuthTask = null;
    }
    public void debugInfo(String[] textForList, int x, String[] urls, int[] eventId) {
        Log.i("TextForList", textForList[x]);
        Log.i("URLS", urls[x]);
        Log.i("eventId", "" + eventId[x]);
        Log.i("J=", Integer.valueOf(x).toString());
    }
    public void pInt(String l, int i) {
        Log.i(l,"" + i);
    }
    public void pString(String l, String i) {
        Log.i(l,"" + i);
    }
    public HashMap<String,String> loadMapParams(HashMap<String, String> map, Intent i) {
        if (i.getStringExtra("club") != null) {
            map.put("club", i.getStringExtra("club"));
        } else if (i.getStringExtra("festival") != null) {
            map.put("festival", i.getStringExtra("festival"));
        } else if (i.getStringExtra("sport") != null) {;
            map.put("sport", i.getStringExtra("sport"));
        }
        return map;
    }
    public String getURL(String end) {
        return "http://hive.sewanee.edu/evansdb0/android1/scripts/" + end;
    }

    public class HttpUrlConn extends AsyncTask<Void, Void, String> {
        protected String response;
        private HashMap<String, String> map = null;
        private String url;
        private final String USER_AGENT = "Mozilla/5.0";

        public HttpUrlConn(HashMap<String, String> _map, String _url) {
            map = _map;
            url = _url;
        }
        public void setMap(HashMap<String,String> map) {
            this.map = map;
        }
        public void setURL(String url) {
            this.url = url;
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

       /* @Override
        protected void onPostExecute(final String success) {
            mAuthTask = null;
        } */

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
