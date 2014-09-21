package za.ac.myuct.klmedu001.uctmobile;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import za.ac.myuct.klmedu001.uctmobile.constants.BaseApplication;
import za.ac.myuct.klmedu001.uctmobile.constants.ottoposters.JammieAllRoutesClickedEvent;
import za.ac.myuct.klmedu001.uctmobile.processes.rest.container.AllRoutesContainer;

/**
 * Created by eduardokolomajr on 2014/09/21.
 */
public class JammieAllRoutesAdapter extends RecyclerView.Adapter<JammieAllRoutesAdapter.ViewHolder>{
    private List<AllRoutesContainer> items;

    public JammieAllRoutesAdapter(List<AllRoutesContainer> items) {
        this.items = items;
    }

    @Override
    public JammieAllRoutesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.textview_jammie_list_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(JammieAllRoutesAdapter.ViewHolder viewHolder, int i) {
        viewHolder.routeName.setText(items.get(i).getRoute());
        viewHolder.displayCode = items.get(i).getDisplayCode();
    }

    @Override
    public int getItemCount() {
        return items.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @InjectView(android.R.id.text1)
        TextView routeName;
        String displayCode;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            BaseApplication.getEventBus().post(new JammieAllRoutesClickedEvent(displayCode));
        }
    }
}
