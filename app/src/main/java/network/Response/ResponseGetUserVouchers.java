package network.Response;

import java.util.ArrayList;

public class ResponseGetUserVouchers {
    private boolean status;
    private String message;
    private ArrayList<VoucherResult> result;

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

    public ArrayList<VoucherResult> getVoucherResult() {
        return result;
    }

    public void setVouherResult(ArrayList<VoucherResult> result) {
        this.result = result;
    }


    public class VoucherResult {
        String faqid;


        String cmid,cmCode, cmName, cmDescription, startDate,
                endDate,
                minimumPurchaseAmount,
                limitNumberOfUse,
                limitExisitingUserUses,
                createDate,
                createdBy,
                vStatus,
                branchID,
                imagePath,
                supplierID,
                couponType,
                customerID,
                cdid,
                discountType,
                discountValue,
                ctDescription,
                shippingCharges,
                cDescription,
                decidedExisitingLimit,
                returnAmount;



        public String getFaqid() {
            return faqid;
        }

        public void setFaqid(String faqid) {
            this.faqid = faqid;
        }

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

        public String getLimitNumberOfUse() {
            return limitNumberOfUse;
        }

        public void setLimitNumberOfUse(String limitNumberOfUse) {
            this.limitNumberOfUse = limitNumberOfUse;
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

        public String getSupplierID() {
            return supplierID;
        }

        public void setSupplierID(String supplierID) {
            this.supplierID = supplierID;
        }

        public String getCouponType() {
            return couponType;
        }

        public void setCouponType(String couponType) {
            this.couponType = couponType;
        }

        public String getCustomerID() {
            return customerID;
        }

        public void setCustomerID(String customerID) {
            this.customerID = customerID;
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

        public String getShippingCharges() {
            return shippingCharges;
        }

        public void setShippingCharges(String shippingCharges) {
            this.shippingCharges = shippingCharges;
        }

        public String getcDescription() {
            return cDescription;
        }

        public void setcDescription(String cDescription) {
            this.cDescription = cDescription;
        }

        public String getDecidedExisitingLimit() {
            return decidedExisitingLimit;
        }

        public void setDecidedExisitingLimit(String decidedExisitingLimit) {
            this.decidedExisitingLimit = decidedExisitingLimit;
        }

        public String getReturnAmount() {
            return returnAmount;
        }

        public void setReturnAmount(String returnAmount) {
            this.returnAmount = returnAmount;
        }
    }
}
