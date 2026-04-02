package ModelClass;

public class NotifyModelUser {

    private String status;
    private String message;
    private NotifyUserDatapart data;

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

    public NotifyUserDatapart getData() {
        return data;
    }

    public void setData(NotifyUserDatapart data) {
        this.data = data;
    }
}
