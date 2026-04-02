package ModelClass;

import java.io.Serializable;

public class NewCartModel implements Serializable {

    private String store_id;
    private String vatRate;
    private String mainSupplier;
    private String supplierID;
    private String stock;
    private String varient_id;
    private String product_id;
    private String product_name;
    private String product_image;
    private String description;
    private String varient_image;
    private String unit;
    private String quantity;
    private String count;
    private String valid_to;
    private String valid_from;
    private String timediff;
    private String hoursmin;
    private String discount;
    private String feedback;
    private String itemSellingprice;
    //private Double itemSellingprice;
    private String fixedPrice;

    private String unitID,uomId,adminRating,stockingType,customerRating,ratingUserCount,productLabel,
            categoryID,itemSubCategory;

    public String getUnitID() {
        return unitID;
    }

    public void setUnitID(String unitID) {
        this.unitID = unitID;
    }

    public String getUomId() {
        return uomId;
    }

    public void setUomId(String uomId) {
        this.uomId = uomId;
    }

    public String getAdminRating() {
        return adminRating;
    }

    public void setAdminRating(String adminRating) {
        this.adminRating = adminRating;
    }

    public String getStockingType() {
        return stockingType;
    }

    public void setStockingType(String stockingType) {
        this.stockingType = stockingType;
    }

    public String getCustomerRating() {
        return customerRating;
    }

    public void setCustomerRating(String customerRating) {
        this.customerRating = customerRating;
    }

    public String getRatingUserCount() {
        return ratingUserCount;
    }

    public void setRatingUserCount(String ratingUserCount) {
        this.ratingUserCount = ratingUserCount;
    }

    public String getProductLabel() {
        return productLabel;
    }

    public void setProductLabel(String productLabel) {
        this.productLabel = productLabel;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getItemSubCategory() {
        return itemSubCategory;
    }

    public void setItemSubCategory(String itemSubCategory) {
        this.itemSubCategory = itemSubCategory;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
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

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getValid_to() {
        return valid_to;
    }

    public void setValid_to(String valid_to) {
        this.valid_to = valid_to;
    }

    public String getValid_from() {
        return valid_from;
    }

    public void setValid_from(String valid_from) {
        this.valid_from = valid_from;
    }

    public String getTimediff() {
        return timediff;
    }

    public void setTimediff(String timediff) {
        this.timediff = timediff;
    }

    public String getHoursmin() {
        return hoursmin;
    }

    public void setHoursmin(String hoursmin) {
        this.hoursmin = hoursmin;
    }

    public String getItemSellingprice() {
        return itemSellingprice;
    }

    public void setItemSellingprice(String itemSellingprice) {
        this.itemSellingprice = itemSellingprice;
    }

    /*public Double getItemSellingprice() {
        return itemSellingprice;
    }

    public void setItemSellingprice(Double itemSellingprice) {
        this.itemSellingprice = itemSellingprice;
    }*/

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public String getMainSupplier() {
        return mainSupplier;
    }

    public void setMainSupplier(String mainSupplier) {
        this.mainSupplier = mainSupplier;
    }

    public String getVatRate() {
        return vatRate;
    }

    public void setVatRate(String vatRate) {
        this.vatRate = vatRate;
    }

    public String getFixedPrice() {
        return fixedPrice;
    }

    public void setFixedPrice(String fixedPrice) {
        this.fixedPrice = fixedPrice;
    }
}
