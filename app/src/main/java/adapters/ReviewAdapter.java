package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;

import ModelClass.ReviewModel;
import com.grocery.QTPmart.R;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder>
{

    Context context;
    ArrayList<ReviewModel> reviewModels;

    public ReviewAdapter (Context context,ArrayList<ReviewModel> reviewModels)
    {

        this.context=context;
        this.reviewModels=reviewModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout_review, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.txt_name.setText(reviewModels.get(position).getCustName());
            holder.txt_review_desc.setText(reviewModels.get(position).getReviewMessage());
            holder.txt_review_date.setText(reviewModels.get(position).getReviewDate());
    }

    @Override
    public int getItemCount() {
        return reviewModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

    public TextView txt_name,txt_review_date,txt_review_desc;
    public AppCompatRatingBar ratingBar_admin;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ratingBar_admin=itemView.findViewById(R.id.ratingBar_admin);
            txt_name=itemView.findViewById(R.id.txt_name);
            txt_review_date=itemView.findViewById(R.id.txt_review_date);
            txt_review_desc=itemView.findViewById(R.id.txt_review_desc);

        }
    }
}
