package theregaltreatment.hotspotparty;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class displayinginfo extends AppCompatActivity {

    ImageView imgView = null;
    TextView name = null;
    TextView desc = null;
    TextView date = null;
    TextView address = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayinginfo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imgView = (ImageView) findViewById(R.id.img);
        name = (TextView) findViewById(R.id.name);
        desc = (TextView) findViewById(R.id.description);
        date = (TextView) findViewById(R.id.date);
        address = (TextView) findViewById(R.id.address);

        Intent i = getIntent();

        String eventInfo = i.getStringExtra("eventInfo");

        String[] eventInfo_arr = eventInfo.split(" /_/ ");

        ImageDownloader task = new ImageDownloader();
        Bitmap image = null;
        try {
            image = task.execute(eventInfo_arr[0]).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        imgView.setImageBitmap(image);
        String event = eventInfo_arr[1] + "\n\n\n\n" + eventInfo_arr[2] +

                "\n\n\n" + "Additional Details: " + eventInfo_arr[3] + "\n\n\n\nWHERE?: " + eventInfo_arr[4]

                + "\n\n\n\nWHEN?: " + eventInfo_arr[5];
        name.setText(event);
        //desc.setText(description);
        //date.setText();
        //address.setText(eventInfo_arr[5]);

        for (String s : eventInfo_arr) {
            Log.i("EventInfo", s);
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
