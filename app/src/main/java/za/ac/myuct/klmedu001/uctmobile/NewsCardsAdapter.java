package za.ac.myuct.klmedu001.uctmobile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import za.ac.myuct.klmedu001.uctmobile.constants.BaseApplication;
import za.ac.myuct.klmedu001.uctmobile.processes.rest.entity.NewsItem;
import za.ac.myuct.klmedu001.uctmobile.constants.ottoposters.NewsCardClickedEvent;
import za.ac.myuct.klmedu001.uctmobile.processes.NewsStreamDrawable;

/**
 * Created by eduardokolomajr on 2014/07/26.
 */
public class NewsCardsAdapter extends RecyclerView.Adapter<NewsCardsAdapter.ViewHolder> {
    private static final String TAG = "NewsCardAdapter";
    private ArrayList<NewsItem> items;
    private static WeakReference<Context> context;
    private static int imageHeight;         //image height in pixels
    private static int imageWidth;          //image height in pixels
    private static int imageCornerRadius;   //card corner radius in dips
    private static int imageMargin = 0;         //card margin in dips
    private static int mCornerRadius;       //card corner radius for transformations
    private static int mMargin;             //card margin for transformation
    private Picasso picasso;


    public NewsCardsAdapter(ArrayList<NewsItem> items, Context context){
        this.items = items;
        this.context = new WeakReference<Context>(context);
        imageWidth = context.getResources().getDisplayMetrics().widthPixels;
        imageHeight = context.getResources().getDimensionPixelSize(R.dimen.cardview_height);
        final float density = context.getResources().getDisplayMetrics().density;
        imageCornerRadius = (int)context.getResources().getDimension(R.dimen.cardview_corner_radius);
        Log.d(TAG, "crd="+imageCornerRadius);
        Log.d(TAG, "density="+density);
        mCornerRadius = imageCornerRadius;
        mMargin = imageMargin;


    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_cardview, viewGroup, false);
        ViewCompat.setElevation(v, 10);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.setTitle(items.get(i).getTitle());
        viewHolder.loadPhoto(items.get(i).getPhotoLink());
        viewHolder.position = i;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener{
        @InjectView(R.id.card_view_image)
        ImageView photo;
        @InjectView(R.id.card_view_title)
        TextView title;
        String fileName;
        int position;
        boolean saveBitmap;
        //ensure that target is always found as a class global variable if
        //it is going to be editing a a view such as an image view
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT_WATCH){
                        photo.setImageDrawable(new NewsStreamDrawable(bitmap, mCornerRadius, mMargin));
                }else{
                    photo.setImageBitmap(bitmap);
                }

                if(saveBitmap) {
                    try {
                        File file = new File(context.get().getCacheDir(), fileName);
                        // Use the compress method on the Bitmap object to write image to
                        // the OutputStream
                        FileOutputStream fos = new FileOutputStream(file);

                        // Writing the bitmap to the output stream
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        fos.close();
                        Log.d(TAG + "Target", "image '" + file.getName() + "' saved");
                    } catch (Exception e) {
                        Log.e("saveToInternalStorage()", e.getMessage());
                    }
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

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

        public void loadPhoto(String photoLink) {
            fileName = Uri.parse(photoLink).getLastPathSegment();

            Log.d(TAG, "loading image '"+fileName+"'");
            File file = new File(context.get().getCacheDir(), fileName);
            Log.d(TAG, "file path = "+file.getPath());

            if(file.exists()){
                Log.d(TAG, "loading image '"+fileName+"' from file, "+photoLink);
//                picasso.load(file).resize(imageWidth, imageHeight).into(new ImageTarget(photo, null));
                saveBitmap = false;
                Picasso.with(context.get()).load(file).resize(imageWidth, imageHeight).into(target);
            }else{
                Log.d(TAG, "loading image '"+fileName+"' from network, "+photoLink);
                saveBitmap = true;
                Picasso.with(context.get()).load(photoLink).resize(imageWidth, imageHeight).into(target);
            }
        }

        public Bitmap getPhoto(){
            return ((BitmapDrawable)photo.getDrawable()).getBitmap();
        }

        @Override
        public void onClick(View view) {
            BaseApplication.getEventBus().post(new NewsCardClickedEvent(title.getText().toString()));
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getActionMasked();

            switch (action){
                case MotionEvent.ACTION_DOWN:
                    ViewCompat.setTranslationZ(v, 120);
                    break;
                case MotionEvent.ACTION_UP:
                    ViewCompat.setTranslationZ(v, 10);
                    break;

            }
            return true;
        }
    }

    public void setItems(ArrayList<NewsItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }
}
