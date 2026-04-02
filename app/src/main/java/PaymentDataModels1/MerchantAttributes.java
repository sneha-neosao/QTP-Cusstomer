
package PaymentDataModels1;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MerchantAttributes {

    @SerializedName("cancelUrl")
    @Expose
    private String cancelUrl;
    @SerializedName("redirectUrl")
    @Expose
    private String redirectUrl;
    @SerializedName("maskPaymentInfo")
    @Expose
    private String maskPaymentInfo;
    @SerializedName("skipConfirmationPage")
    @Expose
    private String skipConfirmationPage;

    public String getCancelUrl() {
        return cancelUrl;
    }

    public void setCancelUrl(String cancelUrl) {
        this.cancelUrl = cancelUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getMaskPaymentInfo() {
        return maskPaymentInfo;
    }

    public void setMaskPaymentInfo(String maskPaymentInfo) {
        this.maskPaymentInfo = maskPaymentInfo;
    }

    public String getSkipConfirmationPage() {
        return skipConfirmationPage;
    }

    public void setSkipConfirmationPage(String skipConfirmationPage) {
        this.skipConfirmationPage = skipConfirmationPage;
    }

}
