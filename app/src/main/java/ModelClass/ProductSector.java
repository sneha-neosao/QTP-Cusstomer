package ModelClass;

public class ProductSector
{
    String product_sector_id;
    String product_sector_name;
    String subCatId;
    String subOfSubCategoryId;

    public String getSubOfSubCategoryId() {
        return subOfSubCategoryId;
    }

    public void setSubOfSubCategoryId(String subOfSubCategoryId) {
        this.subOfSubCategoryId = subOfSubCategoryId;
    }

    public String getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(String subCatId) {
        this.subCatId = subCatId;
    }

    public String getProduct_sector_id() {
        return product_sector_id;
    }

    public void setProduct_sector_id(String product_sector_id) {
        this.product_sector_id = product_sector_id;
    }

    public String getProduct_sector_name() {
        return product_sector_name;
    }

    public void setProduct_sector_name(String product_sector_name) {
        this.product_sector_name = product_sector_name;
    }

    @Override
    public String toString() {
        return "ProductSector{" +
                "product_sector_id=" + product_sector_id +
                ", product_sector_name='" + product_sector_name + '\'' +
                '}';
    }
}
