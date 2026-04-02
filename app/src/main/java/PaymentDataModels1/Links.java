
package PaymentDataModels1;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Links {

    @SerializedName("cancel")
    @Expose
    private Cancel cancel;
    @SerializedName("cnp:payment-link")
    @Expose
    private CnpPaymentLink cnpPaymentLink;
    @SerializedName("payment-authorization")
    @Expose
    private PaymentAuthorization paymentAuthorization;
    @SerializedName("self")
    @Expose
    private Self self;
    @SerializedName("tenant-brand")
    @Expose
    private TenantBrand tenantBrand;
    @SerializedName("payment")
    @Expose
    private Payment payment;
    @SerializedName("merchant-brand")
    @Expose
    private MerchantBrand merchantBrand;

    public Cancel getCancel() {
        return cancel;
    }

    public void setCancel(Cancel cancel) {
        this.cancel = cancel;
    }

    public CnpPaymentLink getCnpPaymentLink() {
        return cnpPaymentLink;
    }

    public void setCnpPaymentLink(CnpPaymentLink cnpPaymentLink) {
        this.cnpPaymentLink = cnpPaymentLink;
    }

    public PaymentAuthorization getPaymentAuthorization() {
        return paymentAuthorization;
    }

    public void setPaymentAuthorization(PaymentAuthorization paymentAuthorization) {
        this.paymentAuthorization = paymentAuthorization;
    }

    public Self getSelf() {
        return self;
    }

    public void setSelf(Self self) {
        this.self = self;
    }

    public TenantBrand getTenantBrand() {
        return tenantBrand;
    }

    public void setTenantBrand(TenantBrand tenantBrand) {
        this.tenantBrand = tenantBrand;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public MerchantBrand getMerchantBrand() {
        return merchantBrand;
    }

    public void setMerchantBrand(MerchantBrand merchantBrand) {
        this.merchantBrand = merchantBrand;
    }

}
