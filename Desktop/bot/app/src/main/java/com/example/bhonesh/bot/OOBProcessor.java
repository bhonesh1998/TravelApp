package com.example.bhonesh.bot;

/**
 * Created by bhonesh on 17/3/18.
 */
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.util.Log;

public class OOBProcessor {

   /* MainActivity activity;
    Exception exception = null;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    public OOBProcessor(MainActivity activity) {
        this.activity = activity;
    }

    // remove the oob tags and send it along to the processor
    public void removeOobTags(String output) throws Exception {
        if (output != null) {
            Pattern pattern = Pattern.compile("<oob>(.*)</oob>");
            Matcher matcher = pattern.matcher(output);
            if (matcher.find()) {
                String oobContent = matcher.group(1);
                processInnerOobTags(oobContent);
                activity.showBotResponse(matcher.replaceAll(""));
            }
        }
    }

    // check inner oob command and take appropriate action
    public void processInnerOobTags(String oobContent) throws Exception {
        if (oobContent.contains("<camera>")) {
            oobCamera();
        }
        if (oobContent.contains("<calendar>")) {
            oobCalendar(oobContent);
        }

    }

    // open the camera
    public void oobCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    // make the calendar event
    @SuppressLint("NewApi")
    public void oobCalendar(String oobContent) {
        //match and extract the content in the various fields within the calendar event
        Pattern bHTimePattern = Pattern.compile("<starthour>(.*)</starthour>");
        Matcher bHTimeMatcher = bHTimePattern.matcher(oobContent);
        Pattern bMTimePattern = Pattern.compile("<startminute>(.*)</startminute>");
        Matcher bMTimeMatcher = bMTimePattern.matcher(oobContent);
        Pattern eHTimePattern = Pattern.compile("<endhour>(.*)</endhour>");
        Matcher eHTimeMatcher = eHTimePattern.matcher(oobContent);
        Pattern eMTimePattern = Pattern.compile("<endminutes>(.*)</endminutes>");
        Matcher eMTimeMatcher = eMTimePattern.matcher(oobContent);
        Pattern dayPattern = Pattern.compile("<day>(.*)</day>");
        Matcher dayMatcher = dayPattern.matcher(oobContent);
        Pattern yearPattern = Pattern.compile("<year>(.*)</year>");
        Matcher yearMatcher = yearPattern.matcher(oobContent);
        Pattern monthPattern = Pattern.compile("<month>(.*)</month>");
        Matcher monthMatcher = monthPattern.matcher(oobContent);
        Pattern titlePattern = Pattern.compile("<title>(.*)</title>");
        Matcher titleMatcher = titlePattern.matcher(oobContent);
        Pattern locPattern = Pattern.compile("<location>(.*)</location>");
        Matcher locMatcher = locPattern.matcher(oobContent);
        if (bHTimeMatcher.find() && bMTimeMatcher.find() && eHTimeMatcher.find() && eMTimeMatcher.find() && dayMatcher.find() && yearMatcher.find() && monthMatcher.find() && titleMatcher.find() && locMatcher.find()) {
            Intent calendarIntent = new Intent(Intent.ACTION_EDIT);
            calendarIntent.setType("vnd.android.cursor.item/event");
            try {
                // create the calendar intent with the data as specified from the conversation
                int year = Integer.parseInt(yearMatcher.group(1).toString());
                int day = Integer.parseInt(dayMatcher.group(1).toString());
                int month = Integer.parseInt(monthMatcher.group(1).toString());
                int hourOfDay = Integer.parseInt(bHTimeMatcher.group(1).toString());
                int minute = Integer.parseInt(bMTimeMatcher.group(1).toString());
                Calendar beginTime = Calendar.getInstance();
                beginTime.set(year, month, day, hourOfDay, minute);
                hourOfDay = Integer.parseInt(eHTimeMatcher.group(1).toString());
                minute = Integer.parseInt(eMTimeMatcher.group(1).toString());
                Calendar endTime = Calendar.getInstance();
                endTime.set(year, month, day, hourOfDay, minute);
                calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
                calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
                calendarIntent.putExtra(Events.TITLE, titleMatcher.group(1).toString());
                calendarIntent.putExtra(Events.EVENT_LOCATION, locMatcher.group(1).toString());
                activity.startActivity(calendarIntent);
            } catch (Exception ex) {
                activity.processBotResponse("There was an error in scheduling your event.");
            }
        } else activity.showBotResponse("There was an issue with one of the details you specificied. Please try and schedule your event again.");
    }
*/
}
