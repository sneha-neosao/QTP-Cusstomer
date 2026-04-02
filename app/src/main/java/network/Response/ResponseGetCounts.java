package network.Response;

public class ResponseGetCounts
{
    private boolean status;
     private String message;



    private CountResult result;

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
    public CountResult getResult() {
        return result;
    }

    public void setResult(CountResult result) {
        this.result = result;
    }

    public static class CountResult {
        int notificationCount;
        int voucharCount;
        int favouriteCount;
        int recentCount;
        int reelsCount;

        public int getNotificationCount() {
            return notificationCount;
        }

        public void setNotificationCount(int notificationCount) {
            this.notificationCount = notificationCount;
        }

        public int getVoucharCount() {
            return voucharCount;
        }

        public void setVoucharCount(int voucharCount) {
            this.voucharCount = voucharCount;
        }

        public int getFavouriteCount() {
            return favouriteCount;
        }

        public void setFavouriteCount(int favouriteCount) {
            this.favouriteCount = favouriteCount;
        }

        public int getRecentCount() {
            return recentCount;
        }

        public void setRecentCount(int recentCount) {
            this.recentCount = recentCount;
        }

        public int getReelCount() {
            return reelsCount;
        }

        public void setReelCount(int reelsCount) {
            this.reelsCount = reelsCount;
        }
    }

}
