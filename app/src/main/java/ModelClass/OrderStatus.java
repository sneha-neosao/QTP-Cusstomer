package ModelClass;

public class OrderStatus {
    String statusName, statusId;

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public OrderStatus(String statusName, String statusId){
        this.statusName=statusName;
        this.statusId=statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
