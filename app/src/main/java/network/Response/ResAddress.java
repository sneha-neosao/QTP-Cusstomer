package network.Response;

import ModelClass.AddressModel;

import java.util.ArrayList;

public class ResAddress
{
    private boolean status;
     private String message;
     private ArrayList<AddressModel> result;

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

    public ArrayList<AddressModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<AddressModel> result) {
        this.result = result;
    }
}
