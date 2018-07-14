package edu10g.android.quiz.testseries.models;

import java.util.ArrayList;

/**
 * Created by Vikram on 1/28/2018.
 */

public class Questions {
    private String qid;
    private String question_type;
    private String question;
    private String question_h;
    private String description;
    private String description_h;
    private String cid;
    private String lid;

    public ArrayList<Options> getOptionsArrayList() {
        return optionsArrayList;
    }

    public void setOptionsArrayList(ArrayList<Options> optionsArrayList) {
        this.optionsArrayList = optionsArrayList;
    }

    private ArrayList<Questions.Options> optionsArrayList;

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getQuid() {
        return quid;
    }

    public void setQuid(String uid) {
        this.quid = uid;
    }

    private String rid;
    private String quid;
    private String no_time_served;
    private String no_time_corrected;
    private String no_time_incorrected;
    private String no_time_unattempted;
    private String category_name;
    private String level_name;


    public Questions() {

    }

    public String getQid()
    {
        return qid;
    }
    public void setQid(String qid)
    {
        this.qid=qid;
    }
    public String getQuestion_type()
    {
        return question_type;
    }
    public void setQuestion_type(String question_type)
    {
        this.question_type=question_type;
    }
    public String getQuestion()
    {
        return question;
    }
    public void setQuestion(String question)
    {
        this.question=question;
    }
    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description=description;
    }
    public String getQuestion_h()
    {
        return question_h;
    }
    public void setQuestion_h(String question_h)
    {
        this.question_h=question_h;
    }
    public String getDescription_h()
    {
        return description_h;
    }
    public void setDescription_h(String description_h)
    {
        this.description_h=description_h;
    }
    public String getCid()
    {
        return cid;
    }
    public void setCid(String cid)
    {
        this.cid=cid;
    }
    public String getLid()
    {
        return lid;
    }
    public void setLid(String lid)
    {
        this.lid=lid;
    }
    public String getNo_time_served()
    {
        return no_time_served;
    }
    public void setNo_time_served(String no_time_served)
    {
        this.no_time_served=no_time_served;
    }
    public String getNo_time_corrected()
    {
        return no_time_corrected;
    }
    public void setNo_time_corrected(String no_time_corrected)
    {
        this.no_time_corrected=no_time_corrected;
    }
    public String getNo_time_incorrected()
    {
        return no_time_incorrected;
    }
    public void setNo_time_incorrected(String no_time_incorrected)
    {
        this.no_time_incorrected=no_time_incorrected;
    }
    public String getNo_time_unattempted()
    {
        return no_time_unattempted;
    }
    public void setNo_time_unattempted(String no_time_unattempted)
    {
        this.no_time_unattempted=no_time_unattempted;
    }
    public String getCategory_name()
    {
        return category_name;
    }
    public void setCategory_name(String category_name)
    {
        this.category_name=category_name;
    }
    public String getLevel_name()
    {
        return level_name;
    }
    public void setLevel_name(String level_name)
    {
        this.level_name=level_name;
    }



    public static class Options {
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

}
