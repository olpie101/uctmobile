package za.ac.myuct.klmedu001.uctmobile;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import za.ac.myuct.klmedu001.uctmobile.constants.BaseApplication;
import za.ac.myuct.klmedu001.uctmobile.constants.ottoposters.JammieDaysClickedEvent;

import static za.ac.myuct.klmedu001.uctmobile.constants.UCTConstants.convertDay;

/**
 * Created by eduardokolomajr on 2014/09/23.
 */
public class JammieDaysAdapter extends RecyclerView.Adapter<JammieDaysAdapter.ViewHolder> {
    char [] days;

    public JammieDaysAdapter(char [] days) {
        this.days = days;
    }

    @Override
    public JammieDaysAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.textview_jammie_list_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(JammieDaysAdapter.ViewHolder viewHolder, int i) {
        viewHolder.day.setText(convertDay(days[i]));
        viewHolder.dayChar = days[i];
    }

    @Override
    public int getItemCount() {
        return days.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @InjectView(android.R.id.text1)
        TextView day;
        char dayChar;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            BaseApplication.getEventBus().post(new JammieDaysClickedEvent(dayChar));
        }
    }
}
