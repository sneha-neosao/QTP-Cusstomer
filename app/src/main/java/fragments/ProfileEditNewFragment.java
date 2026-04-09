package fragments;

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
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import activities.MainDrawerActivity;
import Config.ApiBaseURL;
import Config.BaseURL;
//import activities.ImagePickerActivity;
import ModelClass.CityModel;
import ModelClass.CountryModel;
import ModelClass.ResponseUpdateProfilePic;
import ModelClass.StateModel;
import com.grocery.QTPmart.R;
import network.ApiInterface;
import util.AppController;
import util.CustomVolleyJsonRequest;
import util.NetworkConnection;
import util.Session_management;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.hbb20.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class ProfileEditNewFragment extends Fragment {

    private Session_management sessionManagement;
    EditText edt_fullName,edt_email,edt_contact,edt_dob,edt_country1,edt_state1,edt_city1,edt_landmark,edt_street,edt_area,edt_pincode,edt_gender,
            edt_marital_status,edt_blood_group,edt_old_password,edt_new_password,edt_confirmPass;
    LinearLayout ff;

    String  getid;
    private ProgressDialog progressDialog;
    private Context contexts;

    List<String> countryNames=new ArrayList<>();
    List<String> cityNames=new ArrayList<>();
    List<String> stateNames=new ArrayList<>();
    List<String> countryCodes=new ArrayList<>();
    List<String> cityCodes=new ArrayList<>();
    List<String> stateCodes=new ArrayList<>();
    List<CountryModel> countryModelList=new ArrayList<>();
    List<StateModel> stateModelList=new ArrayList<>();
    List<CityModel> cityModelList=new ArrayList<>();

    AutoCompleteTextView edt_country,edt_city,edt_state;

    String countryCode="";
    String stateCode;
    String cityCode;
    String role="";
    String supplierCode="";
    private int mYear, mMonth, mDay, mHour, mMinute;

    CardView cvFullName,cvEmail,cvContactNum,cvDob,cvCountry,cvState,cvCity,cvLandmark,cvStreet,cvArea,cvPostCode;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private MultipartBody.Part imageToUpload;
    ImageView iv_profile;

    String selected_dob="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile_edit_new, container, false);

        sessionManagement = new Session_management(getContext());
        edt_fullName=view.findViewById(R.id.edt_fullName);
        edt_email=view.findViewById(R.id.edt_email);
        edt_contact=view.findViewById(R.id.edt_contact);
        edt_dob=view.findViewById(R.id.edt_dob);
        edt_country=view.findViewById(R.id.edt_country);
        edt_state=view.findViewById(R.id.edt_state);
        edt_city=view.findViewById(R.id.edt_city);
        edt_landmark=view.findViewById(R.id.edt_landmark);
        edt_street=view.findViewById(R.id.edt_street);
        edt_area=view.findViewById(R.id.edt_area);
        edt_pincode=view.findViewById(R.id.edt_pincode);
        edt_gender=view.findViewById(R.id.edt_gender);
        edt_marital_status=view.findViewById(R.id.edt_marital_status);
        edt_blood_group=view.findViewById(R.id.edt_blood_group);
        CountryCodePicker ccp = view.findViewById(R.id.ccp);


        cvFullName=view.findViewById(R.id.cvFullName);
        cvEmail=view.findViewById(R.id.cvEmail);
        cvContactNum=view.findViewById(R.id.cvContactNum);
        cvDob=view.findViewById(R.id.cvDob);
        cvCountry=view.findViewById(R.id.cvCountry);
        cvState=view.findViewById(R.id.cvState);
        cvCity=view.findViewById(R.id.cvCity);
        cvLandmark=view.findViewById(R.id.cvLandmark);
        cvStreet=view.findViewById(R.id.cvStreet);
        cvArea=view.findViewById(R.id.cvArea);
        cvPostCode=view.findViewById(R.id.cvPostCode);

        iv_profile=view.findViewById(R.id.iv_profile);

        TextView tvUpdatePassword=view.findViewById(R.id.tvUpdatePassword);
        //edt_old_password=view.findViewById(R.id.edt_old_password);
        //edt_new_password=view.findViewById(R.id.edt_new_password);
        //edt_confirmPass=view.findViewById(R.id.edt_confirmPass);
        Button btn_submit=view.findViewById(R.id.btn_submit);
        Button btn_update_password=view.findViewById(R.id.btn_update_password);

        getid = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        role = sessionManagement.getUserDetails().get(BaseURL.KEY_ROLE);
        supplierCode = sessionManagement.getUserDetails().get(BaseURL.KEY_SUPPLIERID);
        edt_fullName.setText(sessionManagement.getUserFullName());
        edt_email.setText(sessionManagement.getUserEmail());
        edt_contact.setText(sessionManagement.getUserMobile());
        edt_dob.setText(sessionManagement.getUserDOB().equals("null")?"":sessionManagement.getUserDOB());
        edt_country.setText(sessionManagement.getUserCountry().equals("null")?"":sessionManagement.getUserCountry());
        edt_state.setText(sessionManagement.getUserState().equals("null")?"":sessionManagement.getUserState());
        edt_city.setText(sessionManagement.getUserCity().equals("null")?"":sessionManagement.getUserCity());
        edt_landmark.setText(sessionManagement.getUserLandmark().equals("null")?"":sessionManagement.getUserLandmark());
        edt_street.setText(sessionManagement.getUserStreet().equals("null")?"":sessionManagement.getUserStreet());
        edt_area.setText(sessionManagement.getUserDetails().get(BaseURL.ADDRESS).equals("null")?"":sessionManagement.getUserDetails().get(BaseURL.ADDRESS));
        edt_pincode.setText(sessionManagement.getUserPinCode().equals("null")?"":sessionManagement.getUserPinCode());
        countryCode = sessionManagement.getUserCountryCode();

        String userCountryCode = sessionManagement.getUserCountryCode();
        if (userCountryCode != null && !userCountryCode.isEmpty()) {
            ccp.setCountryForPhoneCode(Integer.parseInt(userCountryCode.replace("+", "")));
        }

        contexts = container.getContext();
        progressDialog = new ProgressDialog(contexts);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        if(!sessionManagement.getUserDOB().equals("null")) {
            String dtStart = sessionManagement.getUserDOB();
            SimpleDateFormat spf = new SimpleDateFormat("dd-mm-yyyy");

            try {
                Date date = spf.parse(dtStart);
                spf = new SimpleDateFormat("yyyy-mm-dd");
                selected_dob=spf.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        /**Button Update password click*/
        /*btn_update_password.setOnClickListener(view1 -> {
            showDialogChangePassword();
        });*/

        tvUpdatePassword.setOnClickListener(view1 -> {
            showDialogChangePassword();
        });

        /*Load Profile Image*/
        Long time = System.currentTimeMillis();
        Picasso.get()
                .load(ApiBaseURL.IMG_URL_NEW+"profile_" + getid + ".png"+"?"+time)
                .placeholder(R.drawable.toy_face)
                .memoryPolicy(MemoryPolicy.NO_STORE, MemoryPolicy.NO_CACHE)
                .into(iv_profile);

        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(getActivity())
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
        });

        cvFullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showKeyBoard(edt_fullName);
            }
        });


        cvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showKeyBoard(edt_email);
            }
        });


        cvContactNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showKeyBoard(edt_contact);
            }
        });

        cvDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm =(InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(),0);
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                edt_dob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                selected_dob=year+"-"+(monthOfYear + 1)+"-"+dayOfMonth;
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        cvCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCountries();
                showKeyBoard(edt_country);
            }
        });

        cvState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countryCode==null|| countryCode.isEmpty()){
                    showToast("Country required");
                }else{
                    getStates(countryCode);
                    hideKeyboard(edt_country);
                    hideKeyboard(edt_city);
                    showKeyBoard(edt_state);
                }

            }
        });

        cvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stateCode==null||stateCode.isEmpty()){
                    showToast("State required");
                }else{
                    getCities(stateCode);
                    hideKeyboard(edt_state);
                    hideKeyboard(edt_country);
                    showKeyBoard(edt_city);
                }

            }
        });

        edt_country.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hideKeyboard(edt_country);
                for(int i=0;i<countryModelList.size();i++){
                    if(countryModelList.get(i).getCountry_Name().equalsIgnoreCase(parent.getItemAtPosition(position).toString())){
                        countryCode=countryModelList.get(position).getCountry_ID();
                    }
                }
            }
        });

        edt_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hideKeyboard(edt_state);
                for(StateModel stateModel:stateModelList){
                    if(stateModel.getState_Name().equals(parent.getItemAtPosition(position))){
                        stateCode = stateModel.getState_ID();
                    }
                }
            }
        });

        edt_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                hideKeyboard(edt_city);
                for(CityModel cityModel:cityModelList){
                    if(cityModel.getCity_Name().equals(adapterView.getItemAtPosition(i))){
                        cityCode=cityModel.getCity_ID();
                    }
                }

            }
        });

        cvLandmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showKeyBoard(edt_landmark);
            }
        });

        cvStreet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showKeyBoard(edt_street);
            }
        });

        cvArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showKeyBoard(edt_area);
            }
        });

        cvPostCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showKeyBoard(edt_pincode);
            }
        });

        /**Button submit click*/
        btn_submit.setOnClickListener(view1 -> {

            validation();
            //updateProfile();
        });

        return view;
    }

    private void showDialogChangePassword() {

        Dialog dialog=new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_change_password);

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,height);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations =  R.style.DialogAnimation;
        dialog.show();

        EditText edt_old_password = dialog.findViewById(R.id.edt_old_password);
        EditText edt_new_password = dialog.findViewById(R.id.edt_new_password);
        EditText edt_confirmPass = dialog.findViewById(R.id.edt_confirmPass);

        ImageView iv_show_hide_old_password = dialog.findViewById(R.id.iv_show_hide_old_password);
        ImageView iv_show_hide_new_password = dialog.findViewById(R.id.iv_show_hide_new_password);
        ImageView iv_show_hide_confirm_password = dialog.findViewById(R.id.iv_show_hide_confirm_password);

        ImageView close = dialog.findViewById(R.id.close);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnUpdate = dialog.findViewById(R.id.btnUpdate);



        iv_show_hide_old_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId()==R.id.iv_show_hide_old_password){

                    if(edt_old_password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                        iv_show_hide_old_password.setImageResource(R.drawable.ic_icon_feather_eye);

                        //Show Password
                        edt_old_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                    else{
                        iv_show_hide_old_password.setImageResource(R.drawable.ic_icon_feather_eye_off);
                        //Hide Password
                        edt_old_password.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    }
                }
            }
        });

        iv_show_hide_new_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId()==R.id.iv_show_hide_new_password){

                    if(edt_new_password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                        iv_show_hide_new_password.setImageResource(R.drawable.ic_icon_feather_eye);

                        //Show Password
                        edt_new_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                    else{
                        iv_show_hide_new_password.setImageResource(R.drawable.ic_icon_feather_eye_off);
                        //Hide Password
                        edt_new_password.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    }
                }
            }
        });

        iv_show_hide_confirm_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId()==R.id.iv_show_hide_confirm_password){

                    if(edt_confirmPass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                        iv_show_hide_confirm_password.setImageResource(R.drawable.ic_icon_feather_eye);

                        //Show Password
                        edt_confirmPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                    else{
                        iv_show_hide_confirm_password.setImageResource(R.drawable.ic_icon_feather_eye_off);
                        //Hide Password
                        edt_confirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    }
                }
            }
        });



        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newPassword=edt_new_password.getText().toString(),confirmPassword=edt_confirmPass.getText().toString(),oldPassword=edt_old_password.getText().toString();

                if(oldPassword.isEmpty()){
                    Toast.makeText(getContext(), "Old Password required", Toast.LENGTH_SHORT).show();
                }else if(newPassword.isEmpty()){
                    Toast.makeText(getContext(), "New Password required", Toast.LENGTH_SHORT).show();
                }else if(confirmPassword.isEmpty()){
                    Toast.makeText(getContext(), "Confirm Password required", Toast.LENGTH_SHORT).show();
                }else if(!newPassword.equals(confirmPassword)){
                    Toast.makeText(getContext(), "Confirm Password Not Match...!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(NetworkConnection.connectionChecking(requireActivity())){
                        updatePassword(oldPassword,newPassword,dialog);
                    }else{
                        Toast.makeText(getActivity(),getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
    }


    public void getCountries()
    {
        progressDialog.show();
        countryModelList.clear();
        countryNames.clear();
        countryCodes.clear();
        String tag_json_obj = "json_cart_list_req";
        Map<String, String> params = new HashMap<String, String>();

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ApiBaseURL.getCountries, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Log.d("getCountries", response.toString());

                try {
                    boolean status = response.getBoolean("status");

                    if (status) {
                        JSONArray jsonArray = response.getJSONArray("result");
                        List<String> countries=new ArrayList<>();
                        List<String> countriesCode=new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            countries.add(jsonObject.getString("country_Name"));
                            countriesCode.add(jsonObject.getString("country_ID"));
                            CountryModel countryModel = new CountryModel();
                            countryModel.setCountry_ID(jsonObject.getString("country_ID"));
                            countryModel.setCountry_Name(jsonObject.getString("country_Name"));
                            countryModelList.add(countryModel);
                        }
                        countryNames.addAll(countries);
                        countryCodes.addAll(countriesCode);


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(contexts,android.R.layout.simple_list_item_1,countryNames);
                        edt_country.setAdapter(adapter);
                        edt_country.setThreshold(0);
                        edt_country.showDropDown();
                    }
                    else
                    {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                error.printStackTrace();
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

    public void getStates(String countryCode){
        progressDialog.show();
        stateModelList.clear();
        stateNames.clear();
        stateCodes.clear();
        String tag_json_obj = "json_cart_list_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("country_ID",countryCode);
        Log.d("params", params.toString());

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ApiBaseURL.getStates, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Log.d("getStates", response.toString());

                try {
                    boolean status = response.getBoolean("status");

                    if (status) {
                        JSONArray jsonArray = response.getJSONArray("result");
                        List<String> countries=new ArrayList<>();
                        List<String> countriesCode=new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            countries.add(jsonObject.getString("state_Name"));
                            countriesCode.add(jsonObject.getString("state_ID"));
                            StateModel stateModel = new StateModel();
                            stateModel.setState_ID(jsonObject.getString("state_ID"));
                            stateModel.setState_Name(jsonObject.getString("state_Name"));
                            stateModelList.add(stateModel);
                        }

                        stateNames.addAll(countries);
                        stateCodes.addAll(countriesCode);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(contexts,android.R.layout.simple_list_item_1,stateNames);
                        edt_state.setAdapter(adapter);
                        edt_state.setThreshold(1);
                        edt_state.showDropDown();

                    }
                    else
                    {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                error.printStackTrace();
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

    public void getCities(String stateID){
        progressDialog.show();
        cityModelList.clear();
         cityNames.clear();
         cityCodes.clear();
        String tag_json_obj = "json_cart_list_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("state_ID",stateID);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ApiBaseURL.getCities, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Log.d("getCities", response.toString());

                try {
                    boolean status = response.getBoolean("status");

                    if (status) {
                        JSONArray jsonArray = response.getJSONArray("result");
                        List<String> countries=new ArrayList<>();
                        List<String> countriesCode=new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            countries.add(jsonObject.getString("city_Name"));
                            countriesCode.add(jsonObject.getString("city_ID"));
                            CityModel cityModel = new CityModel();
                            cityModel.setCity_ID(jsonObject.getString("city_ID"));
                            cityModel.setCity_Name(jsonObject.getString("city_Name"));
                            cityModelList.add(cityModel);
                        }

                        cityNames.addAll(countries);
                        cityCodes.addAll(countriesCode);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                (contexts,android.R.layout.simple_list_item_1,cityNames);
                        edt_city.setAdapter(adapter);
                        edt_city.setThreshold(1);
                        edt_city.showDropDown();
                    }
                    else
                    {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                error.printStackTrace();
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


    private void updatePassword(String oldPassword,String newPassword,Dialog dialog) {
        progressDialog.show();
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("CPassword", newPassword);
        params.put("oldPassword", oldPassword);
        params.put("custID", getid);

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, ApiBaseURL.UpdatePassword, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Tag", response.toString());
                dialog.dismiss();
                progressDialog.dismiss();
                try {

                    boolean status = response.getBoolean("status");
                    String message = response.getString("message");

                    if (status) {
                        //Intent intent = new Intent(getContext(), MainActivity.class);
                        //startActivity(intent);
                        Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                progressDialog.dismiss();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    private void validation(){
        if(edt_fullName.getText().toString().isEmpty()||edt_fullName.getText().toString().equalsIgnoreCase("null")){
            showToast(getString(R.string.full_name_required));
        }else if(edt_email.getText().toString().isEmpty()||edt_email.getText().toString().equalsIgnoreCase("null")){
            showToast(getString(R.string.email_required));
        }else if(!edt_email.getText().toString().trim().matches(emailPattern)){
            showToast(getString(R.string.valid_email_required));
        } else if(edt_contact.getText().toString().isEmpty()||edt_contact.getText().toString().equalsIgnoreCase("null")){
            showToast(getString(R.string.contact_no_required));
        }else if(edt_dob.getText().toString().isEmpty()||edt_dob.getText().toString().equalsIgnoreCase("null")){
            showToast(getString(R.string.dob_required));
        }else if(edt_country.getText().toString().isEmpty()||edt_country.getText().toString().equalsIgnoreCase("null")){
            showToast(getString(R.string.country_required));
        }else if(edt_state.getText().toString().isEmpty()||edt_state.getText().toString().equalsIgnoreCase("null")){
            showToast(getString(R.string.state_required));
        }else if(edt_city.getText().toString().isEmpty()||edt_city.getText().toString().equalsIgnoreCase("null")){
            showToast(getString(R.string.city_required));
        }else if(edt_landmark.getText().toString().isEmpty()||edt_landmark.getText().toString().equalsIgnoreCase("null")){
            showToast(getString(R.string.landmark_required));
        }else if(edt_street.getText().toString().isEmpty()||edt_street.getText().toString().equalsIgnoreCase("null")){
            showToast(getString(R.string.street_required));
        }else if(edt_area.getText().toString().isEmpty()||edt_area.getText().toString().equalsIgnoreCase("null")){
            showToast(getString(R.string.area_required));
        }else if(edt_pincode.getText().toString().isEmpty()||edt_pincode.getText().toString().equalsIgnoreCase("null")){
            showToast(getString(R.string.postcode_required));
        }else{
            updateProfile();
        }
    }

    private void updateProfile() {
        progressDialog.show();
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("custName", edt_fullName.getText().toString().trim());
        params.put("custID", getid);
        params.put("cusMob",  edt_contact.getText().toString().trim());
        params.put("cusEmail",  edt_email.getText().toString().trim());
        params.put("Countrycode", countryCode);
        params.put("cusAdd1", edt_landmark.getText().toString().trim());
        params.put("cusAdd2", edt_street.getText().toString().trim());
        params.put("DOB", selected_dob);
        params.put("country", edt_country.getText().toString().trim());
        params.put("State", edt_state.getText().toString().trim());
        params.put("City", edt_city.getText().toString().trim());
        params.put("Area", edt_area.getText().toString().trim());
        params.put("Zipcode", edt_pincode.getText().toString().trim());
        Log.d("dsd", String.valueOf(params).trim());

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, ApiBaseURL.ProfileUpdate1, params, new Response.Listener<JSONObject>() {
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
                            sessionManagement.setUserFullName(edt_fullName.getText().toString());
                            sessionManagement.setUserMobile(edt_contact.getText().toString());
                            sessionManagement.setUserEmail(edt_email.getText().toString());
                            sessionManagement.setUserCountry(edt_country.getText().toString());
                            sessionManagement.setUserState(edt_state.getText().toString() );
                            sessionManagement.setUserCity(edt_city.getText().toString() );
                            sessionManagement.setUserLandmark(edt_landmark.getText().toString() );
                            sessionManagement.setUserStreet(edt_street.getText().toString() );
                            sessionManagement.setUserPinCode(edt_pincode.getText().toString());
                            sessionManagement.setUserDOB(edt_dob.getText().toString());
                            sessionManagement.setUserCountryCode(countryCode);

                            SharedPreferences.Editor editor = getContext().getSharedPreferences(BaseURL.MyPrefreance, MODE_PRIVATE).edit();
                            editor.putString(BaseURL.KEY_NAME, edt_fullName.getText().toString());
                            editor.putString(BaseURL.KEY_EMAIL, edt_email.getText().toString());
                            editor.putString(BaseURL.KEY_MOBILE, edt_contact.getText().toString());
                            editor.putString(BaseURL.USER_COUNTRY, edt_country.getText().toString());
                            editor.putString(BaseURL.USER_STATE,edt_state.getText().toString() );
                            editor.putString(BaseURL.USER_CITY,edt_city.getText().toString() );
                            editor.putString(BaseURL.USER_LANDMARK,edt_landmark.getText().toString() );
                            editor.putString(BaseURL.USER_STREET,edt_street.getText().toString() );
                            editor.putString(BaseURL.ADDRESS,edt_area.getText().toString());
                            editor.putString(BaseURL.KEY_PINCODE,edt_pincode.getText().toString());
                            editor.putString(BaseURL.USER_DOB,edt_dob.getText().toString());
                            editor.putString(BaseURL.USER_COUNTRY_CODE,countryCode);
                            editor.apply();
                            Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
//                            sessionManagement.createLoginSession(getid, getemail, getname, getphone, "", getAddress,role,supplierCode);
                            sessionManagement.createUpdateProfileSession(getid, edt_email.getText().toString(),
                            edt_fullName.getText().toString(),
                                    edt_contact.getText().toString(), edt_area.getText().toString(),role,supplierCode,
                                    edt_country.getText().toString(), edt_state.getText().toString(),
                                    edt_city.getText().toString(), edt_landmark.getText().toString(),
                                    edt_street.getText().toString(), edt_dob.getText().toString(),
                                    edt_pincode.getText().toString(),countryCode);
                            Intent intent = new Intent(getContext(), MainDrawerActivity.class);
                            intent.putExtra("loadFrag",1);
                            startActivity(intent);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }else{
                        Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                //Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
                showToast(error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);

    }

    private void showKeyBoard(EditText yourEditText){
        yourEditText.requestFocus();
        InputMethodManager imm =(InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(yourEditText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideKeyboard(EditText yourEditText){
        InputMethodManager imm =(InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(yourEditText.getWindowToken(), 0);
    }

    private void showToast(String message){
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
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

//    private void launchCameraIntent() {
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
//    }

//    private void launchGalleryIntent() {
//        Intent intent = new Intent(contexts, ImagePickerActivity.class);
//        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
//
//        // setting aspect ratio
//        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
//        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
//        startActivityForResult(intent, 101);
//    }

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

        //imageProgress.setVisibility(View.VISIBLE);
        progressDialog.show();
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
                progressDialog.dismiss();
                if (response.isSuccessful())
                {
                    if (response.body().getStatus())
                    {

                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        Long time = System.currentTimeMillis();

                        Picasso.get()
                                .load(ApiBaseURL.IMG_URL_NEW+"profile_" + getid + ".png"+"?"+time)
                                .placeholder(R.drawable.toy_face)
                                .into(iv_profile, new com.squareup.picasso.Callback() {
                                    @Override
                                    public void onSuccess() {
                                        progressDialog.dismiss();
                                       // imageProgress.setVisibility(View.GONE);
                                       // MainActivity main= (MainActivity) contexts;
                                        MainDrawerActivity.updateProfileImage(getid,MainDrawerActivity.imageView_admin);
                                       // main.updateProfilePic();
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                    }

                                });

                    }
                    else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getActivity(), "Something went wrong please try again", Toast.LENGTH_SHORT).show(); }
            }
            @Override
            public void onFailure(Call<ResponseUpdateProfilePic> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(),t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

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

}