package network.Response;

import java.util.ArrayList;

public class ResponseGetAllSubOfSubCategories {
    private boolean status;
    private String message;
    private ArrayList<AllSubOfSubCategories> result;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<AllSubOfSubCategories> getAllSubOfSubCategories() {
        return result;
    }

    public void setAllSubOfSubCategories(ArrayList<AllSubOfSubCategories> result) {
        this.result = result;
    }


    public static class AllSubOfSubCategories {

        String item_subCatID,
                item_subCatName,
                categoryId,
                subOfSubCategoryId,
                image;

        public String getItem_subCatID() {
            return item_subCatID;
        }

        public void setItem_subCatID(String item_subCatID) {
            this.item_subCatID = item_subCatID;
        }

        public String getItem_subCatName() {
            return item_subCatName;
        }

        public void setItem_subCatName(String item_subCatName) {
            this.item_subCatName = item_subCatName;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getSubOfSubCategoryId() {
            return subOfSubCategoryId;
        }

        public void setSubOfSubCategoryId(String subOfSubCategoryId) {
            this.subOfSubCategoryId = subOfSubCategoryId;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
