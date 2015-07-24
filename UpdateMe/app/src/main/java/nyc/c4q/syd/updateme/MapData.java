package nyc.c4q.syd.updateme;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by July on 7/23/15.
 */
public class MapData {
    private List<LatLng> latLngs;
    private String time;
    private String miles;

    public MapData(List<LatLng> latLgn, String time, String miles) {
        this.latLngs = latLgn;
        this.time = time;
        this.miles = miles;
    }

    public List<LatLng> getLatLngs() {
        return latLngs;
    }

    public void setLatLngs(List<LatLng> latLngs) {
        this.latLngs = latLngs;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMiles() {
        return miles;
    }

    public void setMiles(String miles) {
        this.miles = miles;
    }
}
