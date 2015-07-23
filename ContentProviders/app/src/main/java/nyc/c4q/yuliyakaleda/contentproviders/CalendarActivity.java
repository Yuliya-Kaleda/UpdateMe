package nyc.c4q.yuliyakaleda.contentproviders;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by July on 7/21/15.
 */
public class CalendarActivity extends Activity {
    private TextView tv;
    private ListView lv;
    private static final String CALENDAR_ID = "3";
    private static final long EVENT_ID = 3225; // he inserted a row in insert() that returned this id
    long JULY_1_2015;
    long JULY_30_2015;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);
        tv = (TextView) findViewById(R.id.tv);
        lv = (ListView) findViewById(R.id.lv);
        //fetchCalendars();
        //fetchEvents();
        //insertEvent();
        //update();
        //delete();
    }

    public void fetchCalendars() {
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String[] columns = new String[] {
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.ACCOUNT_NAME,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                CalendarContract.Calendars.OWNER_ACCOUNT
        };

        Cursor cursor = getContentResolver().query(
                uri,
                columns,
                CalendarContract.Calendars.ACCOUNT_NAME + " = ?",
                new String[] {"yulia.koleda1988@gmail.com"},
                null
        );

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex(CalendarContract.Calendars._ID));
            String accountName = cursor.getString(1);
            String displayName = cursor.getString(2);
            String owner = cursor.getString(3);
            Log.d("ContentProvider", "ID: " + id +
                            ", account: " + accountName +
                            ", displayName: " + displayName +
                            ", owner: " + owner
            );
        }
    }

    public void fetchEvents() {
        Uri uri = Events.CONTENT_URI;
        String [] columns = new String[] { //columns I want to return
                Events._ID,
                Events.CALENDAR_ID, // Calendars._ID
                Events.ORGANIZER,
                Events.TITLE,
                Events.EVENT_LOCATION,
                Events.DESCRIPTION,
                Events.DTSTART,
                Events.DTEND,
        };

        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, Calendar.JULY, 1);
        JULY_1_2015 = calendar.getTimeInMillis();

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2015, Calendar.JULY, 30);
        JULY_30_2015 = calendar2.getTimeInMillis();

        String filter = Events.CALENDAR_ID + " = ? AND " +
                Events.DTSTART + " > ?" +
                Events.DTSTART + " < ?";
        String[] filterArgs = new String[] {CALENDAR_ID, String.valueOf(JULY_1_2015), String.valueOf(JULY_30_2015)}; //filterArgs takes just Strings

        String sortOrder = Events.DTSTART + " DESC LIMIT 100";

        Cursor cursor = getContentResolver().query(
                uri,
                columns,
                filter,
                filterArgs,
                sortOrder
        );

        ListAdapter listAdapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_expandable_list_item_2,
                cursor,
                new String[] {Events._ID, Events.TITLE}, // what will be displayed
                new int[] {android.R.id.text1, android.R.id.text2},
                0
        );

        lv.setAdapter(listAdapter);
    }

    public void insertEvent() {
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2015, Calendar.JULY, 21, 19, 0);
        long startMillis = beginTime.getTimeInMillis();

        Calendar endTime = Calendar.getInstance();
        endTime.set(2015, Calendar.JULY, 21, 22, 0);
        long endMillis = endTime.getTimeInMillis();

        ContentValues values = new ContentValues();
        values.put(Events.DTSTART, startMillis);
        values.put(Events.DTEND, endMillis);
        values.put(Events.TITLE, "Access Code");
        values.put(Events.DESCRIPTION, "Content providers");
        values.put(Events.CALENDAR_ID, CALENDAR_ID);
        values.put(Events.EVENT_TIMEZONE, "America/New_York");

        Uri uri = getContentResolver().insert(Events.CONTENT_URI, values); //always return url of the row that was inserted

        long eventId = Long.parseLong(uri.getLastPathSegment());

        tv.setText("Event ID: " + eventId);
    }

    public void update() {
        ContentValues values = new ContentValues();
        values.put(Events.TITLE, "Access Code ");

        //content://com.android.calendar/events/3225
        Uri uri = ContentUris.withAppendedId(Events.CONTENT_URI, EVENT_ID);
        getContentResolver().update(uri, values, null, null);
    }

    public void delete() {
        Uri uri = ContentUris.withAppendedId(Events.CONTENT_URI, EVENT_ID);
        getContentResolver().delete(uri, null, null);
    }
}
