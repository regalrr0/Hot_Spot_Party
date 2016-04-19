package theregaltreatment.hotspotparty;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * A login screen that offers login via email/password.
 */
public class loginActivity2 extends AppCompatActivity implements LoaderCallbacks<Cursor>, OnItemSelectedListener {

    protected String response;

    Boolean registering = false;

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    //private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    httpUrlConn mAuthTask = null;

    // UI references.
    private TextView mEmailView;
    private EditText mPasswordView, cPassword, usernameView, fname, age, lname;
    private View mProgressView;
    private View mLoginFormView;
    private Spinner spinner;
    private Button mEmailSignInButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (TextView) findViewById(R.id.email);
        //populateAutoComplete();

        lname = (EditText) findViewById(R.id.last_name);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });


        cPassword = (EditText) findViewById(R.id.confirm_pass);
        usernameView = (EditText) findViewById(R.id.username);
        fname = (EditText) findViewById(R.id.first_name);
        age = (EditText) findViewById(R.id.Age);


        // button that logs in existing users
        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!registering) {
                    try {
                        attemptLogin();
                    } catch (Exception e) {
                        Log.i("FAIL", "We are here");
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        signUp();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // button to make hidden fields appear
        registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        spinner = (Spinner) findViewById(R.id.spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


    }



    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mPasswordView.setError(null);
        usernameView.setError(null);

        // Store values at the time of the login attempt.
        String username = usernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
/*
TODO: Move this to register area to see if email is a valid email address
       // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        */

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            HashMap<String, String> map = new HashMap<>();
            map.put("username", username);
            map.put("pass", password);
            mAuthTask = new httpUrlConn(map, "http://hive.sewanee.edu/evansdb0/android1/scripts/hotPartyLogin.php");

            mAuthTask.execute();

            try {
                if(mAuthTask.get().contains("true")) {
                    String r = mAuthTask.get().replace("true // ","");
                    Log.i("response", "dsfdsf means we got dis shit");
                    Toast.makeText(getApplicationContext(),r,Toast.LENGTH_LONG).show();
                    Intent i = new Intent(this, chooseEvent.class);
                    i.putExtra("username", username);
                    startActivity(i);

                }
                else {
                    String[] resp = response.split(",");
                    for(String i: resp)
                        Toast.makeText(getApplicationContext(),i,Toast.LENGTH_LONG).show();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if (mAuthTask == null || mAuthTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
                mAuthTask = null;
            }
        }
    }


    // TODO: Add new user to database
    public void signUp() throws ExecutionException, InterruptedException {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mPasswordView.setError(null);
        usernameView.setError(null);
        lname.setError(null);
        mEmailView.setError(null);
        cPassword.setError(null);
        age.setError(null);
        fname.setError(null);

        // Store values at the time of the login attempt.
        String username = usernameView.getText().toString();
        String password = mPasswordView.getText().toString();
        String email = mEmailView.getText().toString();
        String lnameS = lname.getText().toString();
        String fnameS = fname.getText().toString();
        String conPass = cPassword.getText().toString();
        String ageS = age.getText().toString();
        String gender = spinner.getSelectedItem().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            HashMap<String, String> map = new HashMap<>();
            map.put("userName", username);
            map.put("pass", password);
            map.put("age", ageS);
            map.put("email", email);
            map.put("fName", fnameS);
            map.put("lName", lnameS);
            map.put("sex", gender);
            map.put("cPass", conPass);

            mAuthTask = new httpUrlConn(map, "http://hive.sewanee.edu/evansdb0/android1/scripts/hotPartySignUp.php");

            mAuthTask.execute();
            // if php inserted credentials into the database
            Log.i("mAuthTask.get is:", mAuthTask.get());
            try {
                if(mAuthTask.get().contains("true")) {
                     String r = mAuthTask.get().replace("true,","");
                     Log.i("response", "dsfdsf means we inserted shit");
                     Toast.makeText(getApplicationContext(),r,Toast.LENGTH_LONG).show();
                    Intent i = new Intent(this,loginActivity2.class);
                    startActivity(i);

                 }
                 else {
                     String[] resp = response.split(",");
                     for(String i: resp)
                         Toast.makeText(getApplicationContext(),i,Toast.LENGTH_LONG).show();
                 }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if(mAuthTask == null || mAuthTask.getStatus().equals(AsyncTask.Status.FINISHED))
                mAuthTask = null;
        }
    }

    public void register() {
        if(!registering) {
            registerButton.setText("Back to Sign-in");
            cPassword.setVisibility(View.VISIBLE);
            fname.setVisibility(View.VISIBLE);
            usernameView.setVisibility(View.VISIBLE);
            age.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.VISIBLE);
            mEmailView.setVisibility(View.VISIBLE);
            lname.setVisibility(View.VISIBLE);
            registering = true;
        }
        else{
            registerButton.setText("Not Registered?");
            cPassword.setVisibility(View.INVISIBLE);
            fname.setVisibility(View.INVISIBLE);
            usernameView.setVisibility(View.VISIBLE);
            age.setVisibility(View.INVISIBLE);
            spinner.setVisibility(View.INVISIBLE);
            mEmailView.setVisibility(View.INVISIBLE);
            lname.setVisibility(View.INVISIBLE);
            mEmailSignInButton.setVisibility(View.VISIBLE);
            registering = false;
        }
        //registerButton.setVisibility(View.INVISIBLE);
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 0;
    }


    /** Begin class
     * The start for the class that will handle our user input
     */
    //------------------------------------------------------------------------------------------

    public class httpUrlConn extends AsyncTask<Void, Void, String> {

        private HashMap<String,String> map = null;
        private String url;
        private final String USER_AGENT = "Mozilla/5.0";

        public httpUrlConn() {
        }

        public httpUrlConn(HashMap<String,String> _map, String _url) {
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

            String params ="";

            // key=value&key=value

            // make the url params that will be sent
            Iterator it = map.entrySet().iterator();
            int size = map.size();
            while(it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                if(size>1)
                    params += pair.getKey() + "=" + pair.getValue() + "&";
                else
                    params += pair.getKey() + "=" + pair.getValue();
                size--;
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
            Log.i("Response is:", response);
            return response;
        }

        @Override
        protected void onPostExecute(final String success){
            //response = success;
            Log.i("Response for OPE is", response);
            mAuthTask = null;
        }
    }








    //-------------------------------------------------------------------------------------------


    /** Begin class to store user credentials in phone
     */

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        //addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

   /* private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(loginActivity2.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }*/

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }







    // TODO: Possibly use this in the final version for autocompletion
/*
    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }
*/





    /**
     * Shows the progress UI and hides the login form.
     */
/*    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
*/
}