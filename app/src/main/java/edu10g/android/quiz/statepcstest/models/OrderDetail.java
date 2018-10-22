package edu10g.android.quiz.statepcstest.models;

/**
 * Created by vikram on 18/4/18.
 */

/*
"quid":"67",
"is_demo":"1",
"quiz_name":"SSC 10+2 CHSL",
"price_offer":"0",
"start_date":"1523093002",
"end_date":"1554629002",
"noq":"100",
"language":"HINDI"*/

public class OrderDetail {
    private String qid;
    private String quiz_name;
    private String noq;
    private String language;
    private String startDate;

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getQuiz_name() {
        return quiz_name;
    }

    public void setQuiz_name(String quiz_name) {
        this.quiz_name = quiz_name;
    }

    public String getNoq() {
        return noq;
    }

    public void setNoq(String noq) {
        this.noq = noq;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    private String endDate;


}
