package network.Response;

public class ResponseMainPopUp {

    private boolean status;
    private String message;
    private MainPopUpResult result;

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

    public MainPopUpResult getResult() {
        return result;
    }

    public void setResult(MainPopUpResult result) {
        this.result = result;
    }

    public static class MainPopUpResult {
        private String banner_name, banner_image, branchCode, deviceName,
                createDate, bStatus, referenceLink, item_subCatID, expiryDate,
                banner_Type, startDate, userId, mediaType, banner_id;

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

        public String getBranchCode() {
            return branchCode;
        }

        public void setBranchCode(String branchCode) {
            this.branchCode = branchCode;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getbStatus() {
            return bStatus;
        }

        public void setbStatus(String bStatus) {
            this.bStatus = bStatus;
        }

        public String getReferenceLink() {
            return referenceLink;
        }

        public void setReferenceLink(String referenceLink) {
            this.referenceLink = referenceLink;
        }

        public String getItem_subCatID() {
            return item_subCatID;
        }

        public void setItem_subCatID(String item_subCatID) {
            this.item_subCatID = item_subCatID;
        }

        public String getExpiryDate() {
            return expiryDate;
        }

        public void setExpiryDate(String expiryDate) {
            this.expiryDate = expiryDate;
        }

        public String getBanner_Type() {
            return banner_Type;
        }

        public void setBanner_Type(String banner_Type) {
            this.banner_Type = banner_Type;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getMediaType() {
            return mediaType;
        }

        public void setMediaType(String mediaType) {
            this.mediaType = mediaType;
        }

        public String getBanner_id() {
            return banner_id;
        }

        public void setBanner_id(String banner_id) {
            this.banner_id = banner_id;
        }
    }
}
