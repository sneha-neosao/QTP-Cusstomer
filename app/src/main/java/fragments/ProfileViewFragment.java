package fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import Config.ApiBaseURL;
import Config.BaseURL;
import com.grocery.QTPmart.R;
import util.Session_management;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.hbb20.CountryCodePicker;

public class ProfileViewFragment extends Fragment {

    private Session_management sessionManagement;
    TextView tv_fullName,tv_email,tv_contact,tv_dob,tv_country,tv_state,tv_city,tv_landmark,tv_area,tv_pincode,tv_gender,
            tv_marital_status,tv_blood_group,tv_street;
    ImageView iv_profile_view;
    String getId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile_view, container, false);

        sessionManagement = new Session_management(getContext());
         getId = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        tv_fullName=view.findViewById(R.id.tv_fullName);
        tv_email=view.findViewById(R.id.tv_email);
        tv_contact=view.findViewById(R.id.tv_contact);
        tv_dob=view.findViewById(R.id.tv_dob);
        tv_country=view.findViewById(R.id.tv_country);
        tv_state=view.findViewById(R.id.tv_state);
        tv_city=view.findViewById(R.id.tv_city);
        tv_landmark=view.findViewById(R.id.tv_landmark);
        tv_street=view.findViewById(R.id.tv_street);
        tv_area=view.findViewById(R.id.tv_area);
        tv_pincode=view.findViewById(R.id.tv_pincode);
        tv_gender=view.findViewById(R.id.tv_gender);
        tv_marital_status=view.findViewById(R.id.tv_marital_status);
        tv_blood_group=view.findViewById(R.id.tv_blood_group);
        LinearLayout llEditProfile=view.findViewById(R.id.llEditProfile);
        iv_profile_view=view.findViewById(R.id.iv_profile_view);
        CountryCodePicker ccp = view.findViewById(R.id.ccp);

        tv_fullName.setText(sessionManagement.getUserDetails().get(BaseURL.KEY_NAME));
        tv_email.setText(sessionManagement.getUserDetails().get(BaseURL.KEY_EMAIL));
        tv_contact.setText(sessionManagement.getUserDetails().get(BaseURL.KEY_MOBILE));
        tv_dob.setText(sessionManagement.getUserDOB().equals("null")?"":sessionManagement.getUserDOB() );
        tv_country.setText(sessionManagement.getUserCountry().equals("null")?"":sessionManagement.getUserCountry());
        tv_state.setText(sessionManagement.getUserState().equals("null")?"":sessionManagement.getUserState());
        tv_city.setText(sessionManagement.getUserCity().equals("null")?"":sessionManagement.getUserCity());
        tv_landmark.setText(sessionManagement.getUserLandmark().equals("null")?"":sessionManagement.getUserLandmark());
        tv_street.setText(sessionManagement.getUserStreet().equals("null")?"":sessionManagement.getUserStreet());
        tv_area.setText(sessionManagement.getUserDetails().get(BaseURL.ADDRESS).equals("null")?"":sessionManagement.getUserDetails().get(BaseURL.ADDRESS));
        tv_pincode.setText(sessionManagement.getUserPinCode().equals("null")?"":sessionManagement.getUserPinCode());

        String countryCode = sessionManagement.getUserCountryCode();
        if (countryCode != null && !countryCode.isEmpty()) {
            ccp.setCountryForPhoneCode(Integer.parseInt(countryCode.replace("+", "")));
        }

        Log.e("ImGUrl",ApiBaseURL.IMG_URL_NEW+"profile_" +getId+ ".png");

        /*Load Profile Image*/
        Long time = System.currentTimeMillis();
        Picasso.get()
                .load(ApiBaseURL.IMG_URL_NEW+"profile_" +getId+ ".png"+"?"+time)
                .placeholder(R.drawable.toy_face)
                .memoryPolicy(MemoryPolicy.NO_STORE, MemoryPolicy.NO_CACHE)
                .into(iv_profile_view);

        llEditProfile.setOnClickListener(view1 -> {
            Fragment profileEditNewFragment = new ProfileEditNewFragment();
            FragmentTransaction fragmentManager =getActivity().getSupportFragmentManager().beginTransaction();
            fragmentManager.replace(R.id.nav_supplier_fragment, profileEditNewFragment);
            //fragmentManager.addToBackStack(null);
            fragmentManager.commit();
        });




        return view;
    }
}