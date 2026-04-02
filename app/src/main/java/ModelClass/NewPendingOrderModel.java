package ModelClass;

import java.io.Serializable;
import java.util.ArrayList;

public class NewPendingOrderModel implements Serializable {

    String order_status;
    String order_status_code;
    String delivery_date;
    String time_slot;
    String payment_method;
    String payment_status;
    String paid_by_wallet;
    String cart_id;
    String price;
    String del_charge;
    String remaining_amount;
    String coupon_discount;
    String dboy_name;
    String dboy_phone;
    String itemCount;
    String itemDiscount,tax,shipping,total,discount,grandtotal,subTotal;
    ArrayList<NewPendingDataModel> data;

    public String getItemCount() {
        return itemCount;
    }

    public void setItemCount(String itemCount) {
        this.itemCount = itemCount;
    }

    public void setItemDiscount(String itemDiscount) {
        this.itemDiscount = itemDiscount;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public void setGrandtotal(String grandtotal) {
        this.grandtotal = grandtotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public String getItemDiscount() {
        return itemDiscount;
    }

    public String getTax() {
        return tax;
    }

    public String getShipping() {
        return shipping;
    }

    public String getTotal() {
        return total;
    }

    public String getDiscount() {
        return discount;
    }

    public String getGrandtotal() {
        return grandtotal;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getOrder_status_code() {
        return order_status_code;
    }

    public void setOrder_status_code(String order_status_code) {
        this.order_status_code = order_status_code;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getTime_slot() {
        return time_slot;
    }

    public void setTime_slot(String time_slot) {
        this.time_slot = time_slot;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        if (payment_status == null) {
            this.payment_status = "";
        } else {
            this.payment_status = payment_status;
        }

    }

    public String getPaid_by_wallet() {
        return paid_by_wallet;
    }

    public void setPaid_by_wallet(String paid_by_wallet) {
        this.paid_by_wallet = paid_by_wallet;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDel_charge() {
        return del_charge;
    }

    public void setDel_charge(String del_charge) {
        this.del_charge = del_charge;
    }

    public ArrayList<NewPendingDataModel> getData() {
        return data;
    }

    public void setData(ArrayList<NewPendingDataModel> data) {
        this.data = data;
    }

    public String getRemaining_amount() {
        return remaining_amount;
    }

    public void setRemaining_amount(String remaining_amount) {
        this.remaining_amount = remaining_amount;
    }

    public String getCoupon_discount() {
        return coupon_discount;
    }

    public void setCoupon_discount(String coupon_discount) {
        this.coupon_discount = coupon_discount;
    }

    public String getDboy_name() {
        if (dboy_name == null) {
            return "";
        }
        return dboy_name;
    }

    public void setDboy_name(String dboy_name) {
        this.dboy_name = dboy_name;
    }

    public String getDboy_phone() {
        if (dboy_phone == null) {
            return "";
        }
        return dboy_phone;
    }

    public void setDboy_phone(String dboy_phone) {
        this.dboy_phone = dboy_phone;
    }
}
