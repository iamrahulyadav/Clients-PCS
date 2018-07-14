package edu10g.android.quiz.testseries.models;

/**
 * Created by Vikram on 1/21/2018.
 */

public class Userdata {
    private String user_id;
    private String name;
    private String email;
    private String phone;
    private String loginType;

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    private String profilePic;


    public void setUser_id(String user_id)
    {
        this.user_id=user_id;
    }
    public String getUse_id()
    {
        return user_id;
    }
    public void setName(String name)
    {
        this.name=name;
    }
    public String getName()
    {
        return name;
    }
    public void setEmail(String email)
    {
        this.email=email;
    }
    public String getEmail()
    {
        return email;
    }
    public void setphone(String phone)
    {
        this.phone=phone;
    }
    public String getPhone()
    {
        return phone;
    }

}
