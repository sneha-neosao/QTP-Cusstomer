package ModelClass;

public class NewCategoryShowList {

    private String productId;
    private String product_name;
    private String product_image;
    private NewCategoryVarientList list;

    public NewCategoryShowList(String productId, String product_name, String product_image, NewCategoryVarientList list) {
        this.productId = productId;
        this.product_name = product_name;
        this.product_image = product_image;
        this.list = list;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public NewCategoryVarientList getList() {
        return list;
    }

    public void setList(NewCategoryVarientList list) {
        this.list = list;
    }
}
