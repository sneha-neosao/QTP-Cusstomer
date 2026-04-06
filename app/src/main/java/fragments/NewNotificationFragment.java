package fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import activities.MainDrawerActivity;
import adapters.NotificationAdapter;
import ModelClass.NotificationModel;
import ModelClass.ProductDetailModel;
import com.grocery.QTPmart.R;
import network.Response.ResNotification;
import network.Response.ResProductDetail;
import network.ServiceGenrator;
import util.Session_management;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.VISIBLE;

public class NewNotificationFragment extends Fragment {

    RecyclerView rv_notification;
    TextView txt_noti_count,txt_no_data;
    ArrayList<NotificationModel> notificationModels=new ArrayList<>();
    private Session_management sessionManagement;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_new_notification, container, false);
        sessionManagement = new Session_management(getContext());
        String user_id = sessionManagement.userId();
        txt_noti_count=view.findViewById(R.id.txt_noti_count);
        txt_no_data=view.findViewById(R.id.txt_no_data);
        rv_notification=view.findViewById(R.id.rv_notification);

        MainDrawerActivity.reelLyt.setVisibility(VISIBLE);
        MainDrawerActivity.notification_iv.setVisibility(VISIBLE);
        MainDrawerActivity.search_iv.setVisibility(VISIBLE);
        MainDrawerActivity.ll_nav_title.setVisibility(View.GONE);
        MainDrawerActivity.tvTitle.setVisibility(View.GONE);

        getNotification(user_id);

        return view;
    }


    public void getNotification(String userID){
        ServiceGenrator.getApiInterface().getNotifications(userID).enqueue(
                new Callback<ResNotification>() {
                    @Override
                    public void onResponse(Call<ResNotification> call, Response<ResNotification> response) {

                        if (response.isSuccessful()) {

                            if (response.body().isStatus())
                            {
                                if(response.body().getResult().getNotificationList()!=null) {
                                    MainDrawerActivity.tvNotificationCount.setText(String.valueOf(response.body().getResult().getNotificationList().size()));
                                    txt_noti_count.setText(response.body().getTotalRecords());
                                    notificationModels = response.body().getResult().getNotificationList();
                                    NotificationAdapter notificationAdapter = new NotificationAdapter(getContext(), notificationModels);
                                    rv_notification.setAdapter(notificationAdapter);
                                }
                            }
                            else {
                                txt_noti_count.setText("0");
                                rv_notification.setVisibility(View.GONE);
                                txt_no_data.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResNotification> call, Throwable t) {

                    }
                });

    }

}