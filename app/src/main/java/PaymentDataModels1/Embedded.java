
package PaymentDataModels1;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Embedded {

    @SerializedName("payment")
    @Expose
    private List<Payment__1> payment = null;

    public List<Payment__1> getPayment() {
        return payment;
    }

    public void setPayment(List<Payment__1> payment) {
        this.payment = payment;
    }

}
