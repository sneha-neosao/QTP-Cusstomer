package util;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {


    public static final int PAGE_START = 1;
    @NonNull
    private LinearLayoutManager layoutManager;
    /**
     * Set scrolling threshold here (for now i'm assuming 10 item in one page)
     */
    private static final int PAGE_SIZE = 10;
    /**
     * Supporting only LinearLayoutManager for now.
     */
    public PaginationScrollListener(@NonNull LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }
    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        hideBottomNav( dy);
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
            ) {
                loadMoreItems(totalItemCount);
            }
        }
    }
    protected abstract void hideBottomNav(int dy);
    protected abstract void loadMoreItems(int totalItemCount);
    public abstract boolean isLastPage();
    public abstract boolean isLoading();

     /*LinearLayoutManager layoutManager;
     public PaginationScrollListener(LinearLayoutManager layoutManager){
         this.layoutManager = layoutManager;
     }


     public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        if (!isLoading() && !isLastPage()) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                    && firstVisibleItemPosition >= 0
            ) {
                loadMoreItems(totalItemCount);

            }
        }
    }
    protected abstract void loadMoreItems(int totalItemCount);

    public abstract Boolean isLastPage();

    public abstract Boolean isLoading();*/
}
