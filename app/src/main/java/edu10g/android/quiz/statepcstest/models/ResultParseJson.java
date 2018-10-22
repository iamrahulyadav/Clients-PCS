package edu10g.android.quiz.statepcstest.models;

/**
 * Created by Vikram on 1/28/2018.
 */

public class ResultParseJson {
    private String name;
    private String rid;
    private String quid;
    private String uid;
    private String status;
    private String categories;
    private String percentage_obtained;
    private String quiz_name;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

    public ResultParseJson()
    {
    }
    public String getRid()
    {
        return rid;
    }
    public void setRid(String rid)
    {
        this.rid=rid;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name=name;
    }
    public String getQuid()
    {
        return quid;
    }
    public void setQuid(String quid)
    {
        this.quid=quid;
    }
    public String getUid()
    {
        return uid;
    }
    public void setUid(String uid)
    {
        this.uid=uid;
    }
    public String getCategories()
    {
        return categories;
    }
    public void setCategories(String categories)
    {
        this.categories=categories;
    }
    public String getPercentage_obtained()
    {
        return percentage_obtained;
    }
    public void setPercentage_obtained(String percentage_obtained)
    {
        this.percentage_obtained=percentage_obtained;
    }
    public String getQuiz_name()
    {
        return quiz_name;
    }
    public void setQuiz_name(String quiz_name)
    {
        this.quiz_name=quiz_name;
    }
    public String getStatus()
    {
        return status;
    }
    public void setStatus(String status)
    {
        this.status=status;
    }

}
