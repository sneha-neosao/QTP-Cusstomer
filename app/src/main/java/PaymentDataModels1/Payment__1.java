
package PaymentDataModels1;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payment__1 {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("_links")
    @Expose
    private Links__1 links;
    @SerializedName("reference")
    @Expose
    private String reference;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("amount")
    @Expose
    private Amount__1 amount;
    @SerializedName("updateDateTime")
    @Expose
    private String updateDateTime;
    @SerializedName("outletId")
    @Expose
    private String outletId;
    @SerializedName("orderReference")
    @Expose
    private String orderReference;
    @SerializedName("3ds2")
    @Expose
    private PaymentDataModels1._3ds2 _3ds2;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Links__1 getLinks() {
        return links;
    }

    public void setLinks(Links__1 links) {
        this.links = links;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Amount__1 getAmount() {
        return amount;
    }

    public void setAmount(Amount__1 amount) {
        this.amount = amount;
    }

    public String getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(String updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public String getOutletId() {
        return outletId;
    }

    public void setOutletId(String outletId) {
        this.outletId = outletId;
    }

    public String getOrderReference() {
        return orderReference;
    }

    public void setOrderReference(String orderReference) {
        this.orderReference = orderReference;
    }

    public PaymentDataModels1._3ds2 get3ds2() {
        return _3ds2;
    }

    public void set3ds2(PaymentDataModels1._3ds2 _3ds2) {
        this._3ds2 = _3ds2;
    }

}
