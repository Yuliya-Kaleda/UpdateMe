package nyc.c4q.yuliyakaleda.contentproviders;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;


public class MainActivity extends ActionBarActivity {
    private EditText title;
    private EditText description;
    private EditText location;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = (EditText) findViewById(R.id.title);
        description = (EditText) findViewById(R.id.description);
        location = (EditText) findViewById(R.id.location);
        Button btn = (Button) findViewById(R.id.btn_create_event);


        btn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                String titleString = title.getText().toString();
                String descriptionString = description.getText().toString();
                String locationString = location.getText().toString();

//                Intent intent = new Intent(Intent.ACTION_INSERT);
//                intent.setData(CalendarContract.Events.CONTENT_URI);
//                intent.putExtra(CalendarContract.Events.TITLE, titleString);
//                intent.putExtra(CalendarContract.Events.DESCRIPTION, descriptionString);
//                intent.putExtra(CalendarContract.Events.EVENT_LOCATION, locationString);
//                intent.putExtra(Intent.EXTRA_EMAIL, "yulia.koleda1988@gmail.com");
//                startActivity(intent);
                insertEventInCalendar(titleString, descriptionString, locationString);
            }
        });
    }

    public void insertEventInCalendar(String title, String description, String location) {
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2015, Calendar.JULY, 21, 19, 0);
        long startMillis = beginTime.getTimeInMillis();

        Calendar endTime = Calendar.getInstance();
        endTime.set(2015, Calendar.JULY, 21, 22, 0);
        long endMillis = endTime.getTimeInMillis();

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.DESCRIPTION, description);
        values.put(CalendarContract.Events.CALENDAR_ID, 3);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "America/New_York");

        Uri uri = getContentResolver().insert(CalendarContract.Events.CONTENT_URI, values); //always return url of the row that was inserted

        long eventId = Long.parseLong(uri.getLastPathSegment());

        //tv.setText("Event ID: " + eventId);
    }
}
