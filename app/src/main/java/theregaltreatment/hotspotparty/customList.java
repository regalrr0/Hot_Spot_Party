package theregaltreatment.hotspotparty;

/**
 * Created by Danny, Ryan, Keshonn, and Blaise on 4/19/2016.
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class customList extends ArrayAdapter<String>{

    private final Activity context;
    private final String[] web;
    private final int[] eventId;
    private Bitmap [] imageId;
    public customList(Activity context, String[] web, int[] eventId, Bitmap[] imageId) {
        super(context, R.layout.list_single, web);
        this.context = context;
        this.web = web;
        this.eventId = eventId;
        this.imageId = imageId;

    }
    public void logEventId() {
        for(int i : eventId) {
            Log.i("eventId", Integer.valueOf(i).toString());
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        txtTitle.setText(web[position]);

        imageView.setImageBitmap(imageId[position]);
        return rowView;
    }
    public int getId(int position) {
        return eventId[position];
    }
}
