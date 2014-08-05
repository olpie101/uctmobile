package za.ac.myuct.klmedu001.uctmobile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import za.ac.myuct.klmedu001.uctmobile.constantsandprocesses.BaseApplication;
import za.ac.myuct.klmedu001.uctmobile.constantsandprocesses.NewsItem;
import za.ac.myuct.klmedu001.uctmobile.constantsandprocesses.ottoposters.NewsCardClickedEvent;
import za.ac.myuct.klmedu001.uctmobile.otherconstantsandprocess.NewsStreamDrawable;

/**
 * Created by eduardokolomajr on 2014/07/26.
 */
public class NewsCardsAdapter extends RecyclerView.Adapter<NewsCardsAdapter.ViewHolder> {
    private final String TAG = "NewsCardAdapter";
    private ArrayList<NewsItem> items;
    private WeakReference<Context> context;
    private float densityMultiplier;
    private static int imageHeight;         //image height in pixels
    private static int imageWidth;          //image height in pixels
    private static int imageCornerRadius;   //card corner radius in dips
    private static int imageMargin = 0;         //card margin in dips
    private static int mCornerRadius;       //card corner radius for transformations
    private static int mMargin;             //card margin for transformation
    private static Picasso picasso;


    public NewsCardsAdapter(ArrayList<NewsItem> items, Context context){
        this.items = items;
        this.context = new WeakReference<Context>(context);
        imageWidth = context.getResources().getDisplayMetrics().widthPixels-context.getResources().getDimensionPixelSize(R.dimen.cardview_padding);
        imageHeight = context.getResources().getDimensionPixelSize(R.dimen.cardview_height);
        final float density = context.getResources().getDisplayMetrics().density;
        imageCornerRadius = (int)(context.getResources().getDimension(R.dimen.cardview_corner_radius)/density);
        Log.d(TAG, "crd="+imageCornerRadius);
        Log.d(TAG, "density="+density);
        mCornerRadius = (int) (imageCornerRadius * density + 0.5f);
        mMargin = (int) (imageMargin * density + 0.5f);

        picasso = new Picasso.Builder(this.context.get()).downloader(new OkHttpDownloader(new OkHttpClient())).indicatorsEnabled(true).loggingEnabled(true).build();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_cardview, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.setTitle(items.get(i).getTitle());
        viewHolder.loadPhoto(items.get(i).getPhotoLink(), context.get());
        viewHolder.position = i;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @InjectView(R.id.card_view_image)
        ImageView photo;
        @InjectView(R.id.card_view_title)
        TextView title;
        int position;
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                if(Build.VERSION.SDK_INT < 21){
//                    Log.d("NewsCardAdapter", "iw="+imageWidth+", ih="+imageHeight);
//                    Log.d("NewsCardAdapter", "aiw="+bitmap.getWidth()+", aih="+bitmap.getHeight());
                    photo.setImageDrawable(new NewsStreamDrawable(bitmap, mCornerRadius, mMargin));
//                    photo.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                //TODO NewsCardAdapter implement onBitmapFailed
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                //TODO NewsCardAdapter implement onPrepareLoad
            }
        };
        public ViewHolder (View itemView){
            super(itemView);
            ButterKnife.inject(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void setTitle(String text){
            title.setText(text);
        }

        public void loadPhoto(String photoLink, Context context) {
            picasso.load(photoLink).resize(imageWidth, imageHeight).into(target);
        }

        public Bitmap getPhoto(){
            return ((BitmapDrawable)photo.getDrawable()).getBitmap();
        }

        @Override
        public void onClick(View view) {
            BaseApplication.getEventBus().post(new NewsCardClickedEvent(getPosition()));
        }
    }

    public void setItems(ArrayList<NewsItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }
}
