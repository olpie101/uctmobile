package za.ac.myuct.klmedu001.uctmobile;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.plus.model.people.Person;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import za.ac.myuct.klmedu001.uctmobile.constants.BaseApplication;
import za.ac.myuct.klmedu001.uctmobile.constants.ottoposters.JammieAllRoutesClickedEvent;
import za.ac.myuct.klmedu001.uctmobile.constants.ottoposters.JammieRouteClickedEvent;
import za.ac.myuct.klmedu001.uctmobile.processes.rest.container.RouteContainer;

/**
 * Created by eduardokolomajr on 2014/09/22.
 * Adapter for Routes
 */
public class JammieRouteAdapter extends RecyclerView.Adapter<JammieRouteAdapter.ViewHolder> {
    private List<RouteContainer> route;

    public JammieRouteAdapter(List<RouteContainer> route) {
        this.route = route;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.textview_jammie_list_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.route.setText(route.get(i).getName());
        viewHolder.displayCode = route.get(i).getDisplayCode();
        viewHolder.code = route.get(i).getCode();
        viewHolder.position = i;
    }

    @Override
    public int getItemCount() {
        return route.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @InjectView(android.R.id.text1)
        TextView route;
        String displayCode;
        String code;
        int position;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            BaseApplication.getEventBus().post(new JammieRouteClickedEvent(displayCode, code, position));
        }
    }
}
