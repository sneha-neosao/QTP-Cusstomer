package adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import activities.ProductDetailActivity;
import ModelClass.NewCartModel;
import com.grocery.QTPmart.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FeatureProductAdapter extends RecyclerView.Adapter<FeatureProductAdapter.BannerImage> {


    private Context context;
    private ArrayList<NewCartModel> images;


    public FeatureProductAdapter(Context context, ArrayList<NewCartModel> images) {
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public BannerImage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout_feature_product, parent, false);
        return new BannerImage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerImage holder, int position) {
//        Log.e("tag", "onBindViewHolder: " + images.get(position));
        Picasso.get().load(images.get(position).getProduct_image()).error(R.mipmap.ic_launcher).into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Fragment homepage = new ProductDetailFragment();
                FragmentTransaction fragmentManager =((FragmentActivity)context).getSupportFragmentManager()
                        .beginTransaction();
                Bundle bundle=new Bundle();
                bundle.putString("itemID", images.get(position).getProduct_id());
                bundle.putInt("from", 0); //key and value
                homepage.setArguments(bundle);
                fragmentManager.replace(R.id.nav_supplier_fragment, homepage);
                fragmentManager.addToBackStack(null);
                fragmentManager.commit();*/
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("itemID", images.get(position).getProduct_id());
                intent.putExtra("from", 0);
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
