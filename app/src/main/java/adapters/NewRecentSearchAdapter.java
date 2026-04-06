package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import ModelClass.RecentSearchModel;
import com.grocery.QTPmart.R;
import java.util.ArrayList;
import java.util.List;

public class NewRecentSearchAdapter extends RecyclerView.Adapter<NewRecentSearchAdapter.ViewHolder> {

    Context context;
    ItemOnClickListener itemOnClickListener;
    ItemOnDeleteRecentSearchClickListener itemOnDeleteRecentSearchClickListener;
    ArrayList<RecentSearchModel> arrayList ;

    public NewRecentSearchAdapter(Context context, ArrayList<RecentSearchModel> arrayList,
                                  ItemOnClickListener itemOnClickListener,
                                  ItemOnDeleteRecentSearchClickListener itemOnDeleteRecentSearchClickListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.itemOnClickListener=itemOnClickListener;
        this.itemOnDeleteRecentSearchClickListener=itemOnDeleteRecentSearchClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout_recent_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.title.setText(arrayList.get(position).getItemSearchByUserID());

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemOnClickListener.onItemClick(holder.getAdapterPosition(),arrayList);
            }
        });

        holder.cvDeleteRecentSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemOnDeleteRecentSearchClickListener.onItemDeleteRecentSearchClick(holder.getAdapterPosition(),arrayList);
            }
        });
    }

    public void removeItem(int position){
        arrayList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        CardView cvDeleteRecentSearch;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txt_search);
            cvDeleteRecentSearch = itemView.findViewById(R.id.cvDeleteRecentSearch);
        }
    }

    public interface ItemOnClickListener{
        public void onItemClick(int position,ArrayList<RecentSearchModel> recentSearchModel);
    }

    public interface ItemOnDeleteRecentSearchClickListener{
        public void onItemDeleteRecentSearchClick(int position,ArrayList<RecentSearchModel> recentSearchModel);
    }
}