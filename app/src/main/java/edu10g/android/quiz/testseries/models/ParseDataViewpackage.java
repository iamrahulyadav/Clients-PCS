package edu10g.android.quiz.testseries.models;

/**
 * Created by Vikram on 1/13/2018.
 */

public class ParseDataViewpackage {
    private   String quid;

    public String getQno() {
        return qno;
    }

    public void setQno(String qno) {
        this.qno = qno;
    }

    private   String qno;
    private   String is_demo;
    private   String quiz_name;
    private   String price_offer;
    private   String start_date;
    private   String end_date;
    private   String noq;
    private   String language;

    public ParseDataViewpackage()
    {

    }
    public ParseDataViewpackage(String quid,String is_demo,String quiz_name,String price_offer,String start_date,String end_date,String noq,String language)
    {
        this.quid=quid;
        this.is_demo=is_demo;
        this.quiz_name=quiz_name;
        this.price_offer=price_offer;
        this.start_date=start_date;
        this.end_date=end_date;
        this.noq=noq;
        this.language=language;
    }
    public String getQuid()
    {
        return quid;
    }
    public void setQuid(String quid)
    {
        this.quid=quid;
    }

    public String getIs_demo()
    {
        return is_demo;
    }
    public void setIs_demo(String is_demo)
    {
        this.is_demo=is_demo;
    }
    public String getQuiz_name()
    {
        return quiz_name;
    }
    public void setQuiz_name(String quiz_name)
    {
        this.quiz_name=quiz_name;
    }

    public String getPrice_offer()
    {
        return price_offer;
    }
    public void setPrice_offer(String price_offer)
    {
        this.price_offer=price_offer;
    }

    public String getStart_date()
    {
        return start_date;
    }
    public void setStart_date(String start_date)
    {
        this.start_date=start_date;
    }

    public String getEnd_date()
    {
        return end_date;
    }
    public void setEnd_date(String end_date)
    {
        this.end_date=end_date;
    }

    public String getNoq()
    {
        return noq;
    }
    public void setNoq(String noq)
    {
        this.noq=noq;
    }
    public String getLanguage()
    {
        return language;
    }
    public void setLanguage(String language)
    {
        this.language=language;
    }

}
