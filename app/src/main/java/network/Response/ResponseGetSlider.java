package network.Response;

import java.util.ArrayList;

public class ResponseGetSlider {
    private boolean status;
    private String message;
    private ArrayList<SliderResult> result;

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

    public ArrayList<SliderResult> getSliderResult() {
        return result;
    }

    public void setSliderResult(ArrayList<SliderResult> result) {
        this.result = result;
    }


    public class SliderResult {

        String banner_id;
        String banner_name;
        String banner_image;
        String mediaType;

        public String getBanner_id() {
            return banner_id;
        }

        public void setBanner_id(String banner_id) {
            this.banner_id = banner_id;
        }

        public String getBanner_name() {
            return banner_name;
        }

        public void setBanner_name(String banner_name) {
            this.banner_name = banner_name;
        }

        public String getBanner_image() {
            return banner_image;
        }

        public void setBanner_image(String banner_image) {
            this.banner_image = banner_image;
        }

        public String getMediaType() {
            return mediaType;
        }

        public void setMediaType(String mediaType) {
            this.mediaType = mediaType;
        }





    }
}
