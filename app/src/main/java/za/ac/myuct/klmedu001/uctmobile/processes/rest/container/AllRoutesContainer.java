package za.ac.myuct.klmedu001.uctmobile.processes.rest.container;

import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import za.ac.myuct.klmedu001.uctmobile.api.endpoints.jammieEndpoint.model.AllRoutes;
import za.ac.myuct.klmedu001.uctmobile.constants.AppDatabase;

/**
 * Created by eduardokolomajr on 2014/09/19.
 */
@Table(databaseName = AppDatabase.NAME, value = AppDatabase.TABLE_ALL_ROUTES)
public class AllRoutesContainer extends BaseModel implements Parcelable {
    @Column(columnType = Column.PRIMARY_KEY_AUTO_INCREMENT) long _id;
    @Column String bracket;    // Periods it is available (term, vac, etc)
    @Column String route;
    @Column String displayCode;

    @SuppressWarnings("unused")
    public AllRoutesContainer(){}

    public AllRoutesContainer(AllRoutes in){
        super();
        this.route = in.getRoute();
        this.displayCode = in.getDisplayCode();
        this.bracket = in.getAvailability();
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

    public String getBracket() { return bracket; }

    public void setBracket(String bracket) { this.bracket = bracket; }

    @Override
    public int describeContents() {
        return 3;
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
