package com.trackingdog;

/**
 * Created by tonyley on 14/12/22.
 */
/*package*/ class BackgroundThreadUncaughtHandler implements Thread.UncaughtExceptionHandler{
    private static final String PREFIX_FOR_LOG_FILE = "BG";
    private static final String PREFIX_FOR_TRACKING = "tracking";

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Writer.write(thread, ex, PREFIX_FOR_LOG_FILE);

        Thread.UncaughtExceptionHandler systemDefaultHandler = TrackingDog.getSystemDefaultHandler();
        if(null != systemDefaultHandler){
            systemDefaultHandler.uncaughtException(thread, ex);
        }
    }

    /*package*/ static void trackingThrowable(Thread thread, Throwable ex){
        Writer.write(thread, ex, PREFIX_FOR_TRACKING);
    }
}
