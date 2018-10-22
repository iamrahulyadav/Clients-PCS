package edu10g.android.quiz.statepcstest.models;

/**
 * Created by vikram on 23/3/18.
 */

public class Qdata {
    private String qid;
    private int answerOption;

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private int status;

    public int getAnswerOption(){
        return this.answerOption;
    }
    public void setAnswerOption(int option){
        this.answerOption = option;
    }
}
