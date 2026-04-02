package ModelClass;

public class NewOrderItem
{

    private String itemId;

    public String getUnitID() {
        return unitID;
    }

    public void setUnitID(String unitID) {
        this.unitID = unitID;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    private String unitID;
    private String itemName;
    private String uom;
    private String orderItemId;
    private String orderId;
    private String itemSellingprice;
    private String perUnitPrice;
    private String quantity;
    private String image;
    private String vatRate;
    private String stockingType;
    private String orderItemStatus;
    private int rating=0;
    private int ratingApproval;




    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getItemSellingprice() {
        return itemSellingprice;
    }

    public void setItemSellingprice(String itemSellingprice) {
        this.itemSellingprice = itemSellingprice;
    }

    public String getPerUnitPrice() {
        return perUnitPrice;
    }

    public void setPerUnitPrice(String perUnitPrice) {
        this.perUnitPrice = perUnitPrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVatRate() {
        return vatRate;
    }

    public void setVatRate(String vatRate) {
        this.vatRate = vatRate;
    }

    public String getStockingType() {
        return stockingType;
    }

    public void setStockingType(String stockingType) {
        this.stockingType = stockingType;
    }

    public String getOrderItemStatus() {
        return orderItemStatus;
    }

    public void setOrderItemStatus(String orderItemStatus) {
        this.orderItemStatus = orderItemStatus;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getRatingApproval() {
        return ratingApproval;
    }

    public void setRatingApproval(int ratingApproval) {
        this.ratingApproval = ratingApproval;
    }
}
