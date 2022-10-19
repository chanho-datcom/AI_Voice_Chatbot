package com.example.roda;

import java.sql.Time;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class timeCheck {
    public String getTimeCheck(long duration){
        Date now = new Date();

        long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime()-duration);
        long minute = TimeUnit.MILLISECONDS.toMinutes(now.getTime()-duration);
        long hour = TimeUnit.MILLISECONDS.toHours(now.getTime()-duration);
        long days = TimeUnit.MILLISECONDS.toDays(now.getTime()-duration);

        if(seconds < 60){
            return "방금";
        } else if (minute == 1){
            return "몇 분 전";
        } else if (minute > 1 && minute < 60){
            return minute + "분 전";
        } else if (hour == 1){
            return "한시간 전";
        } else if(hour > 1 && hour < 24){
            return hour + "시간 전";
        } else if(days == 1){
            return "하루 전";
        } else {
            return days + "일 전";
        }
    }
}
