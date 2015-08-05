package nyc.c4q.syd.updateme;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by July on 7/18/15.
 */
public class JobFragment extends Fragment {
    //eliminate the possibility of toast to appear twice on both sides of the card when there is no job match
    public static int showToast = 3;

    private boolean showingBack;
    private Fragment front;
    private Fragment back;
    private Handler handler;
    private FlipAnimation flipAnimation;
    private FlipAnimation backFlip;
    private TextView header;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.job_activity, container, false);
        header = (TextView) view.findViewById(R.id.header);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //create handler for animation control
        handler = new Handler(getActivity().getMainLooper());

        //create two fragments
        front = new FrontFragment();
        back = new BackFragment();
        getChildFragmentManager().beginTransaction().add(R.id.fragment_container, back, "fragmentRight").commit();
        getChildFragmentManager().beginTransaction().add(R.id.fragment_container, front, "fragmentLeft").commit();


        view.findViewById(R.id.settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BackFragment right = (BackFragment) getChildFragmentManager().findFragmentByTag("fragmentRight");
                FrontFragment left = (FrontFragment) getChildFragmentManager().findFragmentByTag("fragmentLeft");

                //get user input from the settings section
                String userInput = right.getPosition() + "&location=" + right.getLocation();
                left.fetchData(userInput);
                changeHeaderText();
                //show toast only on one side of the card
                showToast+=1;

                flipAnimation = new FlipAnimation(left.getView(), right.getView());
                backFlip = new FlipAnimation(left.getView(), right.getView());
                handler.removeCallbacks(rotate);
                handler.postDelayed(rotate, 260);
            }

        });
    }

    private Runnable rotate = new Runnable() {

        @Override
        public void run() {
            if (!showingBack) {
                front.getView().startAnimation(flipAnimation);
                back.getView().startAnimation(flipAnimation);
                showingBack = true;
            } else {
                showingBack = false;
                backFlip.reverse();
                front.getView().startAnimation(backFlip);
                back.getView().startAnimation(backFlip);

            }
        }
    };

    public void changeHeaderText() {
        if(showToast%2==0) {
            header.setText("Full Jobs List");
        }
        else {
            header.setText("Modify Search");
        }
    }
}
