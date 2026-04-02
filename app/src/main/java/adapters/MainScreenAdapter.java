package adapters;

import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import activities.MainDrawerActivity;
//import com.grocery.QTPmart.Activity.ProductTabActivity;
import Constants.RecyclerTouchListener;
import ModelClass.LabelModel;
import ModelClass.MainScreenList;
import com.grocery.QTPmart.R;

import util.ItemOffsetDecoration;

import java.util.List;

import static activities.SubCategoryActivity.subcateList;

public class MainScreenAdapter extends RecyclerView.Adapter {
    private Activity context;
    private List<MainScreenList> screenLists;
    private List<LabelModel> labelModels;

    public MainScreenAdapter(Activity context, List<MainScreenList> screenLists,List<LabelModel> labelModels) {
        this.context = context;
        this.screenLists = screenLists;
        this.labelModels = labelModels;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topsell, parent, false);
                return new AllCategory(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topsell, parent, false);
                return new TopSelling(view);
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topsell, parent, false);
                return new RecentDeal(view);
            case 3:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topsell, parent, false);
                return new DealOfTheDay(view);
            case 4:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topsell, parent, false);
                return new WhatsNew(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MainScreenList mainScreenList = screenLists.get(position);



        Log.e("in", "onBind");
        Log.e("MainScreenList", mainScreenList.toString());
//        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen.item_offset1);
        switch (mainScreenList.getViewType()) {

            case "ALL":
                AllCategory topSelling1 = (AllCategory) holder;
                //LinearLayoutManager linearLayoutManagers = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
               onScrollRecycler(topSelling1.allCategory);
                GridLayoutManager all= new GridLayoutManager(context, 3);
                topSelling1.allCategory.setLayoutManager(all);

//                topSelling1.allCategory.addItemDecoration(itemDecoration);
                topSelling1.allCategory.addOnItemTouchListener(new RecyclerTouchListener(context, topSelling1.allCategory, new RecyclerTouchListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
//                        Intent intent = new Intent(context, ProductTabActivity.class);
//                        intent.putExtra("cat_id", subcateList.get(position).getId());
//                        intent.putExtra("title", subcateList.get(position).getName());
//                        intent.putExtra("main_cat", subcateList.get(position).getCategoryId());
//                        context.startActivity(intent);
                       // context.startActivityForResult(intent, 24);
                    }
                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));
                topSelling1.allCategory.setAdapter(new SubCategory_adapter( mainScreenList.getSubcateList(),topSelling1.allCategory));
                Log.e("height", topSelling1.allCategory.getHeight() + " " + topSelling1.allCategory.getMeasuredHeight());


                break;

            case "TOP SELLING":
                TopSelling topSelling = (TopSelling) holder;
                onScrollRecycler(topSelling.topSelling);
                //LinearLayoutManager linearLayoutManagers = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                GridLayoutManager topselling= new GridLayoutManager(context, 2);
                topSelling.topSelling.setLayoutManager(topselling);
//                topSelling.topSelling.addItemDecoration(itemDecoration);
//                topSelling.topSelling.setAdapter(new CartAdapter(context, mainScreenList.getTopSelling(),topSelling.topSelling,labelModels));
                Log.e("height", topSelling.topSelling.getHeight() + " " + topSelling.topSelling.getMeasuredHeight());
                break;
            case "RECENT SEARCHES":
                RecentDeal recentDeal = (RecentDeal) holder;
                onScrollRecycler(recentDeal.topSelling);
                //DealOfTheDay recentDeal = (DealOfTheDay) holder;
                GridLayoutManager toprecent= new GridLayoutManager(context, 2);
               // LinearLayoutManager linearLayoutManagerss = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                recentDeal.topSelling.setLayoutManager(toprecent);
//                recentDeal.topSelling.addItemDecoration(itemDecoration);
//                recentDeal.topSelling.setAdapter(new CartAdapter(context, mainScreenList.getRecentSelling(),recentDeal.topSelling,labelModels));
                break;
            case "DEALS OF THE DAY":
                DealOfTheDay dealOfTheDay = (DealOfTheDay) holder;
                onScrollRecycler(dealOfTheDay.topSelling);
                GridLayoutManager dealofter= new GridLayoutManager(context, 2);
               // LinearLayoutManager dealofter = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                dealOfTheDay.topSelling.setLayoutManager(dealofter);
//                dealOfTheDay.topSelling.addItemDecoration(itemDecoration);
//                dealOfTheDay.topSelling.setAdapter(new CartAdapter(context, mainScreenList.getDealoftheday(),dealOfTheDay.topSelling,labelModels));
                break;
            case "WHAT'S NEW":
                WhatsNew whatsNew = (WhatsNew) holder;
                whatsNew.topSelling.addOnScrollListener(new RecyclerView.OnScrollListener() {

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if (dy > 0) {
                            // Scrolling up
                            Log.e("Scroll","Scrolling up");
//                            MainDrawerActivity.bottomNavigation.setVisibility(View.GONE);/
                        } else {
                            // Scrolling down
                            Log.e("Scroll","Scrolling down");
//                            MainDrawerActivity.bottomNavigation.setVisibility(VISIBLE);
                        }
                    }

                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);

                        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                            // Do something
                        } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                            // Do something
                        } else {
                            // Do something
                        }
                    }
                });
                //onScrollRecycler(whatsNew.topSelling);
                GridLayoutManager whatlsnewGrid= new GridLayoutManager(context, 2);
             //   LinearLayoutManager linearLayoutManagersss = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                whatsNew.topSelling.setLayoutManager(whatlsnewGrid);
