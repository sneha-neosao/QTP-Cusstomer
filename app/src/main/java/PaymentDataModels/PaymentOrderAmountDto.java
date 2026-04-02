package PaymentDataModels;

public class PaymentOrderAmountDto{
    private double value;
    private String currencyCode;


    public PaymentOrderAmountDto(double value, String currencyCode) {
        this.value = value;
        this.currencyCode = currencyCode;

    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }


}
