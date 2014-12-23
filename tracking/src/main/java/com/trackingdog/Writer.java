package com.trackingdog;

import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by tonyley on 14/12/22.
 */
/*package*/ final class Writer{
    public static final String TIME_FORMAT_IN_LOG = "yyyy/MM/dd k:mm:ss.SSS";
    public static final String TIME_FORMAT_FILE_NAME = "yyyy_MM_dd_k_mm_ss_SSS";
    private static final File DOG_FOLDER = new File(Environment.getExternalStorageDirectory(), String.format(Locale.US, "TrackingDog%1$s%2$s", File.separator, TrackingDog.getPackageName()));

    private static SimpleDateFormat dateFormat = new SimpleDateFormat();

    public static String write(Thread thread, Throwable ex, String logPrefix){
        if(! DOG_FOLDER.exists()){
            DOG_FOLDER.mkdirs();
        }

        long millis;
        String fileName;
        File log;
        dateFormat.applyPattern(TIME_FORMAT_FILE_NAME);
        do{
            millis = System.currentTimeMillis();
            fileName = String.format(Locale.US, "%1$s_%2$s", logPrefix, dateFormat.format(millis));
            log = new File(DOG_FOLDER, fileName);
        }while(log.exists());

        FileWriter writer = null;
        String path = log.getAbsolutePath();
        try{
            StringBuffer buffer = new StringBuffer(300);
            collectStackTrace(ex, buffer);
            appendInformation(buffer, millis);
            String stackTraceInfo = buffer.toString();

            System.out.println(path);
            writer = new FileWriter(log, false);
            writer.write(stackTraceInfo);
            writer.flush();
        }catch(IOException ioe){
            ioe.printStackTrace();
            //do nothing
        } finally {
            if(null != writer){
                try{
                    writer.close();
                }catch(IOException e){
                    //do nothing
                }
            }
        }
        return path;
    }

    private static void collectStackTrace(Throwable ex, StringBuffer buffer){
        buffer.append(ex.toString()).append("\r\n");
        buffer.append("  ____>>Stack Trace:\r\n");
        StackTraceElement[] stackTraceElements = ex.getStackTrace();
        for(StackTraceElement element : stackTraceElements){
            buffer.append(element).append("\r\n");
        }

        Throwable cause = ex.getCause();
        if(null != cause){
            buffer.append("  ____>>Cause:\r\n");
            collectStackTrace(cause, buffer);
        }
    }

    private static void appendInformation(StringBuffer buffer, long millis){
        dateFormat.applyPattern(TIME_FORMAT_IN_LOG);
        buffer.append(String.format(Locale.US, "\n\nCatch in %1$s", dateFormat.format(millis))).append("\r\n");
        buffer.append("Device    ").append(Build.MODEL).append("\r\n");
        buffer.append("Android   ").append(Build.VERSION.RELEASE).append("\r\n");
    }
}
