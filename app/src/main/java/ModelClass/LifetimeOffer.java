package ModelClass;

public class LifetimeOffer {

    public String cmid;
    public String cmCode;
    public String cmName;
    public String cmDescription;
    public String startDate;
    public String endDate;

    public String getcDescription() {
        return cDescription;
    }

    public void setcDescription(String cDescription) {
        this.cDescription = cDescription;
    }

    public String cDescription;

    public double getShippingCharges() {
        return shippingCharges;
    }

    public void setShippingCharges(double shippingCharges) {
        this.shippingCharges = shippingCharges;
    }

    public double shippingCharges;
    public String minimumPurchaseAmount, limitNumberOfUses, limitExisitingUserUses;
    public String createDate, createdBy, vStatus, branchID, imagePath, cdid;
    public String discountType, discountValue, ctDescription;

    public String getCmid() {
        return cmid;
    }

    public void setCmid(String cmid) {
        this.cmid = cmid;
    }

    public String getCmCode() {
        return cmCode;
    }

    public void setCmCode(String cmCode) {
        this.cmCode = cmCode;
    }

    public String getCmName() {
        return cmName;
    }

    public void setCmName(String cmName) {
        this.cmName = cmName;
    }

    public String getCmDescription() {
        return cmDescription;
    }

    public void setCmDescription(String cmDescription) {
        this.cmDescription = cmDescription;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getMinimumPurchaseAmount() {
        return minimumPurchaseAmount;
    }

    public void setMinimumPurchaseAmount(String minimumPurchaseAmount) {
        this.minimumPurchaseAmount = minimumPurchaseAmount;
    }

    public String getLimitNumberOfUses() {
        return limitNumberOfUses;
    }

    public void setLimitNumberOfUses(String limitNumberOfUses) {
        this.limitNumberOfUses = limitNumberOfUses;
    }

    public String getLimitExisitingUserUses() {
        return limitExisitingUserUses;
    }

    public void setLimitExisitingUserUses(String limitExisitingUserUses) {
        this.limitExisitingUserUses = limitExisitingUserUses;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getvStatus() {
        return vStatus;
    }

    public void setvStatus(String vStatus) {
        this.vStatus = vStatus;
    }

    public String getBranchID() {
        return branchID;
    }

    public void setBranchID(String branchID) {
        this.branchID = branchID;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCdid() {
        return cdid;
    }

    public void setCdid(String cdid) {
        this.cdid = cdid;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(String discountValue) {
        this.discountValue = discountValue;
    }

    public String getCtDescription() {
        return ctDescription;
    }

    public void setCtDescription(String ctDescription) {
        this.ctDescription = ctDescription;
    }
}
