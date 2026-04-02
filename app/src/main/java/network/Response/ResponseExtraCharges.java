package network.Response;

import ModelClass.VatResult;

import java.util.ArrayList;

public class ResponseExtraCharges {
    private String status,message;
    private ArrayList<VatResult> vatResult;

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

    public ArrayList<VatResult> getVatResult() {
        return vatResult;
    }

    public void setVatResult(ArrayList<VatResult> vatResult) {
        this.vatResult = vatResult;
    }
}
