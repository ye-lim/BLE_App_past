package com.example.blu_main_test1;

public class serial {
    private String num;
    private String serial;
    private String using;

    public serial(String num, String serial, String using) {
        this.num = num;
        this.serial = serial;
        this.using = using;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getusing() {
        return using;
    }

    public void setusing(String using) {
        this.using = using;
    }
}
