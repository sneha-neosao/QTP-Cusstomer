package activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.grocery.QTPmart.R;
import util.Session_management;

public class RedeemVoucherActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{
    ImageView search;
    TextView cartCount;

    private SharedPreferences pref;
    Session_management session_management;

    TextView tvAmount,tvValidity,tvVoucherDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reddem_voucher);

        pref = getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
        pref.registerOnSharedPreferenceChangeListener(this);

        session_management = new Session_management(RedeemVoucherActivity.this);

        /*Initialize components*/
        tvAmount=findViewById(R.id.tvAmount);
        tvValidity=findViewById(R.id.tvValidity);
        tvVoucherDescription=findViewById(R.id.tvVoucherDescription);
        search = findViewById(R.id.search);
        cartCount = findViewById(R.id.cartCount);
        initBadges();

        /*Amount and expire date from Voucher fragment*/
        String amount = getIntent().getStringExtra("redeemAmount");
        String expireDate = getIntent().getStringExtra("expireDate");
        String description = getIntent().getStringExtra("description");
        String voucharType = getIntent().getStringExtra("voucharType");
        String discountValue = getIntent().getStringExtra("discountValue");

        /*Set Amount*/
        if(voucharType.equals("Voucher")) {
            tvAmount.setText("AED " + amount);
        }
        else
        {
            tvAmount.setText( discountValue+"%");
        }
        /*Set Validity*/
        tvValidity.setText("Valid until "+expireDate);

        /*Set Description*/
        tvVoucherDescription.setText(description);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(RedeemVoucherActivity.this,NewSeearchActivity.class).putExtra("fromIntent",0));
            }
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

    public void onClickBack(View view) {
        onBackPressed();
    }

    public void onClickCart(View view) {
        startActivity(new Intent(this, CartActivity.class));
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