package za.ac.myuct.klmedu001.uctmobile.processes.rest.container;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import za.ac.myuct.klmedu001.uctmobile.api.endpoints.jammieEndpoint.model.AllRoutes;

/**
 * Created by eduardokolomajr on 2014/09/19.
 */
@Table(name = "allRoutes")
public class AllRoutesContainer extends Model implements Parcelable {
    @Column private String route;
    @Column private String displayCode;

    @SuppressWarnings("unused")
    public AllRoutesContainer(){super();}

    public AllRoutesContainer(AllRoutes in){
        super();
        this.route = in.getRoute();
        this.displayCode = in.getDisplayCode();
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getDisplayCode() {
        return displayCode;
    }

    public void setDisplayCode(String displayCode) {
        this.displayCode = displayCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.route);
        dest.writeString(this.displayCode);
    }

    private AllRoutesContainer(Parcel in) {
        this.route = in.readString();
        this.displayCode = in.readString();
    }

    public static final Parcelable.Creator<AllRoutesContainer> CREATOR = new Parcelable.Creator<AllRoutesContainer>() {
        public AllRoutesContainer createFromParcel(Parcel source) {
            return new AllRoutesContainer(source);
        }

        public AllRoutesContainer[] newArray(int size) {
            return new AllRoutesContainer[size];
        }
    };
}
