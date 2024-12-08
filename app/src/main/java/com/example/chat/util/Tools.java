package com.example.chat.util;

public class Tools {
    //校验密码（8-16位数字或这字母组成）
    public static boolean  isPassword(String password){
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
        return password.matches(regex);
    }

    //校验昵称（8位内 数字或这字母组成）
    public static boolean  isName(String name){
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{1,8}$";
        return name.matches(regex);
    }
}
