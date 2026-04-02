
package PaymentDataModels1;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Amount__1 {

    @SerializedName("currencyCode")
    @Expose
    private String currencyCode;
    @SerializedName("value")
    @Expose
    private Integer value;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

}
