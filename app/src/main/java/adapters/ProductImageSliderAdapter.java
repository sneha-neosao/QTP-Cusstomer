package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import ModelClass.ImageModel;
import com.grocery.QTPmart.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductImageSliderAdapter extends SliderViewAdapter<ProductImageSliderAdapter.SliderAdapterVH> {

    Context context;
    ArrayList<ImageModel> imageModels;

    public ProductImageSliderAdapter( Context context,ArrayList<ImageModel> imageModels){
        this.context=context;
        this.imageModels=imageModels;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_image_slider, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {

        Picasso.get().load(imageModels.get(position).getImagePath()).
                error(R.drawable.noimageavailable).into(viewHolder.img_slider_product);


    }

    @Override
    public int getCount() {
        return imageModels.size();
    }

    public static class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
        public ImageView img_slider_product;

        public SliderAdapterVH(View itemView) {
            super(itemView);

            img_slider_product=itemView.findViewById(R.id.img_slider_product);
        }
    }
}
