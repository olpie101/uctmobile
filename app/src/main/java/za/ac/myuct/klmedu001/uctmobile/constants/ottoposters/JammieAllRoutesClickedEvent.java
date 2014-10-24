package za.ac.myuct.klmedu001.uctmobile.constants.ottoposters;

/**
 * Created by eduardokolomajr on 2014/09/21.
 */
public class JammieAllRoutesClickedEvent {
    public final CharSequence name;
    public final String displayCode;
    public final int position;

    public JammieAllRoutesClickedEvent(CharSequence name, String displayCode, int position) {
        this.name = name;
        this.displayCode = displayCode;
        this.position = position;
    }
}