//                whatsNew.topSelling.addItemDecoration(itemDecoration);
//                whatsNew.topSelling.setAdapter(new CartAdapter(context, mainScreenList.getWhatsNew(),whatsNew.topSelling,labelModels));
                break;
            default:
                break;
        }

    }

    public void onScrollRecycler(RecyclerView recyclerView){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    // Scrolling up
                    Log.e("Scroll","Scrolling up");
                    MainDrawerActivity.bottomNavigation.setVisibility(View.GONE);
                } else {
                    // Scrolling down
                    Log.e("Scroll","Scrolling down");
                    MainDrawerActivity.bottomNavigation.setVisibility(VISIBLE);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    // Do something
                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    // Do something
                } else {
                    // Do something
                }
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemViewType(int position) {
        switch (screenLists.get(position).getViewType()) {
            case "ALL":
                return 0;
            case "TOP SELLING":
                return 1;
            case "RECENT SEARCHES":
                return 2;
            case "DEALS OF THE DAY":
                return 3;
            case "WHAT'S NEW":
                return 4;
            default:
                return super.getItemViewType(position);
        }

    }

    @Override
    public int getItemCount() {
        return screenLists.size();
    }

    public class TopSelling extends RecyclerView.ViewHolder {
        public RecyclerView topSelling;

        public TopSelling(@NonNull View itemView) {
            super(itemView);
            topSelling = itemView.findViewById(R.id.top_selling);
        }
    }

    public class AllCategory extends RecyclerView.ViewHolder {
        public RecyclerView allCategory;

        public AllCategory(@NonNull View itemView) {
            super(itemView);
            allCategory = itemView.findViewById(R.id.top_selling);
        }
    }

    public class WhatsNew extends RecyclerView.ViewHolder {
        public RecyclerView topSelling;

        public WhatsNew(@NonNull View itemView) {
            super(itemView);
            topSelling = itemView.findViewById(R.id.top_selling);
        }
    }

    public class RecentDeal extends RecyclerView.ViewHolder {
        public RecyclerView topSelling;

        public RecentDeal(@NonNull View itemView) {
            super(itemView);
            topSelling = itemView.findViewById(R.id.top_selling);
        }
    }

    public class DealOfTheDay extends RecyclerView.ViewHolder {
        public RecyclerView topSelling;

        public DealOfTheDay(@NonNull View itemView) {
            super(itemView);
            topSelling = itemView.findViewById(R.id.top_selling);
        }
    }

}
