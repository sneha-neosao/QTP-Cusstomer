package PaymentDataModels;

public class ResponseOrderModel {
    private String message;
    private String orderID;
    private boolean status;
    private CreateOrderResponseDto orderResponse;

    public boolean getStatus() {
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

    public CreateOrderResponseDto getOrderResponse() {
        return orderResponse;
    }

    public void setOrderResponse(CreateOrderResponseDto orderResponse) {
        this.orderResponse = orderResponse;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }
}
