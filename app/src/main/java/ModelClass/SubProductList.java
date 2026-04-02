package ModelClass;

public class SubProductList {

    String varient_id;
    String product_id;
    String quantity;
    String unit;
    String mrp;
    String price;
    String description;
    String varient_image;
    String stock;

    public SubProductList(String varient_id, String product_id, String quantity, String unit, String mrp, String price, String description, String varient_image,String stock) {
        this.varient_id = varient_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.unit = unit;
        this.mrp = mrp;
        this.price = price;
        this.description = description;
        this.varient_image = varient_image;
        this.stock = stock;
    }

    public String getVarient_id() {
        return varient_id;
    }

    public void setVarient_id(String varient_id) {
        this.varient_id = varient_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVarient_image() {
        return varient_image;
    }

    public void setVarient_image(String varient_image) {
        this.varient_image = varient_image;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }
}
