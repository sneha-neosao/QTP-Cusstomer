package ModelClass;

import java.io.Serializable;
import java.util.ArrayList;

public class NewCategoryDataModel implements Serializable {

    private String product_id;
    private String vatRate;
    private String mainSupplier;
    private String cat_id;
    private String product_name;
    private String product_image;
    private String varient_image;
    private String description;
    private String mrp;
    private String unit;
    private String quantity;
    private String store_id;
    private String stock;
    private String varient_id;
    private String feedback;
    private String p_id;
    private String discount;
    private String itemSellingprice;
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

    private ArrayList<NewCategoryVarientList> varients;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
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

    public ArrayList<NewCategoryVarientList> getVarients() {
        return varients;
    }

    public void setVarients(ArrayList<NewCategoryVarientList> varients) {
        this.varients = varients;
    }

    public String getVarient_image() {
        return varient_image;
    }

    public void setVarient_image(String varient_image) {
        this.varient_image = varient_image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
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

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
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

    public String getItemSellingprice() {
        return itemSellingprice;
    }

    public void setItemSellingprice(String itemSellingprice) {
        this.itemSellingprice = itemSellingprice;
    }

    public String getFixedPrice() {
        return fixedPrice;
    }

    public void setFixedPrice(String fixedPrice) {
        this.fixedPrice = fixedPrice;
    }
}
