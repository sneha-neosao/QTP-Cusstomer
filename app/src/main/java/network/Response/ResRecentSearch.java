package network.Response;

import ModelClass.RecentSearchModel;

import java.util.ArrayList;

public class ResRecentSearch
{
    private boolean status;
    private String message;
    private ArrayList<RecentSearchModel> result;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<RecentSearchModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<RecentSearchModel> result) {
        this.result = result;
    }
}
