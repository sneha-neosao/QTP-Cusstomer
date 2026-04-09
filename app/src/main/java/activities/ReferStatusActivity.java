package activities;

import static Config.BaseURL.KEY_ID;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import adapters.ReferEarnStatusAdapter;
import ModelClass.ResultReferEarn;
import com.grocery.QTPmart.R;
import network.Response.ResponseReferEarnStatus;
import network.ServiceGenrator;
import util.GridSpacingItemDecoration;
import util.NetworkConnection;
import util.Session_management;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReferStatusActivity extends AppCompatActivity
{
    String custId="";
    Session_management session_management;
    String signupCount="0";
    String orderCount="0";
    ArrayList<ResultReferEarn> list=new ArrayList<>();


    RecyclerView Rc_signup_orders;
    LinearLayout ll_No_signup,ll_rc_signup;
    ReferEarnStatusAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ImageView img_arrowback;

    TextView txt_zero_signup,txt_order_zero;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_status);

        session_management = new Session_management(this);
        custId = session_management.getUserDetails().get(KEY_ID);
        signupCount=getIntent().getStringExtra("signUpCount");
        orderCount=getIntent().getStringExtra("orderCount");


        Rc_signup_orders = findViewById(R.id.Rc_signup_orders);
        txt_zero_signup = findViewById(R.id.txt_zero_signup);
        txt_order_zero = findViewById(R.id.txt_order_zero);
        img_arrowback = findViewById(R.id.img_arrowback);
        ll_No_signup = findViewById(R.id.ll_No_signup);
        ll_rc_signup = findViewById(R.id.ll_rc_signup);
        adapter = new ReferEarnStatusAdapter( this,list);
        Rc_signup_orders.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 1);
        Rc_signup_orders.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        Rc_signup_orders.setLayoutManager(layoutManager);
        Rc_signup_orders.setAdapter(adapter);

        txt_order_zero.setText(orderCount);
        txt_zero_signup.setText(signupCount);

        if (NetworkConnection.connectionChecking(this)) {
            getReferStatusList();
        }

        img_arrowback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void getReferStatusList() {
        ServiceGenrator.getApiInterface().getReferEarnStatus(custId).enqueue(new Callback<ResponseReferEarnStatus>() {
            @Override
            public void onResponse(Call<ResponseReferEarnStatus> call, Response<ResponseReferEarnStatus> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("true")) {

                        list = response.body().getResult();

                        if(adapter!=null){
                            adapter.setItems(list);
                            adapter.notifyDataSetChanged();
                        }
                    }else {
                        ll_rc_signup.setVisibility(View.GONE);
                        ll_No_signup.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseReferEarnStatus> call, Throwable t) {
                ll_rc_signup.setVisibility(View.GONE);
                ll_No_signup.setVisibility(View.VISIBLE);
            }
        });
    }
}
