package adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import Config.ApiBaseURL;
import ModelClass.HomeCate;
import com.grocery.QTPmart.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class SubCategory_adapter extends RecyclerView.Adapter<SubCategory_adapter.MyViewHolder> {

    private List<HomeCate> modelList;
    private Context context;
    String language;
    RecyclerView recyclerView;
    SharedPreferences preferences;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;
        RelativeLayout linearLayout ;
        CardView cardview1;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_home_title);
            image = (ImageView) view.findViewById(R.id.iv_home_img);
            linearLayout =  view.findViewById(R.id.ll1);
            cardview1 =  view.findViewById(R.id.cardview1);
        }
    }

    public SubCategory_adapter(List<HomeCate> modelList,RecyclerView recyclerViews) {
        this.modelList = modelList;
        this.recyclerView = recyclerViews;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_subcategory_rv, parent, false);

        context = parent.getContext();

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        HomeCate mList = modelList.get(position);

        Random rnd = new Random();
        //  int currentColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));  //bright colors
        final int baseColor = Color.WHITE;

        final int baseRed = Color.red(baseColor);
        final int baseGreen = Color.green(baseColor);
        final int baseBlue = Color.blue(baseColor);

        final int red = (baseRed + rnd.nextInt(256)) / 2;
        final int green = (baseGreen + rnd.nextInt(256)) / 2;
        final int blue = (baseBlue + rnd.nextInt(256)) / 2;
        int clr1 = Color.rgb(red, green, blue);                                 //pastel colors
        holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.home_bg));

        Picasso.get()
                .load(ApiBaseURL.IMG_URL + mList.getImages())
                .into(holder.image);
        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        language=preferences.getString("language","");
            holder.title.setText(mList.getName());

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}
