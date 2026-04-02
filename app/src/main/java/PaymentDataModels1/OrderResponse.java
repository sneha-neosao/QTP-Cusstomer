
package PaymentDataModels1;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderResponse {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("_links")
    @Expose
    private Links links;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("merchantDefinedData")
    @Expose
    private MerchantDefinedData merchantDefinedData;
    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("amount")
    @Expose
    private Amount amount;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("merchantAttributes")
    @Expose
    private MerchantAttributes merchantAttributes;
    @SerializedName("emailAddress")
    @Expose
    private String emailAddress;
    @SerializedName("reference")
    @Expose
    private String reference;
    @SerializedName("outletId")
    @Expose
    private String outletId;
    @SerializedName("createDateTime")
    @Expose
    private String createDateTime;
    @SerializedName("paymentMethods")
    @Expose
    private PaymentMethods paymentMethods;
    @SerializedName("billingAddress")
    @Expose
    private BillingAddress billingAddress;
    @SerializedName("referrer")
    @Expose
    private String referrer;
    @SerializedName("formattedAmount")
    @Expose
    private String formattedAmount;
    @SerializedName("formattedOrderSummary")
    @Expose
    private FormattedOrderSummary formattedOrderSummary;
    @SerializedName("_embedded")
    @Expose
    private Embedded embedded;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MerchantDefinedData getMerchantDefinedData() {
        return merchantDefinedData;
    }

    public void setMerchantDefinedData(MerchantDefinedData merchantDefinedData) {
        this.merchantDefinedData = merchantDefinedData;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public MerchantAttributes getMerchantAttributes() {
        return merchantAttributes;
    }

    public void setMerchantAttributes(MerchantAttributes merchantAttributes) {
        this.merchantAttributes = merchantAttributes;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getOutletId() {
        return outletId;
    }

    public void setOutletId(String outletId) {
        this.outletId = outletId;
    }

    public String getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        this.createDateTime = createDateTime;
    }

    public PaymentMethods getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(PaymentMethods paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public BillingAddress getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(BillingAddress billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getFormattedAmount() {
        return formattedAmount;
    }

    public void setFormattedAmount(String formattedAmount) {
        this.formattedAmount = formattedAmount;
    }

    public FormattedOrderSummary getFormattedOrderSummary() {
        return formattedOrderSummary;
    }

    public void setFormattedOrderSummary(FormattedOrderSummary formattedOrderSummary) {
        this.formattedOrderSummary = formattedOrderSummary;
    }

    public Embedded getEmbedded() {
        return embedded;
    }

    public void setEmbedded(Embedded embedded) {
        this.embedded = embedded;
    }

}
