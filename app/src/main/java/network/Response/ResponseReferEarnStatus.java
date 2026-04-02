package network.Response;

import ModelClass.ResultReferEarn;

import java.util.ArrayList;

public class ResponseReferEarnStatus {

    String status, message;
    ArrayList<ResultReferEarn> result;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<ResultReferEarn> getResult() {
        return result;
    }

    public void setResult(ArrayList<ResultReferEarn> result) {
        this.result = result;
    }
}