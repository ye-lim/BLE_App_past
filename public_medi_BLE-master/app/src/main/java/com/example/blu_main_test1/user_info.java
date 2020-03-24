package com.example.blu_main_test1;

public class user_info {

    private String user_id; //사용자 id
    private String user_name; //사용자 이름
    private String user_tell; //사용자 전화번호
    private String user_mail; //사용자 메일
    private String user_day; //사용자 생년월일
    private String user_sex; //사용자 성별

    public user_info(String user_id, String user_name, String user_tell, String user_mail, String user_day, String user_sex) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_tell = user_tell;
        this.user_mail = user_mail;
        this.user_day = user_day;
        this.user_sex = user_sex;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_tell() {
        return user_tell;
    }

    public void setUser_tell(String user_tell) {
        this.user_tell = user_tell;
    }

    public String getUser_mail() {
        return user_mail;
    }

    public void setUser_mail(String user_mail) {
        this.user_mail = user_mail;
    }

    public String getUser_day() {
        return user_day;
    }

    public void setUser_day(String user_day) {
        this.user_day = user_day;
    }

    public String getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(String user_sex) {
        this.user_sex = user_sex;
    }
}
