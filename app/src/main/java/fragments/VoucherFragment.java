package fragments;

import static android.view.View.VISIBLE;
import static Config.BaseURL.KEY_ID;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ServiceCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import activities.MainDrawerActivity;
import activities.RedeemVoucherActivity;
import adapters.VoucherAdapter;
import ModelClass.NotificationModel;
import com.grocery.QTPmart.R;
import network.Response.ResNotification;
import network.Response.ResponseGetUserVouchers;
import network.ServiceGenrator;
import util.NetworkConnection;
import util.Session_management;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VoucherFragment extends Fragment implements VoucherAdapter.ItemOnClickListener{

    RecyclerView rvVoucher;
    TextView txt_no_data;

    VoucherAdapter voucherAdapter;

    ArrayList<ResponseGetUserVouchers.VoucherResult> voucherResultArrayList;

    private Session_management session_management;

    ProgressDialog progressDialog;

    String custId="42";

    LinearLayout llVoucher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_voucher, container, false);
        session_management = new Session_management(getContext());

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait while loading..");

        txt_no_data=view.findViewById(R.id.txt_no_data);
        rvVoucher=view.findViewById(R.id.rvVoucher);
        llVoucher=view.findViewById(R.id.llVoucher);

        MainDrawerActivity.tvTitle.setVisibility(View.GONE);
        MainDrawerActivity.reelLyt.setVisibility(VISIBLE);
        MainDrawerActivity.notification_iv.setVisibility(VISIBLE);
        MainDrawerActivity.search_iv.setVisibility(VISIBLE);
        MainDrawerActivity.ll_nav_title.setVisibility(View.GONE);

        custId = session_management.getUserDetails().get(KEY_ID);

        if(NetworkConnection.connectionChecking(getActivity())){
            getUserVoucher(custId);
        }else{
            Toast.makeText(getActivity(),getActivity().getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
        }

        /*Nested Scroll */
        NestedScrollView scroller = view.findViewById(R.id.nsVoucher);
        if (scroller != null) {
            scroller.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    if (scrollY > oldScrollY) {
                        Log.i("TAG", "Scroll DOWN");
                        MainDrawerActivity.bottomNavigation.setVisibility(View.GONE);
                    }
                    if (scrollY < oldScrollY) {
                        Log.i("TAG", "Scroll UP");
                        MainDrawerActivity.bottomNavigation.setVisibility(View.VISIBLE);
                    }

                    if (scrollY == 0) {

                    }

                    if (scrollY == ( v.getMeasuredHeight() - v.getChildAt(0).getMeasuredHeight() )) {
                        Log.i("TAG", "BOTTOM SCROLL");
                    }
                }
            });
        }


        return view;
    }


    public void loadFragment(Fragment fragment,String amount,String expireDate) {
        Bundle bundle = new Bundle();
        bundle.putString("redeemAmount", amount);
        bundle.putString("expireDate", expireDate);
        fragment.setArguments(bundle);
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.nav_supplier_fragment,fragment,fragment.getTag()).commit();
    }



    private void getUserVoucher(String custId)
    {
        progressDialog.show();
        ServiceGenrator.getApiInterface().getUserVouchers(custId).enqueue(new Callback<ResponseGetUserVouchers>() {
            @Override
            public void onResponse(Call<ResponseGetUserVouchers> call, Response<ResponseGetUserVouchers> response) {
                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    if(response.body().isStatus()){
                        if(response.body().getVoucherResult()!=null){
                            MainDrawerActivity.tvVoucherCount.setText(String.valueOf(response.body().getVoucherResult().size()));
                            llVoucher.setVisibility(View.VISIBLE);
                            txt_no_data.setVisibility(View.GONE);
                            voucherResultArrayList = response.body().getVoucherResult();
                            rvVoucher.setLayoutManager(new LinearLayoutManager(getActivity()));
                            voucherAdapter = new VoucherAdapter(getActivity(),voucherResultArrayList,VoucherFragment.this);
                            rvVoucher.setAdapter(voucherAdapter);
                        }else{
                            txt_no_data.setVisibility(View.VISIBLE);
                            llVoucher.setVisibility(View.GONE);
                            Toast.makeText(getActivity(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        txt_no_data.setVisibility(View.VISIBLE);
                        llVoucher.setVisibility(View.GONE);
                        //Toast.makeText(getActivity(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(),response.message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseGetUserVouchers> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onItemClick(int position, ArrayList<ResponseGetUserVouchers.VoucherResult> voucherList) {
        session_management.setCouponCode(voucherList.get(position).getCmCode(),voucherList.get(position).getCouponType());
        //loadFragment(new ReedemVoucherFragment(),voucherList.get(position).getMinimumPurchaseAmount(),voucherList.get(position).getEndDate());
        startActivity(new Intent(getActivity(), RedeemVoucherActivity.class)
                .putExtra("redeemAmount",voucherList.get(position).getReturnAmount())
                .putExtra("description",voucherList.get(position).getCmDescription())
                .putExtra("expireDate",voucherList.get(position).getEndDate())
                .putExtra("voucharType",voucherList.get(position).getCouponType())
                .putExtra("discountValue",voucherList.get(position).getDiscountValue()));
    }
}