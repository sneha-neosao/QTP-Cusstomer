
package PaymentDataModels1;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentMethods {

    @SerializedName("card")
    @Expose
    private List<String> card = null;

    public List<String> getCard() {
        return card;
    }

    public void setCard(List<String> card) {
        this.card = card;
    }

}
