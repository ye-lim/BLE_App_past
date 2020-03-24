package com.example.blu_main_test1.main_before;

public class MemberInfo {
    private String user_uid;
    private String user_name;
    private String user_id;
    private String user_pw;
    private String user_sex;
    private String user_day;
    private String user_tel;

    public MemberInfo(){

    }

    public MemberInfo(String uid, String name, String id, String pw, String sex, String user_day, String phone){
        this.user_uid = uid;
        this.user_name = name;
        this.user_id = id;
        this.user_pw = pw;
        this.user_sex = sex;
        this.user_day = user_day;
        this.user_tel = phone;
    }

    public String getUid(){
        return this.user_uid;
    }
    public void setUid(String uid){
        this.user_uid = uid;
    }

    //userid
    public String getUser_id(){
        return this.user_id;
    }
    public void setUser_id(String user_id){
        this.user_id = user_id;
    }

    //name
    public String getUser_name(){
        return this.user_name;
    }

    //userpw
    public String getUser_pw(){
        return this.user_pw;
    }
    public void setUser_pw (String user_pw){
        this.user_pw = user_pw;
    }

    //gender
    public String getUser_sex(){
        return this.user_sex;
    }
    public void setUser_sex(String user_sex) {this.user_sex = user_sex;}

    //bd
    public String getUser_day(){
        return this.user_day;
    }

    //phone
    public String getUser_tel(){
        return this.user_tel;
    }
}
