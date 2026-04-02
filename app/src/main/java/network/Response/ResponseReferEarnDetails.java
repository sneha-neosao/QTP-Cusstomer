package network.Response;

import ModelClass.MyReferDetails;

import java.util.ArrayList;

public class ResponseReferEarnDetails
{
    private boolean status;
    private String message;
    private MyReferDetails result;
    private ReferCounts counts;

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

    public MyReferDetails getResult() {
        return result;
    }

    public void setResult(MyReferDetails result) {
        this.result = result;
    }

    public ReferCounts getCounts() {
        return counts;
    }

    public void setCounts(ReferCounts counts) {
        this.counts = counts;
    }

    public class ReferCounts{
        String signUpsCount,totalOrderCount;

        public String getSignUpsCount() {
            return signUpsCount;
        }

        public void setSignUpsCount(String signUpsCount) {
            this.signUpsCount = signUpsCount;
        }

        public String getTotalOrderCount() {
            return totalOrderCount;
        }

        public void setTotalOrderCount(String totalOrderCount) {
            this.totalOrderCount = totalOrderCount;
        }
    }
}
