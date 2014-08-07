package za.ac.myuct.klmedu001.uctmobile.constantsandprocesses;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;


/**
 * Created by eduardokolomajr on 2014/07/28.
 * Database ORM used can be found at
 * https://github.com/pardom/ActiveAndroid.git
 */
public class BaseApplication extends Application {

    private static Bus mEventBus;

    public static Bus getEventBus() {
        return mEventBus;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mEventBus = new Bus(ThreadEnforcer.ANY);
        ActiveAndroid.initialize(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }
}
