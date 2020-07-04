package com.example.myapplication.model;

public class Utility {
    public static String convertDuration(long duration){
        long minutes=(duration/1000)/60;
        long seconds=(duration /1000)%60;
        String converted=String.format("%d:%d",minutes,seconds);
         return converted;
    }
}
