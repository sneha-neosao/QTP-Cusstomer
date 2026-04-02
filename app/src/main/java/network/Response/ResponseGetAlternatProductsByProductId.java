package network.Response;
import ModelClass.LabelModel;
import ModelClass.NewCartModel;

import java.util.ArrayList;

public class ResponseGetAlternatProductsByProductId
{
    private boolean status;
     private String message;
     private ArrayList<NewCartModel> result;

    private ArrayList<LabelModel> labels;

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

    public ArrayList<NewCartModel> getResult() {
        return result;
    }

    public void setResult(ArrayList<NewCartModel> result) {
        this.result = result;
    }

    public ArrayList<LabelModel> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<LabelModel> labels) {
        this.labels = labels;
    }
}
