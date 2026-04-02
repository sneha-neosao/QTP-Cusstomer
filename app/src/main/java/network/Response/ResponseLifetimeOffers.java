package network.Response;

import ModelClass.LifetimeOffer;

import java.util.ArrayList;

public class ResponseLifetimeOffers {

    public boolean status;
    public String message;

    public ArrayList<LifetimeOffer> result;

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

    public ArrayList<LifetimeOffer> getResult() {
        return result;
    }

    public void setResult(ArrayList<LifetimeOffer> result) {
        this.result = result;
    }
}
