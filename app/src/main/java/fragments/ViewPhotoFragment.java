package fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import adapters.ProductImageAdapter;
import ModelClass.ImageModel;
import com.grocery.QTPmart.R;

import java.util.ArrayList;

public class ViewPhotoFragment extends Fragment implements ProductImageAdapter.ItemOnClickListener {

    private ImageView img_product_main;
    String imgPath;
    ArrayList<ImageModel> imageModels = new ArrayList<>();

    RecyclerView rvProductImage;
    ProductImageAdapter productImageAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_view_photo, container, false);
        img_product_main=view.findViewById(R.id.img_product_main);
        rvProductImage=view.findViewById(R.id.rvProductImage);

        imgPath=getArguments().getString("imgPath");
        imageModels = (ArrayList<ImageModel>)getArguments().getSerializable("imageList");

        Log.e("imageLis",imageModels.toString());


        Glide.with(getContext()).load(imgPath).into(img_product_main);

        if(!imageModels.isEmpty()){
            productImageAdapter = new ProductImageAdapter(container.getContext(),imageModels,ViewPhotoFragment.this);
            rvProductImage.setAdapter(productImageAdapter);
        }


       /*view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    return true;
                }
                return false;
            }
        } );*/

        return view;
    }

    @Override
    public void onItemClick(int position, ArrayList<ImageModel> imageModel) {
        Glide.with(getContext()).load(imageModel.get(position).getImagePath()).into(img_product_main);
    }
}