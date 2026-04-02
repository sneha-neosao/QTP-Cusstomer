package network.Response;

import ModelClass.ItemModel;

import java.util.ArrayList;

public class RestItem {

     private boolean status;
     private String message;
    private ArrayList<ItemModel> result;

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

    public ArrayList<ItemModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<ItemModel> result) {
        this.result = result;
    }
}
