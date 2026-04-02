package ModelClass;

import java.io.Serializable;

public class NewPendingDataModel implements Serializable {
    String store_order_id;
    String store_approval;
    String product_name;
    String varient_image;
    String qty;
    String description;
    String unit;
    String quantity;
    String order_cart_id;
    String order_date;
    String price;
    String total_mrp;
    String varient_id;
    String perUnitPrice;
    String supplierID;
    String itemDiscount,tax,shipping,total,discount,grandtotal;


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

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getVarient_image() {
        return varient_image;
    }

    public void setVarient_image(String varient_image) {
        this.varient_image = varient_image;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getDescription() {
        if (description == null) {
            return "";
        }
        return description;
    }

    public void setDescription(String description) {
        if (description == null) {
            this.description = "";
        } else {
            this.description = description;
        }
    }

    public String getPerUnitPrice() {
        return perUnitPrice;
    }

    public void setPerUnitPrice(String perUnitPrice) {
        this.perUnitPrice = perUnitPrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getOrder_cart_id() {
        return order_cart_id;
    }

    public void setOrder_cart_id(String order_cart_id) {
        this.order_cart_id = order_cart_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStore_order_id() {
        return store_order_id;
    }

    public void setStore_order_id(String store_order_id) {
        this.store_order_id = store_order_id;
    }

    public String getStore_approval() {
        return store_approval;
    }

    public void setStore_approval(String store_approval) {
        this.store_approval = store_approval;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getTotal_mrp() {
        return total_mrp;
    }

    public void setTotal_mrp(String total_mrp) {
        this.total_mrp = total_mrp;
    }

    public String getVarient_id() {
        return varient_id;
    }

    public void setVarient_id(String varient_id) {
        this.varient_id = varient_id;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }
}
