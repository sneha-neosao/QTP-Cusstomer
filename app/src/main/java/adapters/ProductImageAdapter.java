package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ModelClass.ImageModel;
import com.grocery.QTPmart.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductImageAdapter extends RecyclerView.Adapter<ProductImageAdapter.MyViewHolder>{

    Context context;
    ArrayList<ImageModel> imageModels;
    ItemOnClickListener itemOnClickListener;

    public ProductImageAdapter(Context context, ArrayList<ImageModel> imageModels, ItemOnClickListener itemOnClickListener){
        this.context=context;
        this.imageModels=imageModels;
        this.itemOnClickListener=itemOnClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout_product_image, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get()
                .load( imageModels.get(position).getImagePath())
                .placeholder(R.drawable.product_1)
                .into(holder.ivProductImage);

        holder.itemView.setOnClickListener(view -> {
            itemOnClickListener.onItemClick(position,imageModels);
        });

    }

    @Override
    public int getItemCount() {
        return imageModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView ivProductImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProductImage=itemView.findViewById(R.id.ivProductImage);

        }
    }

    public interface ItemOnClickListener{
        public void onItemClick(int position,ArrayList<ImageModel> imageModel);
    }
}
