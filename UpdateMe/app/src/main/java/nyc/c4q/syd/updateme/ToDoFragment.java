package nyc.c4q.syd.updateme;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by July on 7/19/15.
 */
public class ToDoFragment extends Fragment {
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private AlertDialog.Builder dialogBuilder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.todo_layout, container, false);
        lvItems = (ListView) v.findViewById(R.id.list);
        items = new ArrayList<String>();
        readItems();
        itemsAdapter = new ArrayAdapter<String>(getActivity(), R.layout.todo_list, R.id.text, items);
        lvItems.setAdapter(itemsAdapter);
       // setListViewHeightBasedOnChildren(lvItems);
        if (items.size() == 0)
            items.add("Add your first to do list now!");

        ImageButton add = (ImageButton) v.findViewById(R.id.add);
        add.setOnClickListener(addTODOListener);
        lvItems.setOnItemClickListener(lvItemClickListener);
        lvItems.setOnItemLongClickListener(lvItemLongClickListener);
        return v;
    }

    // save and load items from to-do list
    private void readItems() {
        File filesDir = getActivity().getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    private void writeItems() {
        File filesDir = getActivity().getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // add item to to-do list & adjust view size
    View.OnClickListener addTODOListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialogBuilder = new AlertDialog.Builder(getActivity());
            final EditText todoET = new EditText(getActivity());
            dialogBuilder.setTitle("Add Todo Task Item")
                    .setMessage("What is on your list today?")
                    .setView(todoET)
                    .setPositiveButton("Add Task", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String itemText = todoET.getText().toString();
                            items.add(itemText);
//                            setListViewHeightBasedOnChildren(lvItems);
                        }
                    });
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
            writeItems();
        }
            };

    // option to delete item from to-do list
    AdapterView.OnItemClickListener lvItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapter, View item, final int pos, long id) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
            dialogBuilder.setTitle("Remove Task")
                    .setMessage("Have you completed this task?")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            items.remove(pos);
                            itemsAdapter.notifyDataSetChanged();
                            writeItems();
//                            setListViewHeightBasedOnChildren(lvItems);
                            Toast.makeText(getActivity(), "Well done!", Toast.LENGTH_LONG).show();
                        }
                    });
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
        }
        };



    AdapterView.OnItemLongClickListener lvItemLongClickListener = (new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, long id) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
            final DatePicker setDate = new DatePicker(getActivity());
            setDate.setSpinnersShown(false);
            dialogBuilder.setTitle("Set Reminder on Date")
                    .setView(setDate)
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Next", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            notificationDate(items.get(position),
                                    setDate.getYear(), setDate.getMonth(), setDate.getDayOfMonth(),
                                    parent, position);
                        }
                    });
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
            return true;
        }

    });


    public void notificationDate(final String task, final int year, final int month, final int day,
                                 final AdapterView<?> parent, final int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        final TimePicker setTime = new TimePicker(getActivity());
        dialogBuilder.setTitle("Set Reminder at Time")
                .setView(setTime)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Set Reminder", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setNotification(task, year, month, day,
                                setTime.getCurrentHour(), setTime.getCurrentMinute());
                        //TODO: make invisible again?
                        View image = parent.getChildAt(position);
                        image.findViewById(R.id.icon).setVisibility(View.VISIBLE);
                    }
                });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    public void setNotification(String task, int year, int month, int day, int hour, int min) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, min);
        long millis = cal.getTimeInMillis();

        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        intent.putExtra("task", task);
        PendingIntent mAlarmSender = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
        AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, millis, mAlarmSender);
    }
}

