package edu10g.android.quiz.testseries.models;

/**
 * Created by Vikram on 1/28/2018.
 */

public class Options {
    private String oid;
    private String qid;
    private String q_option;

    public String getQ_option_h() {
        return q_option_h;
    }

    public void setQ_option_h(String q_option_h) {
        this.q_option_h = q_option_h;
    }

    public String getQ_option_match_h() {
        return q_option_match_h;
    }

    public void setQ_option_match_h(String q_option_match_h) {
        this.q_option_match_h = q_option_match_h;
    }

    private String q_option_h;
    private String q_option_match;
    private String q_option_match_h;
    private String score;


    public Options() {

    }

    public String getOid()
    {
        return oid;
    }
    public void setOid(String oid)
    {
        this.oid=oid;
    }
    public String getQid()
    {
        return qid;
    }
    public void setQid(String qid)
    {
        this.qid=qid;
    }
    public String getQ_option()
    {
        return q_option;
    }
    public void setqQ_option(String q_option)
    {
        this.q_option=q_option;
    }
    public String getQ_option_match()
    {
        return q_option_match;
    }
    public void setQ_option_match(String q_option_match)
    {
        this.q_option_match=q_option_match;
    }
    public String getScore()
    {
        return score;
    }
    public void setScore(String score)
    {
        this.score=score;
    }
}
