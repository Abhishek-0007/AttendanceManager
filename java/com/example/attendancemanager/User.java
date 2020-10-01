package com.example.attendancemanager;

public class User {
    String Displayname;
    String Avatar;
    String Email;
    long createdAt;

    public User (){};
    public User(String displayname,String email,String avatar,long createdAt){
        this.Displayname=displayname;
        this.Email=email;
        this.Avatar=avatar;
        this.createdAt=createdAt;
    }


    public String getDisplayname() {
        return Displayname;
    }

    public String getEmail() {
        return Email;
    }

    public String getAvatar() { return Avatar; }

    public long getCreatedAt() {
        return createdAt;
    }

}
