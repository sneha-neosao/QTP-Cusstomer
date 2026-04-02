package ModelClass;

public class DeliveryManDetails {

    private  String id;
    private  String userName;
    private  String latitude;
    private  String longitude;
    private  String firstName;
    private  String mobile;

    public DeliveryManDetails(String id, String userName, String latitude, String longitude, String firstName, String mobile) {
        this.id = id;
        this.userName = userName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.firstName = firstName;
        this.mobile = mobile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }



}


