package com.example.dummy.Models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormat {
    static public String getFormatedDate(Date date){
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        return df.format(date);
    }
}
