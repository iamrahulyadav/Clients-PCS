package edu10g.android.quiz.testseries.models;

/**
 * Created by Vikram on 1/28/2018.
 */

public class AttemptQuizdata {
    public String getQuid() {
        return Quid;
    }

    public void setQuid(String quid) {
        Quid = quid;
    }

    public String getIs_Demo() {
        return is_Demo;
    }

    public void setIs_Demo(String is_Demo) {
        this.is_Demo = is_Demo;
    }

    public String getQuiz_Name() {
        return quiz_Name;
    }

    public void setQuiz_Name(String quiz_Name) {
        this.quiz_Name = quiz_Name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getMaximum_Attempts() {
        return maximum_Attempts;
    }

    public void setMaximum_Attempts(String maximum_Attempts) {
        this.maximum_Attempts = maximum_Attempts;
    }

    public String getPass_Percentage() {
        return pass_Percentage;
    }

    public void setPass_Percentage(String pass_Percentage) {
        this.pass_Percentage = pass_Percentage;
    }

    public String getCorrect_Score() {
        return correct_Score;
    }

    public void setCorrect_Score(String correct_Score) {
        this.correct_Score = correct_Score;
    }

    public String getIncorrect_Score() {
        return incorrect_Score;
    }

    public void setIncorrect_Score(String incorrect_Score) {
        this.incorrect_Score = incorrect_Score;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getNoq() {
        return noq;
    }

    public void setNoq(String noq) {
        this.noq = noq;
    }

    private String Quid;
    private String is_Demo;
    private String quiz_Name;
    private String Description;
    private String Duration;
    private String maximum_Attempts;
    private String pass_Percentage;
    private String correct_Score;
    private String incorrect_Score;
    private String start_date;
    private String end_date;
    private String noq;

}
