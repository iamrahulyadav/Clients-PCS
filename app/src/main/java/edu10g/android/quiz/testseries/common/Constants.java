package edu10g.android.quiz.testseries.common;


/**
 * Constants class for storing string constants
 */
public class Constants {
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    public static boolean FromPush = false;
    public static final long ZERO_INT_VALUE = 0;
    public static String SUCCESS = "success";
    public static String ERROR = "error";
    public static String STATUS = "statuscode";
    public static String MESSAGE = "message";
    public static String MSG = "msg";


    public static class AttemptQuizdata {
        public static String Quid ="";
        public static String is_Demo;
        public static String quiz_Name;
        public static String Description;
        public static String Duration;
        public static String maximum_Attempts;
        public static String pass_Percentage;
        public static String correct_Score;
        public static String incorrect_Score;
        public static String start_date;
        public static String end_date;
        public static String noq;
    }


}
