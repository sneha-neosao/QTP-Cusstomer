package fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import activities.AddAddress;
import com.grocery.QTPmart.MainActivity;
import Config.ApiBaseURL;
import Config.BaseURL;
//import activities.ImagePickerActivity;
import ModelClass.ForgotEmailModel;
import ModelClass.NotifyModelUser;
import ModelClass.ResponseUpdateProfilePic;
import com.grocery.QTPmart.R;
import network.ApiInterface;
import util.AppController;
import util.CustomVolleyJsonRequest;
import util.Session_management;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static android.content.Context.MODE_PRIVATE;
import static Config.BaseURL.updatenotifyby;


public class Edit_profile_fragment extends Fragment implements View.OnClickListener {

    private static final int GALLERY_REQUEST_CODE1 = 201;
    private static String TAG = Edit_profile_fragment.class.getSimpleName();
    SharedPreferences myPrefrence;
    String image;
    String getphone, getid, user_id;
    String getname;
    String getemail, getpassword;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    boolean Email_Status = false, Sms_Status = false, In_App = false;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView update_profile;
    private ProgressDialog progressDialog;
    private EditText et_phone, et_name, et_email, et_oldPassword,et_newPassword,et_confirmPassword;
    private RelativeLayout btn_update;
    private TextView tv_address, tv_name, tv_email, tv_house, tv_socity, btn_socity,update_Password;
    private ImageView iv_profile;
    private String getsocity = "";
    private String filePath = "";
    private Bitmap bitmap;
    private Uri imageuri;
    private Session_management sessionManagement;
    private RadioButton email_yes, email_no, sms_yes, sms_no, inapp_yes, inapp_no;
    private String emaildata = "", smsdata = "", inappdata = "";
    private Context contexts;
    private SwitchCompat email_toggle;
    private SwitchCompat sms_toggle;
    private SwitchCompat inapp_toggle;
    private LinearLayout sms_lay,linearAddress;
    String getAddress="";
    private Activity activity;
    private MultipartBody.Part imageToUpload;
    //private ProgressDialog pd;
    String role="";
    String supplierCode="";

    private ProgressBar imageProgress;

    public Edit_profile_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        contexts = context;
        activity = getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        setHasOptionsMenu(true);
        contexts = container.getContext();
        progressDialog = new ProgressDialog(contexts);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
//        sharedPreferences = contexts.getSharedPreferences("User_profile", MODE_PRIVATE);
//        editor = sharedPreferences.edit();

        getActivity().setTitle("Edit Profile");

       // checkOtpStatus();
//        Email_Status = sharedPreferences.getBoolean("Email", true);
//        Sms_Status = sharedPreferences.getBoolean("Sms", true);
//        In_App = sharedPreferences.getBoolean("App", true);
        sessionManagement = new Session_management(getActivity());
        user_id = sessionManagement.userId();

        //new Thread(this::checkUserNotify).start();
        iv_profile = view.findViewById(R.id.loginImage);
        et_phone = view.findViewById(R.id.et_pro_phone);
        sms_lay = view.findViewById(R.id.sms_lay);
        email_yes = view.findViewById(R.id.email_yes);
        email_no = view.findViewById(R.id.email_no);
        sms_yes = view.findViewById(R.id.sms_yes);
        sms_no = view.findViewById(R.id.sms_no);
        inapp_yes = view.findViewById(R.id.inapp_yes);
        inapp_no = view.findViewById(R.id.inapp_no);
        et_name = view.findViewById(R.id.et_pro_name);
        et_email = view.findViewById(R.id.et_pro_email);
        btn_update = view.findViewById(R.id.btn_pro_edit);
        email_toggle = view.findViewById(R.id.email_toggle);
        sms_toggle = view.findViewById(R.id.sms_toggle);
        inapp_toggle = view.findViewById(R.id.inapp_toggle);
        et_oldPassword = view.findViewById(R.id.et_oldPassword);
        et_newPassword = view.findViewById(R.id.et_newPassword);
        et_confirmPassword = view.findViewById(R.id.et_confirmPassword);
        update_Password = view.findViewById(R.id.update_Password);
        tv_address = view.findViewById(R.id.tv_address);
        linearAddress = view.findViewById(R.id.linearAddress);
        imageProgress = view.findViewById(R.id.imageProgress);





