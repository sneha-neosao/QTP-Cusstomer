package network.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import ModelClass.LabelModel;
import ModelClass.ProductDetailModel;

import java.util.List;

public class ResProductDetail
{

    private String status,message;
    private ProductDetailModel result;

    @SerializedName("labels")
    @Expose
    private List<LabelModel> labels = null;

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

    public ProductDetailModel getResult() {
        return result;
    }

    public void setResult(ProductDetailModel result) {
        this.result = result;
    }

    public List<LabelModel> getLabels() {
        return labels;
    }

    public void setLabels(List<LabelModel> labels) {
        this.labels = labels;
    }
}
