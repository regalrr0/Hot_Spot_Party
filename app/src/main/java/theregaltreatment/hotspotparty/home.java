package theregaltreatment.hotspotparty;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class home extends loginActivity2 implements View.OnClickListener {

    TextView emaild, fname;
    private ListView obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        /*ArrayList array_list = dbh.getAllUsers();
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1, array_list);

        obj = (ListView)findViewById(R.id.listView1);
        obj.setAdapter(arrayAdapter);
*/
        /*
        //findViewById(R.id.sign_out_button).setOnClickListener(this);

        //googleConnection.getAccountEmail();
        //String Name = googleConnection.getFirstName();
        //users user = dbh.getRecord(emailAddress);
        //fname = (TextView) findViewById(R.id.nameView);
        //fname.setText(Name);
        //emaild = (TextView) findViewById(R.id.email);


        Bundle extras = getIntent().getExtras();
        String email3= extras.getString("EMAIL2");
        users user = dbh.getRecord(email3);

        emaild.setText(user.getEmail());
        /*login state = getApplicationContext();
        setContentView(R.layout.activity_home);
        String currentAccount1 = Plus.AccountApi.getAccountName(state.mGoogleApiClient);
        ((TextView) findViewById(R.id.email)).setText(currentAccount1);
        */
    }



    private void onSignOutClicked() {
        finish();
    }

    public void onClick (View v) {
        /*switch (v.getId()) {
            case R.id.sign_out_button:
                onSignOutClicked();
                break;
        }*/

    }
}