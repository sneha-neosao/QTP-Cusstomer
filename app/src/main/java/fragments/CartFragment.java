package fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.grocery.QTPmart.MainActivity;
import activities.OrderSummary;
import activities.SignUpActivity;
import adapters.Cart_adapter;
import Constants.CheckEmptyCartListener;
import com.grocery.QTPmart.R;
import util.DatabaseHandler;
import util.Session_management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartFragment extends Fragment implements CheckEmptyCartListener {

    Button btn_ShopNOw;
    RecyclerView recyclerView;
    LinearLayout ll_Checkout;
    RelativeLayout noData,viewCart;
    TextView totalItems;
    public static TextView tv_total ;
    private DatabaseHandler db;
    private Session_management sessionManagement;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerView=view.findViewById(R.id.recyclerCart);
        btn_ShopNOw=view.findViewById(R.id.btn_ShopNOw);
        viewCart=view.findViewById(R.id.viewCartItems);
        tv_total=view.findViewById(R.id.txt_totalamount);
        totalItems=view.findViewById(R.id.txt_totalQuan);
        noData=view.findViewById(R.id.noData);
        sessionManagement = new Session_management(getActivity());
        sessionManagement.cleardatetime();
        db = new DatabaseHandler(getActivity());

        ll_Checkout=view.findViewById(R.id.ll_Checkout);
        btn_ShopNOw.setOnClickListener(v -> {
            Intent intent=new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });

        ll_Checkout.setOnClickListener(v -> {
            if (isOnline()) {
                if (sessionManagement.isLoggedIn()) {
                    if (sessionManagement.userBlockStatus().equalsIgnoreCase("2")){
                        if (db.getCartCount() == 0) {
                            noData.setVisibility(View.VISIBLE);
                            viewCart.setVisibility(View.GONE);
                        } else {
                            Intent intent = new Intent(getActivity(), OrderSummary.class);
                            startActivityForResult(intent,22);
                        }
                    }else {
                        showBloackDialog();
                    }
                } else {

                    Intent intent = new Intent(getActivity(), SignUpActivity.class);
                    intent.putExtra("return","Order");
                    startActivity(intent);
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));


        db = new DatabaseHandler(getActivity());

        if (sessionManagement.isLoggedIn()) {

            if (db.getCartCount() == 0) {
                noData.setVisibility(View.VISIBLE);
                viewCart.setVisibility(View.GONE);
            }
        }else {
            if (db.getCartCount() == 0) {
                noData.setVisibility(View.VISIBLE);
                viewCart.setVisibility(View.GONE);
            }
        }
        ArrayList<HashMap<String, String>> map = db.getCartAll();

        Cart_adapter adapter = new Cart_adapter(getActivity(),this, map, () -> {
            if (db.getCartCount() == 0) {
                noData.setVisibility(View.VISIBLE);
                viewCart.setVisibility(View.GONE);
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        updateData();
        return view;

    }



    public void updateData() {

        tv_total.setText(sessionManagement.getCurrency() + " " + db.getTotalAmount() + " | ");
        totalItems.setText(" Total Items : " + db.getCartCount() + " | ");

    }

    private void showBloackDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setCancelable(true);
        alertDialog.setMessage("You are blocked from backend.\n Please Contact with customer care!");
//        alertDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        });
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @Override
    public void onCartChange() {
        if (db.getCartCount() == 0) {
            noData.setVisibility(View.VISIBLE);
            viewCart.setVisibility(View.GONE);
        }
        updateData();
    }


}