                        getemail = sessionManagement.getUserDetails().get(BaseURL.KEY_EMAIL);
        getpassword = sessionManagement.getUserDetails().get(BaseURL.KEY_PASSWORD);
        String getimage = sessionManagement.getUserDetails().get(BaseURL.KEY_IMAGE);
        getname = sessionManagement.getUserDetails().get(BaseURL.KEY_NAME);
        getphone = sessionManagement.getUserDetails().get(BaseURL.KEY_MOBILE);
        getid = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        Log.d("dd", getid);
        String getpin = sessionManagement.getUserDetails().get(BaseURL.KEY_PINCODE);

        getsocity = sessionManagement.getUserDetails().get(BaseURL.KEY_SOCITY_ID);

        String getsocity_name = sessionManagement.getUserDetails().get(BaseURL.KEY_SOCITY_NAME);
        role = sessionManagement.getUserDetails().get(BaseURL.KEY_ROLE);
        supplierCode = sessionManagement.getUserDetails().get(BaseURL.KEY_SUPPLIERID);
        et_name.setText(getname);
        et_phone.setText(getphone);
        getAddress = sessionManagement.getUserDetails().get(BaseURL.ADDRESS);
        if(!getAddress.equals("")) {
           tv_address.setText(getAddress);
        }
        else
        {
            ViewGroup.LayoutParams params = linearAddress.getLayoutParams();
            params.height = 120;
            linearAddress.setLayoutParams(params);
            tv_address.setText("Click here to add address.");
        }
        tv_address.setOnClickListener(v -> startActivityForResult(new Intent(getActivity(), AddAddress.class), 23));
        update_profile = view.findViewById(R.id.update_profile);


        Picasso.get()
                .load(ApiBaseURL.IMG_URL+"images/profile/profile_" + getid + ".png")
                .placeholder(R.drawable.ic_account_circle_black_24dp)
                .memoryPolicy(MemoryPolicy.NO_STORE, MemoryPolicy.NO_CACHE)
                .into(iv_profile);


        if (sessionManagement.getOtpSatus().equalsIgnoreCase("1")) {
            sms_lay.setVisibility(View.VISIBLE);
        } else {
            sms_lay.setVisibility(View.GONE);
        }

        email_yes.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                email_no.setChecked(false);
                sessionManagement.setEmailServer("1");
                emaildata = "1";
            }
        });

        email_no.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                emaildata = "0";
                email_yes.setChecked(false);
                sessionManagement.setEmailServer("0");
            }
        });

        sms_yes.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                smsdata = "1";
                sms_no.setChecked(false);
                sessionManagement.setUserSMSService("1");
            }
        });

        sms_no.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                smsdata = "0";
                sms_yes.setChecked(false);
                sessionManagement.setEmailServer("0");
            }
        });

        inapp_yes.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                inapp_no.setChecked(false);
                inappdata = "1";
                sessionManagement.setUserInAppService("1");
            }
        });

        inapp_no.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                inappdata = "0";
                inapp_yes.setChecked(false);
                sessionManagement.setUserInAppService("0");
            }
        });


        if (sessionManagement.getEmailService().equalsIgnoreCase("1")) {
            email_yes.setChecked(true);
            email_no.setChecked(false);
            email_toggle.setChecked(true);
            emaildata = "1";
        } else {
            email_yes.setChecked(false);
            email_no.setChecked(true);
            email_toggle.setChecked(false);
            emaildata = "0";
        }

        if (sessionManagement.getSMSService().equalsIgnoreCase("1")) {
            sms_yes.setChecked(true);
            sms_no.setChecked(false);
            sms_toggle.setChecked(true);
            smsdata = "1";
        } else {
            sms_yes.setChecked(false);
            sms_no.setChecked(true);
            sms_toggle.setChecked(false);
            smsdata = "1";
        }

        if (sessionManagement.getINAPPService().equalsIgnoreCase("1")) {
            inapp_yes.setChecked(true);
            inapp_no.setChecked(false);
            inapp_toggle.setChecked(true);
            inappdata = "1";
        } else {
            inapp_no.setChecked(true);
            inapp_yes.setChecked(false);
            inapp_toggle.setChecked(false);
            inappdata = "1";
        }

        email_toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                sessionManagement.setEmailServer("1");
                emaildata = "1";
            } else {
                sessionManagement.setEmailServer("0");
                emaildata = "0";
            }
        });

        sms_toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                smsdata = "1";
                sessionManagement.setUserSMSService("1");
            } else {
                smsdata = "0";
                sessionManagement.setUserSMSService("0");
            }
        });

        inapp_toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                inappdata = "1";
                sessionManagement.setUserInAppService("1");
            } else {
                inappdata = "0";
                sessionManagement.setUserInAppService("0");
            }
        });


