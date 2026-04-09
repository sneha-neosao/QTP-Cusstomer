package activities;

import static Config.BaseURL.KEY_ID;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import adapters.FlagAdapter;
import adapters.TermsConditionAdapter;
import ModelClass.Country;
import ModelClass.MyReferDetails;
import ModelClass.ResultTerms;
import com.grocery.QTPmart.R;
import network.Response.ResponseMainPopUp;
import network.Response.ResponseReferEarnDetails;
import network.Response.ResponseTermsCondition;
import network.ServiceGenrator;
import util.CommunicatorFlag;
import util.NetworkConnection;
import util.Session_management;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReferActivity extends AppCompatActivity {

    ArrayList<ResultTerms> termsConditionList=new ArrayList<>();
    String custId="";
    Session_management session_management;

    TextView txt_invitationCode,txt_reDescription,txt_show_offer,txt_signup_count,txt_order_count,txt_terms,txt_viewstatus;
    LinearLayout ll_tapcopy;
    private ClipboardManager myClipboard;
    private ClipData myClip;
    AppCompatButton btnShare;
    String shareMesage="";
    RecyclerView recyclerImages;
    LinearLayoutManager linearLayoutManager;
    TermsConditionAdapter adapter;
    ImageView img_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referearn);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");

        session_management = new Session_management(this);
        custId = session_management.getUserDetails().get(KEY_ID);

        txt_terms=findViewById(R.id.txt_terms);
        txt_invitationCode=findViewById(R.id.txt_invitationCode);
        txt_reDescription=findViewById(R.id.txt_reDescription);
        txt_show_offer=findViewById(R.id.txt_show_offer);
        ll_tapcopy = findViewById(R.id.ll_tapcopy);
        txt_signup_count = findViewById(R.id.txt_signup_count);
        txt_order_count = findViewById(R.id.txt_order_count);
        txt_viewstatus = findViewById(R.id.txt_viewstatus);
        btnShare = findViewById(R.id.btn_share);

        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ll_tapcopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text;
                text = txt_invitationCode.getText().toString();

                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);

                Toast.makeText(getApplicationContext(), "Code Copied",
                        Toast.LENGTH_SHORT).show();
            }
        });
        txt_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackPressDialog();
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareMessage();
            }
        });

        txt_viewstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ReferActivity.this,ReferStatusActivity.class);
                intent.putExtra("signUpCount",txt_signup_count.getText().toString());
                intent.putExtra("orderCount",txt_order_count.getText().toString());
                startActivity(intent);
            }
        });



        if (NetworkConnection.connectionChecking(this)) {
            getMyReferDetails();
            checkAndCreateUserVouchers();
            getTermsNconditions();
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getMyReferDetails() {

        ServiceGenrator.getApiInterface().getMyReferDetails(custId).enqueue(new Callback<ResponseReferEarnDetails>() {
            @Override
            public void onResponse(Call<ResponseReferEarnDetails> call, Response<ResponseReferEarnDetails> response) {
                Log.e("ReferNEarn", response.toString());
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        setUIData(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseReferEarnDetails> call, Throwable t) {
                Log.e("ReferNEarn", t.getMessage());
            }
        });
    }

    private void checkAndCreateUserVouchers() {

        ServiceGenrator.getApiInterface().checkAndCreateUserVouchers(custId).enqueue(new Callback<ResponseReferEarnDetails>() {
            @Override
            public void onResponse(Call<ResponseReferEarnDetails> call, Response<ResponseReferEarnDetails> response) {
                Log.e("ReferNEarnVT", response.toString());
                if (response.isSuccessful()) {
                    getMyReferDetails();
                }
            }

            @Override
            public void onFailure(Call<ResponseReferEarnDetails> call, Throwable t) {
                Log.e("ReferNEarnVF", t.getMessage());
            }
        });
    }

    void setUIData(ResponseReferEarnDetails response)
    {
        MyReferDetails details=response.getResult();
        txt_invitationCode.setText(details.getInvitationCode());
        txt_reDescription.setText(details.getReDescription());

        String offer="Give "+details.getGiveOFFValue()+"% OFF, Get "+details.getGetOFFValue()+"% OFF";
        txt_show_offer.setText(offer);

        txt_signup_count.setText(response.getCounts().getSignUpsCount());
        txt_order_count.setText(response.getCounts().getTotalOrderCount());

        shareMesage=details.getShareMessage();

    }

    void shareMessage()
    {
        shareMesage+="http://play.google.com/store/apps/details?id=" + getPackageName();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareMesage);
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    private void getTermsNconditions() {
        ServiceGenrator.getApiInterface().getReferTermsAndCondition().enqueue(new Callback<ResponseTermsCondition>() {
            @Override
            public void onResponse(Call<ResponseTermsCondition> call, Response<ResponseTermsCondition> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("true")) {
                        termsConditionList = response.body().getResult();
                        if(adapter!=null){
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseTermsCondition> call, Throwable t) {

            }
        });
    }

        private void BackPressDialog() {
            Dialog bottomSheetDialog = new Dialog(ReferActivity.this);
            bottomSheetDialog.setContentView(R.layout.dialog_termscondition_layout);
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.95);
            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);


            recyclerImages = bottomSheetDialog.findViewById(R.id.rv_allterms_conditon);
            img_cancel = bottomSheetDialog.findViewById(R.id.img_cancel);
            img_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                }
            });

            bottomSheetDialog.getWindow().setLayout(width, height);
            bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
            bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            bottomSheetDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            bottomSheetDialog.show();
            adapter = new TermsConditionAdapter(this, termsConditionList);
            linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerImages.setAdapter(adapter);
            recyclerImages.setLayoutManager(linearLayoutManager);
            bottomSheetDialog.show();
        }
    }




