package network.Response;

import java.util.ArrayList;

public class ResponseGetFaqList {
    private boolean status;
    private String message;
    private ArrayList<FAQResult> result;

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

    public ArrayList<FAQResult> getFAQResult() {
        return result;
    }

    public void setFAQResult(ArrayList<FAQResult> result) {
        this.result = result;
    }


    public static class FAQResult {
        String faqid;
        String question;
        String answer;

        public String getFaqid() {
            return faqid;
        }

        public void setFaqid(String faqid) {
            this.faqid = faqid;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }


    }
}
