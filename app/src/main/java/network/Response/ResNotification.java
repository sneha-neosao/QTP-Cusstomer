package network.Response;

import ModelClass.NotificationModel;

import java.util.ArrayList;

public class ResNotification
{

    private boolean status;
    private String message,totalRecords;

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

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    private Result result;

    public String getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(String totalRecords) {
        this.totalRecords = totalRecords;
    }

    public class Result{
    private ArrayList<NotificationModel> notificationList;

        public ArrayList<NotificationModel> getNotificationList() {
            return notificationList;
        }

        public void setNotificationList(ArrayList<NotificationModel> notificationList) {
            this.notificationList = notificationList;
        }
    }

}
