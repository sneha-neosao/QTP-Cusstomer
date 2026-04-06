package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ModelClass.NotificationModel;
import com.grocery.QTPmart.R;

import java.util.ArrayList;

public class  NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationView>
{

    Context context;
    ArrayList<NotificationModel> notificationModels;

    public NotificationAdapter( Context context,ArrayList<NotificationModel> notificationModels){
        this.context=context;
        this.notificationModels=notificationModels;
    }

    @NonNull
    @Override
    public NotificationView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout_notification, parent, false);

        return new NotificationView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationView holder, int position) {
            holder.txt_notiTitle.setText(notificationModels.get(position).getNotication_text());
            holder.txt_notiDesc.setText(notificationModels.get(position).getMsgDescription());
            holder.txt_notiDate.setText(notificationModels.get(position).getReceiveDate());

    }

    @Override
    public int getItemCount()
    {
        return notificationModels.size();
    }

    public class NotificationView extends RecyclerView.ViewHolder{

        TextView txt_notiTitle,txt_notiDesc,txt_notiDate;

        public NotificationView(@NonNull View itemView) {
            super(itemView);
            txt_notiDesc=itemView.findViewById(R.id.txt_notiDesc);
            txt_notiTitle=itemView.findViewById(R.id.txt_notiTitle);
            txt_notiDate=itemView.findViewById(R.id.txt_notiDate);
        }
    }
}
