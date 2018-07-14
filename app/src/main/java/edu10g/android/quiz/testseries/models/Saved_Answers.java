package edu10g.android.quiz.testseries.models;

/**
 * Created by Vikram on 1/27/2018.
 */

public class Saved_Answers {
    private String qid;


    private String q_option;
    private String score_u;

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getQ_option() {
        return q_option;
    }

    public void setQ_option(String q_option) {
        this.q_option = q_option;
    }

    public String getScore_u() {
        return score_u;
    }

    public void setScore_u(String score_u) {
        this.score_u = score_u;
    }
}
