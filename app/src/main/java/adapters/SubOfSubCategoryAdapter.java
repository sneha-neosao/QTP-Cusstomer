package adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.grocery.QTPmart.R;
import network.Response.ResponseGetAllSubOfSubCategories;

import java.util.ArrayList;

public class SubOfSubCategoryAdapter extends RecyclerView.Adapter<SubOfSubCategoryAdapter.ViewHolder> {

    Context context;
    ItemOnClickListener itemOnClickListener;
    ArrayList<ResponseGetAllSubOfSubCategories.AllSubOfSubCategories> arrayList ;
    int tvPosition=0;
    boolean tvClick=false;

    public SubOfSubCategoryAdapter(Context context, ArrayList<ResponseGetAllSubOfSubCategories.AllSubOfSubCategories> arrayList,
                                   ItemOnClickListener itemOnClickListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.itemOnClickListener=itemOnClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sub_cat_tab, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tvSubOfSubCat.setText(arrayList.get(position).getItem_subCatName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemOnClickListener.onItemClick(holder.getAdapterPosition(),arrayList);
            }
        });

        if(position==tvPosition&&tvClick){
            holder.tvSubOfSubCat.setTextColor(context.getResources().getColor(R.color.white));
            //holder.tvSubOfSubCat.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.tvSubOfSubCat.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.aqua)));
        }else{
            holder.tvSubOfSubCat.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            //holder.tvSubOfSubCat.setBackgroundColor(context.getResources().getColor(R.color.lighter_greys));
            holder.tvSubOfSubCat.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.lighter_greys)));
        }
    }

    public void setList( ArrayList<ResponseGetAllSubOfSubCategories.AllSubOfSubCategories> allSubOfSubCategories) {
        this.arrayList.addAll(allSubOfSubCategories);
        Log.e("setList",arrayList.toString());
        notifyDataSetChanged();
    }

    public void removeItem(int position){
        arrayList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void changeTextColorAndBackground(int tvPosition,boolean tvClick){
        this.tvPosition=tvPosition;
        this.tvClick=tvClick;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubOfSubCat;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSubOfSubCat = itemView.findViewById(R.id.tvSubOfSubCat);
        }
    }

    public interface ItemOnClickListener{
        public void onItemClick(int position, ArrayList<ResponseGetAllSubOfSubCategories.AllSubOfSubCategories> items);
    }

}