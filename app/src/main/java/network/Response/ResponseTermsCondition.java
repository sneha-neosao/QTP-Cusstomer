package network.Response;

import ModelClass.ResultTerms;

import java.util.ArrayList;

public class ResponseTermsCondition {
    String status, message;

    ArrayList<ResultTerms>result;
//    ResultTerms result;

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

//    public ResultTerms getResult() {
//        return result;
//    }
//
//    public void setResult(ResultTerms result) {
//        this.result = result;
//    }
//}

    public ArrayList<ResultTerms> getResult() {
        return result;
    }

    public void setResult(ArrayList<ResultTerms> result) {
        this.result = result;
    }

}
