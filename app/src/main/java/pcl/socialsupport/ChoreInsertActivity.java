package pcl.socialsupport;

/**
 * Created by Anup on 11/9/2015.
 */

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

import pcl.socialsupport.Utils.GetServerResponse;

public class ChoreInsertActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView titleTextView;
    private TextView titleIdView;
    Button submit;
    TextView message;
    DatePicker date;
    TimePicker time;
    TextView dateDisplay;
    TextView timeDisplay;
    String url = "";
    String host = "192.168.0.108";
    String server_path = "saware";
    String proto = "http";
    String fileName = "generate_activity.php";
    String title;
    String chore_id;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chore_insert);
        submit=(Button) findViewById(R.id.submit);
        message=(TextView) findViewById(R.id.message);
       // date=(DatePicker) findViewById(R.id.date);
      //  time=(TimePicker) findViewById(R.id.time);
        host = getString(R.string.server_ip);
        server_path = getString(R.string.pathname);

        submit.setOnClickListener(this);

        title =  getIntent().getExtras().get("name").toString();
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        dateDisplay = (TextView) findViewById(R.id.date_display);
        timeDisplay = (TextView) findViewById(R.id.time_display);


        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        String dayOfWeek = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
        int minute = c.get(Calendar.MINUTE);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        String time = hour + ":" + (minute>10?minute:"0"+minute);
        String date = dayOfWeek+"," +" " + month+"/"+day+"/"+year;


        timeDisplay.setText(time);
        dateDisplay.setText(date);


        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);
        //getSupportActionBar().setSubtitle(R.string.app_caption);


        // ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();




        chore_id = getIntent().getExtras().get("id").toString();


       // titleTextView = (TextView) findViewById(R.id.title);
        //titleIdView = (TextView) findViewById(R.id.grid_item_id);
        //titleTextView.setText(Html.fromHtml(title));
        //titleIdView.setText((Html.fromHtml(chore_id)));


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void onClick(View v) {
        String optionsValue = "";

        /*int day = date.getDayOfMonth();
        int month = date.getMonth();
        int year = date.getYear();

        String dateValue = day +"/" + month +"/" +year;
        int hour = time.getCurrentHour();//time.getHour(); //Not supported in API 19. Need to work it our
        int min = time.getCurrentMinute();//time.getMinute(); //Same as above

        String timeValue = hour + ":" + min;

*/

        String user_id = getResources().getString(R.string.user_id);
        String options = "date="+dateDisplay.getText()+"|time="+timeDisplay.getText();
        String msg = message.getText().toString();

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(proto)
                .authority(host)
                .appendEncodedPath(server_path)
                .appendPath(fileName)
                .appendQueryParameter("user_id","1")
                .appendQueryParameter("chore_id", chore_id)
                .appendQueryParameter("options",options)
                .appendQueryParameter("message",msg);

        url = builder.build().toString();

        new processServerResponse().execute(url);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);


    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment(timeDisplay);
        //newFragment.show(getSupportFragmentManager(), "timePicker");

        newFragment.show(getFragmentManager(),"Time Picker");
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment(dateDisplay);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public boolean isConnected()
    {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }



    private class processServerResponse extends GetServerResponse
    {
        @Override
        protected void onPostExecute(String result) {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(context, result,duration).show();
        }
    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
// the web page content as a InputStream, which it returns as
// a string.
  /*  private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds*//* );*/
         /*   conn.setConnectTimeout(15000 /* milliseconds *//*);*/
           /* conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            //Log.d("The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // displayed in the UI by the AsyncTask's onPostExecute method.
    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(context, result,duration).show();
        }
    }*/
}
