package theregaltreatment.hotspotparty;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class chooseEvent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    public void clubs (View v){
        Intent i = new Intent(this, getEvents2.class);
        i.putExtra("club", "club");
        startActivity(i);
    }

    public void sports (View v){
        Intent i = new Intent(this, getEvents2.class);
        i.putExtra("sport", "sport");
        startActivity(i);
    }

    public void festivals (View v){
        Intent i = new Intent(this, getEvents2.class);
        i.putExtra("festival", "festival");
        startActivity(i);
    }

}
