package ModelClass;

import java.io.Serializable;

public class AddressModel implements Serializable
{
    private String csdid,custID,csdTypeName,cusAdd1,cusAdd2,city,state,country,zipcode,area,branchid,latitude,
            longitude,cusMob,cusEmail;

    public String getCsdid() {
        return csdid;
    }

    public void setCsdid(String csdid) {
        this.csdid = csdid;
    }

    public String getCustID() {
        return custID;
    }

    public void setCustID(String custID) {
        this.custID = custID;
    }

    public String getCsdTypeName() {
        return csdTypeName;
    }

    public void setCsdTypeName(String csdTypeName) {
        this.csdTypeName = csdTypeName;
    }

    public String getCusAdd1() {
        return cusAdd1;
    }

    public void setCusAdd1(String cusAdd1) {
        this.cusAdd1 = cusAdd1;
    }

    public String getCusAdd2() {
        return cusAdd2;
    }

    public void setCusAdd2(String cusAdd2) {
        this.cusAdd2 = cusAdd2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getBranchid() {
        return branchid;
    }

    public void setBranchid(String branchid) {
        this.branchid = branchid;
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

    public String getCusMob() {
        return cusMob;
    }

    public void setCusMob(String cusMob) {
        this.cusMob = cusMob;
    }

    public String getCusEmail() {
        return cusEmail;
    }

    public void setCusEmail(String cusEmail) {
        this.cusEmail = cusEmail;
    }
}
