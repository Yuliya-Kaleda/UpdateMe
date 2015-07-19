package nyc.c4q.syd.updateme;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by July on 7/18/15.
 */
public class Utils {
    public static boolean isTablet(Context ctx){

        DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        float density = ctx.getResources().getDisplayMetrics().density;
        float px = 480 * density;
        if (width<px){
            return false;
        }

        return true;
    }
}
