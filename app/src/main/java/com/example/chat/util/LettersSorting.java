package com.example.chat.util;

/*
 *  项目名：  LettersNavigation 
 *  创建者:   LGL
 *  描述：    字母排序算法
 */

import com.example.chat.bean.User;

import java.util.Comparator;

public class LettersSorting implements Comparator<User> {

    @Override
    public int compare(User lettersModel, User t1) {
        //给我两个对象，我只比较他的字母
        return lettersModel.getLetter().compareTo(t1.getLetter());
    }
}

