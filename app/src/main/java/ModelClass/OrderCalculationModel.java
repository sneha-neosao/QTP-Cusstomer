package ModelClass;

public class OrderCalculationModel {
    Double subTotal, discountInPercentage, discountInAmount, total, vatRate, vatTotal, deliveryCharges, grandTotal;



    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Double getDiscountInPercentage() {
        return discountInPercentage;
    }

    public void setDiscountInPercentage(Double discountInPercentage) {
        this.discountInPercentage = discountInPercentage;
    }

    public Double getDiscountInAmount() {
        return discountInAmount;
    }

    public void setDiscountInAmount(Double discountInAmount) {
        this.discountInAmount = discountInAmount;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getVatRate() {
        return vatRate;
    }

    public void setVatRate(Double vatRate) {
        this.vatRate = vatRate;
    }

    public Double getVatTotal() {
        return vatTotal;
    }

    public void setVatTotal(Double vatTotal) {
        this.vatTotal = vatTotal;
    }

    public Double getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(Double deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }

    public Double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(Double grandTotal) {
        this.grandTotal = grandTotal;
    }
}

