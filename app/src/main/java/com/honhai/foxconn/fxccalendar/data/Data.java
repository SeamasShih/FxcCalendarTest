package com.honhai.foxconn.fxccalendar.data;

import android.graphics.Color;

import java.util.ArrayList;

public class Data {

    private static Data instance = new Data();

    public ArrayList<Event> events = new ArrayList<>();
    public int themeColor = Color.BLUE;

    private Data(){}

    public static Data getInstnace(){
        return instance;
    }

}
