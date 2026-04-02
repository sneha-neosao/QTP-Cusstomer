package ModelClass;

import java.util.ArrayList;

public class CategoryProductNewModel {

    private String status;
    private String message;
    private ArrayList<NewCategoryDataModel> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<NewCategoryDataModel> getData() {
        return data;
    }

    public void setData(ArrayList<NewCategoryDataModel> data) {
        this.data = data;
    }
}
