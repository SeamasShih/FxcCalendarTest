package com.honhai.foxconn.fxccalendar.data;

public class Event {

    public int startYear;
    public int startMonth;
    public int startDayOfMonth;
    public int startHour;
    public int startMinute;
    public int endYear;
    public int endMonth;
    public int endDayOfMonth;
    public int endHour;
    public int endMinute;
    public int color;
    public String context;

    public Event(String context, int startYear, int startMonth, int startDayOfMonth, int startHour, int startMinute,
                 int endYear, int endMonth, int endDayOfMonth, int endHour, int endMinute, int color) {
        this.context = context;
        this.startYear = startYear;
        this.startMonth = startMonth;
        this.startDayOfMonth = startDayOfMonth;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endYear = endYear;
        this.endMonth = endMonth;
        this.endDayOfMonth = endDayOfMonth;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.color = color;
    }

    public long getStartTime(){
        return (startYear * 100000000 + startMonth * 1000000 + startDayOfMonth * 10000 + startHour*100 + startMinute);
    }

    public long getEndTime(){
        return (endYear * 100000000 + endMonth * 1000000 + endDayOfMonth * 10000 + endHour*100 + endMinute);
    }
}
