package adapters.ViewHolders;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.grocery.QTPmart.R;
import util.DatabaseHandler;
import util.Session_management;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rajesh Dabhi on 26/6/2017.
 */

public class ImageAdapterData1 extends RecyclerView.Adapter<ImageAdapterData1.ProductHolder> {
    ArrayList<HashMap<String, String>> list;
    Activity activity;
    String price_tx;
    SharedPreferences preferences;
    String language;

    int lastpostion;
    DatabaseHandler dbHandler;

    private Session_management session_management;

    public ImageAdapterData1(Activity activity, ArrayList<HashMap<String, String>> list) {
        this.list = list;
        this.activity = activity;

        dbHandler = new DatabaseHandler(activity);
        session_management = new Session_management(activity);
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bucket_place_order, parent, false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, final int position) {


        final HashMap<String, String> map = list.get(position);

        holder.currency_indicator.setText(session_management.getCurrency());

        Picasso.get()
                .load(map.get("product_image"))
                .placeholder(R.drawable.noimageavailable)
                .into(holder.prodImage);

        holder.txt_pName.setText(map.get("product_name"));
        double sprice = 0;
        if (map.get("price") != null) {
            sprice = Double.parseDouble(map.get("price"));
        }
        int qtyd = Integer.parseInt(dbHandler.getInCartItemQtys(map.get("varient_id")));
        String p = String.format("%.2f", (sprice * qtyd));
        holder.pPrice1.setText(p.substring(0, p.length() - 3));
        holder.pPrice2.setText(p.substring(p.length() - 3));

        holder.unitvalue.setText(qtyd + " " + map.get("unit_value"));
        /*if (map.get("stock") != null && map.get("stock").equals("Stock")) {
            holder.cvItem.setVisibility(View.VISIBLE);

        holder.currency_indicator.setText(session_management.getCurrency());

        Picasso.get()
                .load(map.get("product_image"))
                .placeholder(R.drawable.noimageavailable)
                .into(holder.prodImage);

        holder.txt_pName.setText(map.get("product_name"));
        double sprice = 0;
        if (map.get("price") != null) {
            sprice = Double.parseDouble(map.get("price"));
        }
        int qtyd = Integer.parseInt(dbHandler.getInCartItemQtys(map.get("varient_id")));
        String p = String.format("%.2f", (sprice * qtyd));
        holder.pPrice1.setText(p.substring(0, p.length() - 3));
        holder.pPrice2.setText(p.substring(p.length() - 3));

        holder.unitvalue.setText(qtyd + " " + map.get("unit_value"));
    }else{
            holder.cvItem.setVisibility(View.GONE);
        }*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {

        public ImageView prodImage;
        public TextView  txt_pName,txt_pInfo,txt_unitvalue,txt_Pprice,pPrice1, pPrice2,unitvalue,currency_indicator;
        public CardView cvItem;


        public ProductHolder(View view) {
            super(view);

            currency_indicator = view.findViewById(R.id.currency_indicator);
            prodImage = (ImageView) view.findViewById(R.id.prodImage);
            txt_pName = (TextView) view.findViewById(R.id.txt_pName);
            txt_pInfo = (TextView) view.findViewById(R.id.txt_pInfo);
            unitvalue = (TextView) view.findViewById(R.id.txt_unitvalue);
            txt_Pprice = (TextView) view.findViewById(R.id.txt_Pprice);
            pPrice1 = view.findViewById(R.id.txt_Pprice1);
            pPrice2 = view.findViewById(R.id.txt_Pprice2);
            cvItem = view.findViewById(R.id.cvItem);
        }
    }



}