//        Notif(user_id,emaildata,smsdata,inappdata);


//        if (email_yes.isChecked()) {
//            emaildata="1";
//            sessionManagement.setEmailServer("1");
//        }
//           if (email_no.isChecked())
//        {
//            emaildata="0";
//            sessionManagement.setEmailServer("0");
//        }
//        if (sms_yes.isChecked())
//        {
//            sessionManagement.setUserSMSService("1");
//smsdata="1";
//        }
//        if (sms_no.isChecked())
//        {
//            smsdata="0";
//            sessionManagement.setUserSMSService("0");
//        }
//        if (inapp_yes.isChecked())
//        {
//            inappdata="1";
//            sessionManagement.setUserInAppService("1");
//        }
//        if (inapp_no.isChecked())
//        {
//            sessionManagement.setUserInAppService("0");
//            inappdata="0";
//        }

//        circle1= view.findViewById(R.id.circle1);
//        circle2= view.findViewById(R.id.circle2);
//        circle3= view.findViewById(R.id.circle3);
//        circle4= view.findViewById(R.id.circle4);
//        circle5= view.findViewById(R.id.circle5);
//        circle6= view.findViewById(R.id.circle6);


//        if (Email_Status){
//            circle1.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//            circle2.setCardBackgroundColor(getResources().getColor(R.color.grey));
//            circle1.setEnabled(false);
//            circle2.setEnabled(true);
//
//        }
//        else {
//            circle2.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//            circle1.setCardBackgroundColor(getResources().getColor(R.color.grey));
//            circle2.setEnabled(false);
//            circle1.setEnabled(true);
//
//        }
//        if (Sms_Status){
//            circle3.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//            circle4.setCardBackgroundColor(getResources().getColor(R.color.grey));
//            circle3.setEnabled(false);
//            circle4.setEnabled(true);
//
//        }
//        else {
//            circle4.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//            circle3.setCardBackgroundColor(getResources().getColor(R.color.grey));
//            circle4.setEnabled(false);
//            circle3.setEnabled(true);
//        }
//
//        if (In_App){
//            circle5.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//            circle6.setCardBackgroundColor(getResources().getColor(R.color.grey));
//            circle5.setEnabled(false);
//            circle6.setEnabled(true);
//
//        }
//        else {
//            circle6.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//            circle5.setCardBackgroundColor(getResources().getColor(R.color.grey));
//            circle6.setEnabled(false);
//            circle5.setEnabled(true);
//        }

        update_profile.setOnClickListener(v -> {

//                if (!user_id.equalsIgnoreCase("")){
//                    String email_,sms_;
////                editor=sharedPreferences.edit();
//                    editor.putBoolean("Email",Email_Status);
//                    editor.putBoolean("Sms",Sms_Status);
//                    editor.putBoolean("App",In_App);
//                    editor.commit();
//                    editor.apply();
//                    //recreate();
//                    if (Email_Status){
//                        email_="1";
//
//                    }
//                    else {
//                        email_="0";
//                    }
//                    if (Sms_Status){
//                        sms_="1";
//                    }
//                    else {
//                        sms_="0";
//                    }
//                    if (In_App){
//                        sms_="1";
//                    }
//                    else {
//                        sms_="0";
//                    }
            progressDialog.show();
            user_id = sessionManagement.userId();
         //   Notif(user_id, emaildata, inappdata, smsdata);
//                }else {
//                    Intent In=new Intent(v.getContext(), LoginActivity.class);
//                    v.getContext().startActivity(In);
//                }
        });


