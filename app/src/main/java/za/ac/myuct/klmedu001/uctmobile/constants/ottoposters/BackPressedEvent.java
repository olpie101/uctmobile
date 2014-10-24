package za.ac.myuct.klmedu001.uctmobile.constants.ottoposters;

import java.util.Date;

/**
 * Created by eduardokolomajr on 2014/10/23.
 */
public class BackPressedEvent {
    public boolean intercepted = false;
    public Date start;

    public BackPressedEvent(){
        start = new Date();
    }
}
