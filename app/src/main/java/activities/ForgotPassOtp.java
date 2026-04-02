package activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import Config.ApiBaseURL;
import Config.BaseURL;
import ModelClass.ForgotEmailModel;
import com.grocery.QTPmart.R;
import network.ApiInterface;
import util.AppController;
import util.CustomVolleyJsonRequest;
import util.Session_management;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;


public class ForgotPassOtp extends AppCompatActivity {
    public static String TAG = "Login";
   /* EditText et_req_mobile;
    CardView cvverify;
    CardView cv_email;
    ProgressDialog progressDialog;
    Button verify;
    TextView edit, txt_mobile;
    LinearLayout ll_edit;
    EditText et_otp;
    EditText et_mail;
    String MobileNO;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";*/
    private Session_management session_management;

    //TextInputEditText edt_email;
    EditText edt_email;
    ProgressDialog pd;
    ImageView email_iv,img_forgot_pass,img_back;
    View cv_email;
    TextView txt_pass_1,txt_pass_2,txt_login;
    LinearLayout ll_mail_sent,login_layout;
    MaterialButton btn_forgot_pass_login,btn_req_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_new);
        /*et_req_mobile = findViewById(R.id.et_req_mobile);
        cvverify = findViewById(R.id.cvverify);
        cv_email = findViewById(R.id.cv_email);
        et_mail = findViewById(R.id.et_mail);*/

        edt_email = findViewById(R.id.edt_email);
        btn_forgot_pass_login = findViewById(R.id.btn_forgot_pass_login);
       // cv_email = findViewById(R.id.view_email);
        //email_iv = findViewById(R.id.email_iv);
        img_forgot_pass = findViewById(R.id.img_forgot_pass);
        ll_mail_sent = findViewById(R.id.ll_mail_sent);
        login_layout = findViewById(R.id.login_layout);
        txt_pass_1 = findViewById(R.id.txt_pass_1);
        txt_pass_2 = findViewById(R.id.txt_pass_2);
        txt_login = findViewById(R.id.txt_login);
        img_back = findViewById(R.id.img_back);
        btn_req_send = findViewById(R.id.btn_req_send);

        session_management = new Session_management(this);
        pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCanceledOnTouchOutside(false);
     //   checkOtpStatus();
     //   cv_email.setVisibility(View.GONE);
//        if (session_management.getOtpSatus().equalsIgnoreCase("0")) {
//            cv_email.setVisibility(View.VISIBLE);
//            cvverify.setVisibility(View.VISIBLE);
//        } else {
//            cv_email.setVisibility(View.GONE);
//            cvverify.setVisibility(View.VISIBLE);
//        }

        img_back.setOnClickListener(view -> {
            finish();
        });

        btn_req_send.setOnClickListener(v -> {
            if (edt_email.getText().toString().length() > 0) {
                pd.show();
                makeotpRequest(edt_email.getText().toString());
            } else {
                edt_email.setError("Enter valid email, mobile or user id!");
            }
        });

        txt_login.setOnClickListener(view -> {
            startActivity(new Intent(ForgotPassOtp.this,LoginActivity.class));
            finish();
        });
    }


    private void makeotpRequest(String user_phone)
    {
        String tag_json_obj = "json_forgot_password_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("cusMob", user_phone);
        params.put("branchid", ApiInterface.branchcode);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ApiBaseURL.forgotPasswort, params, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("CheckApi forgot pass", response.toString());
                pd.dismiss();
                try {
                    boolean status = response.getBoolean("status");
                    String msg = response.getString("message");
                    if (status) {
                        login_layout.setVisibility(View.GONE);
                        ll_mail_sent.setVisibility(View.VISIBLE);
                        Glide.with(ForgotPassOtp.this)
                                .load(R.drawable.forgot_pass_sent)
                                .into(img_forgot_pass);

                        txt_pass_1.setText("Email has been sent!");
                        txt_pass_2.setText("Please check your inbox and click in the \n" +
                                "received link to reset a password.");

                        Toast.makeText(ForgotPassOtp.this, ""+msg, Toast.LENGTH_LONG).show();
                    }
                    else
                    {

                        Toast.makeText(ForgotPassOtp.this, ""+msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    pd.dismiss();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                pd.dismiss();
                VolleyLog.d("", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                }
            }
        });

        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    private void checkOtpStatus() {
        pd.show();
        Retrofit emailOtp = new Retrofit.Builder()
                .baseUrl(BaseURL.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        ApiInterface apiInterface = emailOtp.create(ApiInterface.class);

        Call<ForgotEmailModel> checkOtpStatus = apiInterface.getOtpOnOffStatus();
        checkOtpStatus.enqueue(new Callback<ForgotEmailModel>() {
            @Override
            public void onResponse(@NonNull Call<ForgotEmailModel> call, @NonNull retrofit2.Response<ForgotEmailModel> response) {
                if (response.isSuccessful()) {
                    ForgotEmailModel model = response.body();
                    if (model != null) {
                        if (model.getStatus().equalsIgnoreCase("0")) {
                            session_management.setOtpStatus("0");
                            cv_email.setVisibility(View.VISIBLE);

                        } else {
                            session_management.setOtpStatus("1");
                            cv_email.setVisibility(View.GONE);
                        }
                    }

                }
                pd.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<ForgotEmailModel> call, @NonNull Throwable t) {
                pd.dismiss();
            }
        });

    }
}