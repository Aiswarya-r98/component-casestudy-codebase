package com.tweetapp.common.utility;

import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class DateUtility {

    public static Date getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s");
        Date currentDate = null;
        try {
            currentDate = dateFormat.parse("2020-09-23 00:00:00");
        } catch (ParseException e) {
            log.error("", e);
        }
        return currentDate;
    }
}
