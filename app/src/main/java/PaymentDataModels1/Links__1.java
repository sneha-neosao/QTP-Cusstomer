
package PaymentDataModels1;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Links__1 {

    @SerializedName("self")
    @Expose
    private Self__1 self;
    @SerializedName("payment:card")
    @Expose
    private PaymentCard paymentCard;
    @SerializedName("curies")
    @Expose
    private List<Cury> curies = null;

    public Self__1 getSelf() {
        return self;
    }

    public void setSelf(Self__1 self) {
        this.self = self;
    }

    public PaymentCard getPaymentCard() {
        return paymentCard;
    }

    public void setPaymentCard(PaymentCard paymentCard) {
        this.paymentCard = paymentCard;
    }

    public List<Cury> getCuries() {
        return curies;
    }

    public void setCuries(List<Cury> curies) {
        this.curies = curies;
    }

}
