package ch.hefr.tic.hefr_mylocation.json;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nkcr on 11.06.15.
 */
public class JsonWrap implements Parcelable {
    public double lat;
    public double lng;
    public String name;
    public String food;
    public JsonWrap(double lat, double lng, String name, String food) {
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.food = food;
    }
    public JsonWrap(Parcel in) {
        this.lat = in.readDouble();
        this.lng = in.readDouble();
        this.name = in.readString();
        this.food = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(lat);
        parcel.writeDouble(lng);
        parcel.writeString(name);
        parcel.writeString(food);
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        @Override
        public Object createFromParcel(Parcel parcel) {
            return new JsonWrap(parcel);
        }

        @Override
        public Object[] newArray(int i) {
            return new JsonWrap[i];
        }
    };
}
