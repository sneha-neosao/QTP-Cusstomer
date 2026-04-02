
package PaymentDataModels1;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseOrderModel1 {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("orderID")
    @Expose
    private String orderID;
    @SerializedName("orderResponse")
    @Expose
    private OrderResponse orderResponse;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public OrderResponse getOrderResponse() {
        return orderResponse;
    }

    public void setOrderResponse(OrderResponse orderResponse) {
        this.orderResponse = orderResponse;
    }

}