//        circle1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                circle1.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//                circle2.setCardBackgroundColor(getResources().getColor(R.color.grey));
//                circle1.setEnabled(false);
//                Email_Status= true;
//
//                circle2.setEnabled(true);
//
//            }
//        });
//        circle2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Email_Status=false;
//                circle2.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//                circle1.setCardBackgroundColor(getResources().getColor(R.color.grey));
//                circle2.setEnabled(false);
//                circle1.setEnabled(true);
//
//            }
//        });
//        circle3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Sms_Status = true;
//                circle3.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//                circle4.setCardBackgroundColor(getResources().getColor(R.color.grey));
//                circle3.setEnabled(false);
//                circle4.setEnabled(true);
//
//            }
//        });
//        circle4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Sms_Status= false;
//                circle4.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//                circle3.setCardBackgroundColor(getResources().getColor(R.color.grey));
//                circle4.setEnabled(false);
//                circle3.setEnabled(true);
//            }
//        });
//        circle5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                In_App = true;
//                circle5.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//                circle6.setCardBackgroundColor(getResources().getColor(R.color.grey));
//                circle5.setEnabled(false);
//                circle6.setEnabled(true);
//
//            }
//        });
//        circle6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                In_App= false;
//                circle6.setCardBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//                circle5.setCardBackgroundColor(getResources().getColor(R.color.grey));
//                circle6.setEnabled(false);
//                circle5.setEnabled(true);
//            }
//        });

        if (!TextUtils.isEmpty(getimage)) {

//            Glide.with(getActivity())
//                    .load(IMG_PROFILE_URL + getimage)
//                    .centerCrop()
//                    .placeholder(R.drawable.splashicon)
//
//                    .into(iv_profile);
        }

        if (!TextUtils.isEmpty(getemail)) {
            et_email.setText(getemail);
        }

        /*if (!TextUtils.isEmpty(gethouse)){
            et_house.setText(gethouse);
        }*/

        update_Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword=et_newPassword.getText().toString(),confirmPassword=et_confirmPassword.getText().toString(),oldPassword=et_oldPassword.getText().toString();

                if(!newPassword.equals(confirmPassword)){
                    Toast.makeText(getContext(), "Confirm Password Not Match...!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressDialog.show();
                    updatePassword(oldPassword,newPassword);
                }
            }
        });


        btn_update.setOnClickListener(this);

        //btn_socity.setOnClickListener(this);
        iv_profile.setOnClickListener(this);

        return view;
    }

    private void Notif(String user_id, String email1, String app, String sms) {

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        params.put("sms", sms);
        params.put("app", app);
        params.put("email", email1);

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, updatenotifyby, params
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Tag", response.toString());

                try {
                    String message = response.getString("message");

                    String status = response.getString("status");
                    Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
//                    if (status.contains("1")) {
//                        Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
//                    } else {
//
//                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                //   Toast.makeText(getApplicationContext(), ""+error, Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 2;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }


    @Override
    public void onResume() {
        super.onResume();

        getAddress = sessionManagement.getUserDetails().get(BaseURL.ADDRESS);
        if(!getAddress.equals("")) {
            tv_address.setText(getAddress);
        }
    }

    private void checkOtpStatus() {

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
                            sessionManagement.setOtpStatus("0");
                            sms_lay.setVisibility(View.GONE);
                            smsdata = "0";
                            sessionManagement.setUserSMSService("0");
                        } else {
                            sessionManagement.setOtpStatus("1");
                            sessionManagement.setUserSMSService("1");
                            smsdata = "1";
                            sms_lay.setVisibility(View.VISIBLE);
                        }
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ForgotEmailModel> call, @NonNull Throwable t) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_pro_edit) {

            if(et_name.getText().toString().isEmpty()){
                Toast.makeText(getActivity(), "enter user name", Toast.LENGTH_SHORT).show();
            }
            else if (et_email.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "enter email address", Toast.LENGTH_SHORT).show();
            } else {
                if (et_email.getText().toString().trim().matches(emailPattern)) {
                    getphone = et_phone.getText().toString();
                    getname = et_name.getText().toString();
                    getemail = et_email.getText().toString();

//                    storeImage(bitmap);
                    progressDialog.show();
                    updateprofile();

                } else {
                    Toast.makeText(getActivity(), "Invalid email address", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (id == R.id.loginImage) {


            Dexter.withActivity(activity)
                    .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                showImagePickerOptions();
                            }

                            if (report.isAnyPermissionPermanentlyDenied()) {
                                showSettingsDialog();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();

        }
    }

    private void showImagePickerOptions() {
//        ImagePickerActivity.showImagePickerOptions(contexts, new ImagePickerActivity.PickerOptionListener() {
//            @Override
//            public void onTakeCameraSelected() {
//                launchCameraIntent();
//            }
//
//            @Override
//            public void onChooseGallerySelected() {
//                launchGalleryIntent();
//            }
//        });
    }

    private void launchCameraIntent() {
//        Intent intent = new Intent(contexts, ImagePickerActivity.class);
//        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);
//
//        // setting aspect ratio
//        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
//
//        // setting maximum bitmap width and height
//        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
//        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
//        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);
//
//        startActivityForResult(intent, 101);
    }

    private void launchGalleryIntent() {
//        Intent intent = new Intent(contexts, ImagePickerActivity.class);
//        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
//
//        // setting aspect ratio
//        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
//        startActivityForResult(intent, 101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");

                    RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), new File(uri.getPath()));
                    imageToUpload = MultipartBody.Part.createFormData("files", "profilePhoto.png", mFile);

                    uploadImage();
            }
        }
    }


    private void uploadImage() {

        imageProgress.setVisibility(View.VISIBLE);
        RequestBody custId =RequestBody.create(MediaType.parse("text/plain"),getid);


        Retrofit emailOtp = new Retrofit.Builder()
                .baseUrl(ApiBaseURL.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        ApiInterface apiInterface = emailOtp.create(ApiInterface.class);

        apiInterface.updateProfilePic(imageToUpload, custId).enqueue(new Callback<ResponseUpdateProfilePic>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<ResponseUpdateProfilePic> call, retrofit2.Response<ResponseUpdateProfilePic> response) {
                //pd.dismiss();
                if (response.isSuccessful())
                {

                    if (response.body().getStatus())
                    {

                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        Picasso.get()
                                .load(ApiBaseURL.IMG_URL+"images/profile/profile_" + getid + ".png")
                                .placeholder(R.drawable.ic_account_circle_black_24dp)
                                .into(iv_profile, new com.squareup.picasso.Callback() {
                                    @Override
                                    public void onSuccess() {
                                        imageProgress.setVisibility(View.GONE);
                                        MainActivity main= (MainActivity) contexts;
                                        main.updateProfilePic();
                                    }

                                    @Override
                                    public void onError(Exception e) {

                                    }

                                });



                        //Picasso.with(contexts).

                        //Picasso.get().load(response.body().getResult().getUserProfilePhoto()).placeholder(R.mipmap.ic_launcher_round).into(((MainActivity)getActivity()).imageView);
                    }
                    else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getActivity(), "Something went wrong please try again", Toast.LENGTH_SHORT).show();
                }            }
            @Override
            public void onFailure(Call<ResponseUpdateProfilePic> call, Throwable t) {

                Toast.makeText(getActivity(),t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



                /*

                enqueue(new Callback<ResponseUpdateProfilePic>() {
            @Override
            public void onResponse(@NonNull Call<ResponseUpdateProfilePic> call, @NonNull retrofit2.Response<ResponseUpdateProfilePic> response) {

                if (response.isSuccessful()) {
                    boolean status = Boolean.parseBoolean(response.body().getStatus());
                    if (status)
                    {

                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        //Picasso.with(contexts).

                        //Picasso.get().load(response.body().getResult().getUserProfilePhoto()).placeholder(R.mipmap.ic_launcher_round).into(((MainActivity)getActivity()).imageView);
                    }
                    else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseUpdateProfilePic> call, @NonNull Throwable t) {

                Toast.makeText(contexts, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
*/



    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(contexts);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", contexts.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void attemptEditProfile() {

        // tv_phone.setText(getResources().getString(R.string.et_login_phone_hint));
//        tv_email.setText(getResources().getString(R.string.tv_login_email));
//        tv_name.setText(getResources().getString(R.string.tv_reg_name_hint));
        /*tv_house.setText(getResources().getString(R.string.tv_reg_house));
        tv_socity.setText(getResources().getString(R.string.tv_reg_socity));*/

//        tv_name.setTextColor(getResources().getColor(R.color.dark_gray));
        // tv_phone.setTextColor(getResources().getColor(R.color.dark_gray));
//        tv_email.setTextColor(getResources().getColor(R.color.dark_gray));
        /*tv_house.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_socity.setTextColor(getResources().getColor(R.color.dark_gray));*/


        Log.d("jj", getname + getphone);
        /*String gethouse = et_house.getText().toString();
        String getsocity = sessionManagement.getUserDetails().get(BaseURL.KEY_SOCITY_ID);*/


    }

    private boolean isPhoneValid(String phoneno) {
        return phoneno.length() > 9;
    }

    private void storeImage(Bitmap thumbnail) {
//        if (iv_profile.getDrawable() == null) {
//            //  Toast.makeText(getActivity(), "Select Image", Toast.LENGTH_SHORT).show();
//
//        } else {
//            myPrefrence = PreferenceManager.getDefaultSharedPreferences(getActivity());
//            SharedPreferences.Editor edit = myPrefrence.edit();
//            edit.remove("image_data");
//            edit.commit();
//            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//            File destination = new File(Environment.getExternalStorageDirectory(),
//                    System.currentTimeMillis() + ".jpg");
//            FileOutputStream fo;
//            try {
//                destination.createNewFile();
//                fo = new FileOutputStream(destination);
//                fo.write(bytes.toByteArray());
//                fo.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            iv_profile.setImageBitmap(thumbnail);
//            byte[] b = bytes.toByteArray();
//            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
//            edit.putString("image_data", encodedImage);
//            edit.commit();
//
//        }

    }

    private void updateprofile() {

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("custName", getname);
        params.put("custID", getid);
        params.put("cusMob", getphone);
        params.put("cusEmail", getemail);
        Log.d("dsd", String.valueOf(params));

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, ApiBaseURL.ProfileUpdate, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Tag", response.toString());
                progressDialog.dismiss();
                try {

                    boolean status = response.getBoolean("status");
                    String message = response.getString("message");


                    if (status) {
                        try {
                            Session_management sessionManagement = new Session_management(getContext());
                            SharedPreferences.Editor editor = getContext().getSharedPreferences(BaseURL.MyPrefreance, MODE_PRIVATE).edit();
                            editor.putString(BaseURL.KEY_MOBILE, getphone);
                            editor.putString(BaseURL.KEY_PASSWORD, "");
                            editor.apply();
                            sessionManagement.createLoginSessionLogin(getid, getemail, getname, getphone, "", getAddress,role,supplierCode);
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                    Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
//                Toast.makeText(Search.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);

    }

    private void updatePassword(String oldPassword,String newPassword) {
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("CPassword", newPassword);
        params.put("oldPassword", oldPassword);
        params.put("custID", getid);

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, ApiBaseURL.UpdatePassword, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Tag", response.toString());
                progressDialog.dismiss();
                try {

                    boolean status = response.getBoolean("status");
                    String message = response.getString("message");

                    if (status) {
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                    }
                    Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
//                Toast.makeText(Search.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);

      /*  MenuItem cart = menu.findItem(R.id.action_cart);
        cart.setVisible(false);
*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }
        return false;
    }

    private void checkUserNotify() {
        Retrofit emailOtp = new Retrofit.Builder()
                .baseUrl(BaseURL.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        ApiInterface apiInterface = emailOtp.create(ApiInterface.class);

        Call<NotifyModelUser> checkOtpStatus = apiInterface.getNotifyUser(user_id);

        checkOtpStatus.enqueue(new Callback<NotifyModelUser>() {
            @Override
            public void onResponse(@NonNull Call<NotifyModelUser> call, @NonNull retrofit2.Response<NotifyModelUser> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        NotifyModelUser modelUser = response.body();
                        if (modelUser.getStatus().equalsIgnoreCase("1")) {
                            sessionManagement.setEmailServer(modelUser.getData().getEmail());
                            sessionManagement.setUserSMSService(modelUser.getData().getSms());
                            sessionManagement.setUserInAppService(modelUser.getData().getApp());
                        } else {
                            sessionManagement.setEmailServer("0");
                            sessionManagement.setUserSMSService("0");
                            sessionManagement.setUserInAppService("0");
                        }
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<NotifyModelUser> call, @NonNull Throwable t) {

            }
        });

    }

}
