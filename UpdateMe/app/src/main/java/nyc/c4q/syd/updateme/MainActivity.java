package nyc.c4q.syd.updateme;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuliya Kaleda on 6/25/15.
 */

public class MainActivity extends Activity implements JobSearchAsync.MyListener {

    //variable to eliminate job related toasts when there is no Internet connection and jobs array does not have data
    public static boolean notConnected;

    private ArrayList<JobPosition> jobList;
    private JobCard jobCard;
    private MainAdapter adapter;
    private ProgressBar progressBar;
    public ArrayList<StockInfo> stockList;
    private  SharedPreferences sp;
    private FragmentManager fm;
    private ToDoFragment toDoFragment;
    private JobFragment jobFragment;
    private MyMapFragment mapFragment;
    private StockFragment stockFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create a jobList container for data which will get returned from jobAsync
        jobList = new ArrayList<JobPosition>();
        stockList = new ArrayList<StockInfo>();

        if (!noNetwork()) {
            notConnected = false;
            //start jobs JSON parsing and fetching the data for the default java positions
            JobSearchAsync jobSearchAsync = new JobSearchAsync(this);
            jobSearchAsync.setListener(this);
            jobSearchAsync.execute("java");
        }

        else {
            //if there is no connection the jobs card gets populated with previously saved data
            notConnected = true;
            Toast.makeText(this, "Sorry, there is no Internet connection", Toast.LENGTH_LONG).show();
            getDataFromSharedPref();
        }

        fm = getFragmentManager();

        //set a progress bar for jobs loading
        progressBar = (ProgressBar) findViewById(R.id.progress);

        //create a list of different card types
        ArrayList<Card> cards = new ArrayList<Card>();
        StockCard stockCard = new StockCard();
        jobCard = new JobCard(jobList);
        ToDoCard todoCard = new ToDoCard("Items");
        MapCard mapCard = new MapCard("Map");

        cards.add(todoCard);
        cards.add(jobCard);
        cards.add(mapCard);
        cards.add(stockCard);

        //fragments for the tablet layout
        toDoFragment = new ToDoFragment();
        jobFragment = new JobFragment();
        mapFragment = new MyMapFragment();
        stockFragment = new StockFragment();

        if (!Utils.isTablet(this)) {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
            adapter = new MainAdapter(this, cards);
            recyclerView.setAdapter(adapter);
        }
        else {
            fm.beginTransaction().replace(R.id.details_fragment, toDoFragment).addToBackStack(null).commit();
        }
    }

    //method to get Data from jobAsync and update
    @Override
    public void onLoadComplete(List<JobPosition> jobs) {
        //save jobs data from the user's previous session for the case when there is no network connection
        saveJobsData(jobs);
        //setter in JobCard to update List
        jobCard.setJobArray(jobs);
        //very important! when all the Recycler View is set up, it is not populated bc jobAsync didn't finish parsing and returning data
        if (!Utils.isTablet(this)) {
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    //method to check Internet connection
    private boolean noNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return true;
        } else
            return false;
    }

    //method to get previously saved data from shared preferences to populate job card when there is no network connection
    private void getDataFromSharedPref() {
        sp = getSharedPreferences("shared",0);
        JobPosition jp1 = new JobPosition(sp.getString("job1", "null"), sp.getString("company1", "null"), null);
        JobPosition jp2 = new JobPosition(sp.getString("job2", "null"), sp.getString("company2", "null"), null);
        JobPosition jp3 = new JobPosition(sp.getString("job3", "null"), sp.getString("company3", "null"), null);
        jobList.add(jp1);
        jobList.add(jp2);
        jobList.add(jp3);

    }

    //save jobs data for the case when network is lost
    public void saveJobsData(List<JobPosition> jobs) {
        sp = getSharedPreferences("shared", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("job1", jobs.get(0).getTitle());
        editor.putString("job2", jobs.get(1).getTitle());
        editor.putString("job3", jobs.get(2).getTitle());
        editor.putString("company1", jobs.get(0).getCompany());
        editor.putString("company2", jobs.get(1).getCompany());
        editor.putString("company3", jobs.get(2).getCompany());

        editor.commit();
    }

    public void addToDoFragment(View v) {
        fm.beginTransaction().replace(R.id.details_fragment, toDoFragment).addToBackStack(null).commit();
    }

    public void addJobFragment(View v) {
        fm.beginTransaction().replace(R.id.details_fragment, jobFragment).addToBackStack(null).commit();
    }

    public void addMapFragment(View v) {
       fm.beginTransaction().replace(R.id.details_fragment, mapFragment).addToBackStack(null).commit();
    }

    public void addStockFragment(View v) {
        fm.beginTransaction().replace(R.id.details_fragment, stockFragment).addToBackStack(null).commit();
    }
}

