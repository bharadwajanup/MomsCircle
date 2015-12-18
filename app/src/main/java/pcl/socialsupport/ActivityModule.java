package pcl.socialsupport;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import pcl.socialsupport.Utils.QuickstartPreferences;
import pcl.socialsupport.Utils.RegistrationIntentService;

/**
 * Created by Anup on 11/25/2015.
 */
public class ActivityModule extends AppCompatActivity implements View.OnClickListener{


    private Button motherButton;
    private Button helperButton;
    private Toolbar toolbar;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    Toast.makeText(getApplicationContext(), "GCM Service available!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "GCM Service Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        };





        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(R.string.app_name);


        motherButton = (Button) findViewById(R.id.mother);
        helperButton = (Button) findViewById(R.id.helper);

        motherButton.setOnClickListener(this);
        helperButton.setOnClickListener(this);
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        //Implemented auto-redirect based on user_ids

        int user_id = Integer.parseInt(getString(R.string.user_id));

        Intent intent;
        if(user_id == 1)
        {
           intent =  new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }
        else if (user_id == 2 || user_id == 3)
        {
            intent =  new Intent(getApplicationContext(),HelperMainActivity.class);
            startActivity(intent);
        }




    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "Error in onResume!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onClick(View v) {



        startModule(v.getId());



    }

    public void startModule(int buttonId)
    {
        Context context = getApplicationContext();
        Intent intent;
        int duration = Toast.LENGTH_SHORT;
        switch (buttonId)
        {
            case R.id.mother:intent = new Intent(context,MainActivity.class);
                break;
            case R.id.helper:intent = new Intent(context,HelperMainActivity.class);
                break;
            default:Toast.makeText(context, "Apparently that didn't go well...",duration).show();return;
        }
        startActivity(intent);
    }
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("TAG","This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
