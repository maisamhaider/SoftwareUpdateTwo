package com.example.softwareupdatetwo.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyTime {


    public String appInstallTime(long milliSec)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSec);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MM:yyyy h:mm;ss a");
        return simpleDateFormat.format(calendar.getTime());
    }

    
}
