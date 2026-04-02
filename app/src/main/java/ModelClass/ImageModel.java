package ModelClass;

import java.io.Serializable;

public class ImageModel implements Serializable {

    private String imagePath;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
