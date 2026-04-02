package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ModelClass.LabelModel;
import com.grocery.QTPmart.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LabelAdapter extends RecyclerView.Adapter<LabelAdapter.MyViewHolder>{

    List<LabelModel> labelModels;
    Context context;

    public LabelAdapter(List<LabelModel> labelModels,Context context){
        this.labelModels=labelModels;
        this.context=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_layout_label, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.txt_label_name.setText(labelModels.get(position).getLableText());
        Picasso.get()
                .load(labelModels.get(position).getImagePath())
                .placeholder(R.drawable.noimageavailable)
                .into(holder.img_item_label);
    }

    @Override
    public int getItemCount() {
        return labelModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView txt_label_name;
        public ImageView img_item_label;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img_item_label=itemView.findViewById(R.id.img_item_label);
            txt_label_name=itemView.findViewById(R.id.txt_label_name);

        }
    }
}
