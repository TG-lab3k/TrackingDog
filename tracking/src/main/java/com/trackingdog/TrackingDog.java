package com.trackingdog;

import android.content.Context;

/**
 * Created by tonyley on 14/12/22.
 */
public class TrackingDog {
    private volatile  static String packageName;
    private static Thread.UncaughtExceptionHandler systemDefaultHandler;
    private static volatile boolean isConfig;

    /**
     * Config TrackingDog for APP
     * @param context
     */
    public static void config(Context context){
        if(null == context){
            throw new IllegalArgumentException("The Context can not is null.");
        }

        Thread uiThread = Thread.currentThread();
        if(!isUIThread(uiThread)){
            throw new IllegalStateException("The method must be invoked in ui thread");
        }

        isConfig = true;
        packageName = context.getPackageName();
        systemDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        uiThread.setUncaughtExceptionHandler(new UIThreadUncaughtExceptionHandler());
        Thread.setDefaultUncaughtExceptionHandler(new BackgroundThreadUncaughtHandler());
    }

    /**
     * Tracking throwable only, but UI thread.
     * And just tracking throwable into log, could not notify to user.
     * @param ex
     */
    public static void trackingThrowableNotNotify(Throwable ex){
        if(!isConfig){
            return;
        }

        Thread thread = Thread.currentThread();
        if(!isUIThread(thread)){
            BackgroundThreadUncaughtHandler.trackingThrowable(thread, ex);
        }
    }

    private static boolean isUIThread(Thread thread){
        return "main".equals(thread.getName());
    }

    /*package*/ static String getPackageName(){
        return packageName;
    }

    /*package*/ static Thread.UncaughtExceptionHandler getSystemDefaultHandler(){
        return systemDefaultHandler;
    }
}
