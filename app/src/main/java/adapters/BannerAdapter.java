package adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import activities.BannerItemsActivity;
import fragments.BannerItemsFragment;

import com.grocery.QTPmart.MainActivity;
import com.squareup.picasso.Picasso;
import com.grocery.QTPmart.R;

import java.util.ArrayList;
import java.util.HashMap;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerImage> {


    private Context context;
    private ArrayList<String> images;
    ArrayList<HashMap<String, String>> listarrays ;

    public BannerAdapter(Context context, ArrayList<String> images, ArrayList<HashMap<String, String>> listarray) {
        this.context = context;
        this.images = images;
        this.listarrays = listarray;
    }

    @NonNull
    @Override
    public BannerImage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_layout, parent, false);
        return new BannerImage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerImage holder, int position) {
        Log.e("tag", "onBindViewHolder: " + images.get(position));
        Picasso.get().load(images.get(position)).error(R.mipmap.ic_launcher).into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BannerItemsFragment bannerItemsFragment = new BannerItemsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("banner_id",listarrays.get(position).get("banner_id"));
                bannerItemsFragment.setArguments(bundle);
                ((MainActivity)context).loadFragment(bannerItemsFragment);
                Intent intent = new Intent(context, BannerItemsActivity.class);
                intent.putExtra("banner_id",listarrays.get(position).get("banner_id"));
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        if (images.size() > 0) {
            return images.size();
        } else {
            return 0;
        }

    }

    public static class BannerImage extends RecyclerView.ViewHolder {

        private ImageView img;

        public BannerImage(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.banner_image);
        }
        // each data item is just a string in this case

    }
}
