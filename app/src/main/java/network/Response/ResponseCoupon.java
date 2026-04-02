package network.Response;

import java.util.ArrayList;

public class ResponseCoupon {

    public boolean status;
    public int nextLimit;
    public String message;
    public ArrayList<Coupon> result;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getNextLimit() {
        return nextLimit;
    }

    public void setNextLimit(int nextLimit) {
        this.nextLimit = nextLimit;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Coupon> getResult() {
        return result;
    }

    public void setResult(ArrayList<Coupon> result) {
        this.result = result;
    }
}
