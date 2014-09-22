package za.ac.myuct.klmedu001.uctmobile.constants.ottoposters;

/**
 * Created by eduardokolomajr on 2014/09/22.
 */
public class JammieRouteClickedEvent {
    public final String displayCode;
    public final String code;
    public final int position;

    public JammieRouteClickedEvent(String displayCode, String code, int position) {
        this.displayCode = displayCode;
        this.code = code;
        this.position = position;
    }
}
