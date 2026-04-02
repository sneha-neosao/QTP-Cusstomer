package network.Response;

import ModelClass.SearchSuggestionModel;

import java.util.ArrayList;

public class ResSearchSuggestion
{
    private boolean status;
    private String message;
    private ArrayList<SearchSuggestionModel> result;

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

    public ArrayList<SearchSuggestionModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<SearchSuggestionModel> result) {
        this.result = result;
    }
}
