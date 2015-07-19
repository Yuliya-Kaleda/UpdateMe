package nyc.c4q.syd.updateme;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.MapFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuliya Kaleda on 6/25/15.
 */

public class MainActivity extends FragmentActivity implements CardsListFragment.SendClickInfo {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void sendInfo(int positionClicked) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (positionClicked==0) {
            ft.replace(R.id.details_fragment, new ToDoFragment()).addToBackStack(null).commit();
        }
        else if (positionClicked==1) {
            ft.replace(R.id.details_fragment,new JobFragment()).addToBackStack(null).commit();
        }
//        else if (positionClicked==2) {
//            ft.replace(R.id.details_fragment, new MyMapFragment()).addToBackStack(null).commit();
//        }
        else{
            ft.replace(R.id.details_fragment, new StockFragment()).addToBackStack(null).commit();
        }
    }
}

