package activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.grocery.QTPmart.MainActivity;
import com.grocery.QTPmart.R;
import util.DatabaseHandler;
import util.Session_management;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static Config.BaseURL.KEY_EMAIL;
import static Config.BaseURL.KEY_ID;
import static Config.BaseURL.KEY_MOBILE;
import static Config.BaseURL.KEY_NAME;

public class OrderSuccessActivity extends AppCompatActivity {
    private DatabaseHandler db;
    boolean isCoupon;
    Button btn_cnt_shop;
    TextView subTotalAmountf, couponPerTextf, couponAppliedf,delivery_tv,order_ids,
            totalf, vatPercentf, vatPercentAmountf, shippingChargesf, grandTotalAmountf;
    private Session_management sessionManagement;
    private String user_id,TAG="OrderSuccessActivity";
    String message,subTotalSuccess,couponSuccess="",couponSuccessText,totalSuccess,vatSuccess,
            shippingChargeSuccess,grandSuccess,vatSuccessPer,addressSuccess;
    TableRow rowCouponf, totalRowf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);
        db = new DatabaseHandler(this);

        sessionManagement = new Session_management(getApplicationContext());
        user_id = sessionManagement.userId();


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.item_layout_video, null);
        dialogBuilder.setView(dialogView);

        /*String video_url = "android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.order_placed;
        VideoView videoView = dialogView.findViewById(R.id.video);
        Uri videoUri = Uri.parse(video_url);
        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(videoUri);
        videoView.requestFocus();
        videoView.start();

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        alertDialog.setCancelable(false);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                alertDialog.dismiss();
            }
        });
*/

        SweetAlertDialog sweetAlertDialog=new SweetAlertDialog(this,SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.show();
        sweetAlertDialog.setTitle("Order Placed Successfully");
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();
            }
        });


        message=getIntent().getStringExtra("msg");
        subTotalSuccess=getIntent().getStringExtra("amount");
        couponSuccess=getIntent().getStringExtra("discount");
        couponSuccessText=getIntent().getStringExtra("couponSuccessText");
        totalSuccess=getIntent().getStringExtra("totalAmount");
        vatSuccess=getIntent().getStringExtra("vatCharge");
        shippingChargeSuccess=getIntent().getStringExtra("shippingCharge");
        grandSuccess=getIntent().getStringExtra("grand");
        vatSuccessPer=getIntent().getStringExtra("vatRate");
        addressSuccess=getIntent().getStringExtra("address");
        isCoupon=getIntent().getBooleanExtra("isCoupon",false);

        subTotalAmountf = findViewById(R.id.subTotalAmountf);
        rowCouponf = findViewById(R.id.rowCouponf);
        totalRowf = findViewById(R.id.totalRowf);
        btn_cnt_shop = findViewById(R.id.btn_cnt_shop);
        delivery_tv = findViewById(R.id.delivery_tv);
        order_ids = findViewById(R.id.order_ids);
        totalf = findViewById(R.id.totalf);
        couponPerTextf = findViewById(R.id.couponPerf);
        couponAppliedf = findViewById(R.id.couponAppliedf);
        vatPercentf = findViewById(R.id.vatPercentf);
        vatPercentAmountf = findViewById(R.id.vatPercentAmountf);
        shippingChargesf = findViewById(R.id.shippingChargesf);
        grandTotalAmountf = findViewById(R.id.grandTotalAmountf);

        order_ids.setText(""+message);
        delivery_tv.setText(""+addressSuccess);

        if(!isCoupon){
            rowCouponf.setVisibility(View.GONE);
            totalRowf.setVisibility(View.GONE);
        }

        subTotalAmountf.setText(subTotalSuccess);

        couponPerTextf.setText(couponSuccessText);

        couponAppliedf.setText(couponSuccess);

        totalf.setText(totalSuccess);

        vatPercentf.setText(vatSuccess);

        vatPercentAmountf.setText(vatSuccessPer);

        shippingChargesf.setText(shippingChargeSuccess);

        grandTotalAmountf.setText(grandSuccess);


        btn_cnt_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManagement.setFirstCouponUsed(true);
                db.clearCart();
                startActivity(new Intent(OrderSuccessActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        sessionManagement.setFirstCouponUsed(true);
        db.clearCart();
        startActivity(new Intent(OrderSuccessActivity.this, MainActivity.class));
        finish();
    }
}