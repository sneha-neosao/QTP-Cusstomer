package adapters;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.grocery.QTPmart.R;
import util.DatabaseHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class ImageAdapterData extends RecyclerView.Adapter<ImageAdapterData.ProductHolder> {
    ArrayList<HashMap<String, String>> list;
    Activity activity;
    String price_tx;
    SharedPreferences preferences;
    String language;

    int lastpostion;
    DatabaseHandler dbHandler;

    public ImageAdapterData(Activity activity, ArrayList<HashMap<String, String>> list) {
        this.list = list;
        this.activity = activity;

        dbHandler = new DatabaseHandler(activity);
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.imageadapter, parent, false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, final int position) {

        final HashMap<String, String> map = list.get(position);
        Picasso.get()
                .load(map.get("product_image"))
                .placeholder(R.drawable.noimageavailable)
                .into(holder.image_data);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {

        public ImageView image_data;


        public ProductHolder(View view) {
            super(view);


            image_data = (ImageView) view.findViewById(R.id.image_data);


        }
    }



}

