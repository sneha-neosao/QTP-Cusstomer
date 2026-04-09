package activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import adapters.FullScreenProductImageSliderAdapter;
import adapters.ProductImageAdapter;
import ModelClass.ImageModel;
import com.grocery.QTPmart.R;
import util.CommonFunctions;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class ViewProductImageActivity extends AppCompatActivity implements ProductImageAdapter.ItemOnClickListener,SharedPreferences.OnSharedPreferenceChangeListener {
    private ImageView img_product_main;
    String imgPath;
    ArrayList<ImageModel> imageModels = new ArrayList<>();

    RecyclerView rvProductImage;
    ProductImageAdapter productImageAdapter;

    TextView cartCount,tvCurrentPage;
    RelativeLayout rlCart;
    ImageView search;
    LinearLayout llBack;
    private SharedPreferences pref;
    SliderView slider_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product_image);

        pref = getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
        pref.registerOnSharedPreferenceChangeListener(this);

        img_product_main=findViewById(R.id.img_product_main);
        rvProductImage=findViewById(R.id.rvProductImage);
        rlCart = findViewById(R.id.rlCart);
        slider_view = findViewById(R.id.slider_view);
        tvCurrentPage = findViewById(R.id.tvCurrentPage);

        cartCount = findViewById(R.id.cartCount);

        search = findViewById(R.id.search);
        llBack = findViewById(R.id.llBack);

        imgPath=getIntent().getStringExtra("imgPath");
        imageModels = (ArrayList<ImageModel>)getIntent().getSerializableExtra("imageList");

        Log.e("imageLis",imageModels.toString());

        ImageView img_logo = findViewById(R.id.img_logo);

        img_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonFunctions.callMainDrawerActivity(ViewProductImageActivity.this);
            }
        });

        initBadges();

        Glide.with(this).load(imgPath).into(img_product_main);

        if(!imageModels.isEmpty()){
            productImageAdapter = new ProductImageAdapter(this,imageModels, ViewProductImageActivity.this);
            rvProductImage.setAdapter(productImageAdapter);

            FullScreenProductImageSliderAdapter adapter = new FullScreenProductImageSliderAdapter(ViewProductImageActivity.this, imageModels);
            slider_view.setSliderAdapter(adapter);
            slider_view.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
            slider_view.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);

        }
        tvCurrentPage.setText(slider_view.getCurrentPagePosition()+" of "+imageModels.size());


        rlCart.setOnClickListener(view -> {
            startActivity(new Intent(this, CartActivity.class));
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // startActivity(new Intent(ViewProductImageActivity.this, SearchActivity.class));
                startActivity(new Intent(ViewProductImageActivity.this,NewSeearchActivity.class)
                        .putExtra("fromIntent",0));
            }
        });

        llBack.setOnClickListener(view -> {
            finish();
        });
    }

    private void initBadges() {

        int badgeCount = pref.getInt("cardqnty", 0);
        if (badgeCount > 0) {
            cartCount.setText("" + badgeCount);
            cartCount.setVisibility(View.VISIBLE);
        } else {
            cartCount.setVisibility(View.GONE);
        }

    }

    @Override
    public void onItemClick(int position, ArrayList<ImageModel> imageModel) {
        Glide.with(this).load(imageModel.get(position).getImagePath()).into(img_product_main);
        slider_view.setCurrentPagePosition(position);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equalsIgnoreCase("cardqnty")) {

            int badgeCount = pref.getInt("cardqnty", 0);
            if (badgeCount > 0) {
                cartCount.setText("" + badgeCount);
                cartCount.setVisibility(View.VISIBLE);
            } else {
                cartCount.setVisibility(View.GONE);
            }
        }
    }
}