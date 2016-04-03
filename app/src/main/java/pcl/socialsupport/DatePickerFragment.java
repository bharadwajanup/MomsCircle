package pcl.socialsupport;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private TextView tView;
    public DatePickerFragment(TextView view)
    {
        tView = view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Log.i("Datepicker","Date has been set");
        SimpleDateFormat simpledateformat = new SimpleDateFormat("E");
        Date dateF = new Date(year, month, day-1);
        String dayOfWeek = simpledateformat.format(dateF);
        //String dayOfWeek = .getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
        String date = dayOfWeek+","+" "+month+"/"+day+"/"+year;
        tView.setText(date);
    }
}
