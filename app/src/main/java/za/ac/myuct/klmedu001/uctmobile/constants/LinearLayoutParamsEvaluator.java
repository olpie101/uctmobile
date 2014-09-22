package za.ac.myuct.klmedu001.uctmobile.constants;

import android.animation.TypeEvaluator;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by eduardokolomajr on 2014/09/22.
 */
public class LinearLayoutParamsEvaluator implements TypeEvaluator<ViewGroup.LayoutParams> {
    @Override
    public ViewGroup.LayoutParams evaluate(float v, ViewGroup.LayoutParams start, ViewGroup.LayoutParams end) {

        ((LinearLayout.LayoutParams)start).weight = ((LinearLayout.LayoutParams)start).weight
                + v*(((LinearLayout.LayoutParams)end).weight-((LinearLayout.LayoutParams)start).weight);
        return start;
    }
}
