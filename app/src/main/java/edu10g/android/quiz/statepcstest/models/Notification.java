package edu10g.android.quiz.statepcstest.models;

/**
 * Created by vikram on 12/7/18.
 */

public class Notification {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String isRead() {
        return isRead;
    }

    public void setRead(String read) {
        isRead = read;
    }

    public void setNotificationTitle(String title){
        this.notificationTitle = title;
    }
    public String getNotificationTitle(){
        return notificationTitle;
    }
    public void setAdded_date(String date){
        this.added_date = date;
    }
    public String getAdded_date(){
        return added_date;
    }

    public String getNotification_description() {
        return notification_description;
    }

    public void setNotification_description(String coupon_description) {
        this.notification_description = coupon_description;
    }

    private String notificationTitle;

    private String added_date;

    private String notification_description;
    private String isRead;
}
