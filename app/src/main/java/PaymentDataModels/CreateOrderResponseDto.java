package PaymentDataModels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by angeooo on 11-Dec-19.
 */
public class CreateOrderResponseDto {
    @SerializedName("reference")
    private String reference;
    @SerializedName("_links")
    private PaymentLinks paymentLinks;
    @SerializedName("paymentMethods")
    private PaymentMethods  paymentMethods;

    public String getReference() {
        return reference;
    }

    public PaymentLinks getPaymentLinks() {
        return paymentLinks;
    }

    public PaymentMethods getPaymentMethods() {
        return paymentMethods;
    }



}


//
//data class CreateOrderResponseDto (
//@SerializedName("reference") val reference : String,
//@SerializedName("_links") val paymentLinks : PaymentLinks,
//@SerializedName("paymentMethods") val paymentMethods : PaymentMethods
//        )
//
//        data class PaymentLinks (
//@SerializedName("payment") val payment : Href,
//@SerializedName("payment-authorization") val paymentAuthorization : Href
//        )
//
//        data class Href (
//@SerializedName("href") val href: String
//        )
//
//        data class PaymentMethods (
//@SerializedName("card") val card : List<String>
//)
//
