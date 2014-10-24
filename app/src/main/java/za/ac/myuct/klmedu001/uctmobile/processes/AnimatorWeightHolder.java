package za.ac.myuct.klmedu001.uctmobile.processes;

import android.view.View;

import za.ac.myuct.klmedu001.uctmobile.constants.UCTConstants;

/**
 * Created by eduardokolomajr on 2014/10/23.
 * Holds View and endWeight for given view.
 */
public class AnimatorWeightHolder {
    public final View view;
    public final float endWeight;

    public AnimatorWeightHolder(View view, float endWeight) {
        this.view = view;
        this.endWeight = endWeight;
    }
}
