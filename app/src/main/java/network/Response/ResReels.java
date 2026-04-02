package network.Response;

import ModelClass.ReelsModel;

import java.util.ArrayList;

public class ResReels
{
    private boolean status;
    private String message;
    private ArrayList<ReelsModel> result;

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

    public ArrayList<ReelsModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<ReelsModel> result) {
        this.result = result;
    }
}
