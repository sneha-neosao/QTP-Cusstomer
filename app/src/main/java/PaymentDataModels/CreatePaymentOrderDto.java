package PaymentDataModels;

import java.util.Map;

/**
 * Created by angeooo on 11-Dec-19.
 */
public class CreatePaymentOrderDto {
    private String action;
    private PaymentOrderAmountDto amount;
    private String language;
    private String description= "Furniture Store Android App";
    private Map<String, String> merchantAttributes;
    private String grandtotal,FirstName,AddressLine1,City,email,tax,Promo,discount;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public PaymentOrderAmountDto getAmount() {
        return amount;
    }

    public void setAmount(PaymentOrderAmountDto amount) {
        this.amount = amount;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, String> getMerchantAttributes() {
        return merchantAttributes;
    }

    public void setMerchantAttributes(Map<String, String> merchantAttributes) {
        this.merchantAttributes = merchantAttributes;
    }

    public String getGrandtotal() {
        return grandtotal;
    }

    public void setGrandtotal(String grandtotal) {
        this.grandtotal = grandtotal;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getAddressLine1() {
        return AddressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        AddressLine1 = addressLine1;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getPromo() {
        return Promo;
    }

    public void setPromo(String promo) {
        Promo = promo;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

}



//
//data class CreatePaymentOrderRequestDto(
//private val action: String,
//private val amount: PaymentOrderAmountDto,
//private val language: String,
//private val description: String = "Furniture Store Android App",
//private val merchantAttributes: Map<String, String> = mapOf()
//        )
//
///**
// * Gateway amount is always integer.
// * BigDecimal amount value should be mapped Int depending on currency
// *
// * £1.00    -> 100
// * $1.00    -> 100
// * AED1.00  -> 100
// *
// *  This is not the same for all currencies. Mapping should be done before sending Gateway
// */
//        data class PaymentOrderAmountDto(
//private val value: Int,
//private val currencyCode: String
//        )
