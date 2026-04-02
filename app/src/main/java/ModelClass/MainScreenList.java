package ModelClass;

import java.util.ArrayList;
import java.util.List;

public class MainScreenList {

    private String viewType;
    private List<NewCartModel> topSelling;
    private List<NewCartModel> recentSelling;
    private List<NewCartModel> dealoftheday;
    private List<NewCartModel> whatsNew;
    private  List<HomeCate> subcateList;
    private  List<LabelModel> labelModelArrayList = new ArrayList<>();;

    public MainScreenList(String viewType, List<NewCartModel> topSelling,
                          List<NewCartModel> recentSelling, List<NewCartModel> dealoftheday,
                          List<NewCartModel> whatsNew, List<HomeCate> subcateList,List<LabelModel> labelModelArrayList) {
        this.viewType = viewType;
        this.topSelling = topSelling;
        this.recentSelling = recentSelling;
        this.dealoftheday = dealoftheday;
        this.whatsNew = whatsNew;
        this.subcateList = subcateList;
        this.labelModelArrayList = labelModelArrayList;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public List<NewCartModel> getTopSelling() {
        return topSelling;
    }

    public void setTopSelling(List<NewCartModel> topSelling) {
        this.topSelling = topSelling;
    }

    public List<NewCartModel> getRecentSelling() {
        return recentSelling;
    }

    public void setRecentSelling(List<NewCartModel> recentSelling) {
        this.recentSelling = recentSelling;
    }

    public List<NewCartModel> getDealoftheday() {
        return dealoftheday;
    }

    public void setDealoftheday(List<NewCartModel> dealoftheday) {
        this.dealoftheday = dealoftheday;
    }

    public List<NewCartModel> getWhatsNew() {
        return whatsNew;
    }

    public void setWhatsNew(List<NewCartModel> whatsNew) {
        this.whatsNew = whatsNew;
    }

    public List<HomeCate> getSubcateList() {
        return subcateList;
    }

    public void setSubcateList(List<HomeCate> subcateList) {
        this.subcateList = subcateList;
    }
}
