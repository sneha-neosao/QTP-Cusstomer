package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ModelClass.ImageModel;
import com.grocery.QTPmart.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FullScreenProductImageSliderAdapter extends SliderViewAdapter<FullScreenProductImageSliderAdapter.SliderAdapterVH1> {

    Context context;
    ArrayList<ImageModel> imageModels;

    public FullScreenProductImageSliderAdapter(Context context, ArrayList<ImageModel> imageModels){
        this.context=context;
        this.imageModels=imageModels;
    }

    @Override
    public SliderAdapterVH1 onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slidingimages_layout, null);
        return new SliderAdapterVH1(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH1 viewHolder, int position) {

        Picasso.get().load(imageModels.get(position).getImagePath()).
                error(R.mipmap.ic_launcher).into(viewHolder.img_slider_product);


    }

    @Override
    public int getCount() {
        return imageModels.size();
    }

    public static class SliderAdapterVH1 extends ViewHolder {
        public ImageView img_slider_product;

        public SliderAdapterVH1(View itemView) {
            super(itemView);

            img_slider_product=itemView.findViewById(R.id.image);
        }
    }
}
