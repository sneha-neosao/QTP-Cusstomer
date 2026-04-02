package network.Response;

import ModelClass.ReviewModel;

import java.util.ArrayList;

public class ResReviews
{

    private boolean status;
    private String message;
    private ArrayList<ReviewModel> reviewList;
    private Result result;

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

    public ArrayList<ReviewModel> getReviewList() {
        return reviewList;
    }

    public void setReviewList(ArrayList<ReviewModel> reviewList) {
        this.reviewList = reviewList;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result{
        private String totalRating,ratings,reviews,poor,average,good,veryGood,excellent;

        public String getTotalRating() {
            return totalRating;
        }

        public void setTotalRating(String totalRating) {
            this.totalRating = totalRating;
        }

        public String getRatings() {
            return ratings;
        }

        public void setRatings(String ratings) {
            this.ratings = ratings;
        }

        public String getReviews() {
            return reviews;
        }

        public void setReviews(String reviews) {
            this.reviews = reviews;
        }

        public String getPoor() {
            return poor;
        }

        public void setPoor(String poor) {
            this.poor = poor;
        }

        public String getAverage() {
            return average;
        }

        public void setAverage(String average) {
            this.average = average;
        }

        public String getGood() {
            return good;
        }

        public void setGood(String good) {
            this.good = good;
        }

        public String getVeryGood() {
            return veryGood;
        }

        public void setVeryGood(String veryGood) {
            this.veryGood = veryGood;
        }

        public String getExcellent() {
            return excellent;
        }

        public void setExcellent(String excellent) {
            this.excellent = excellent;
        }
    }

}
