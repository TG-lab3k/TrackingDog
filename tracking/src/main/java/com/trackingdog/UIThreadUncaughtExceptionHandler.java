package com.trackingdog;

/**
 * Created by tonyley on 14/12/22.
 */
/*package*/ class UIThreadUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler{
    private static final String PREFIX_FOR_LOG_FILE = "UI";

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if(null == ex){
            return;
        };

        Writer.write(thread, ex, PREFIX_FOR_LOG_FILE);
        Thread.UncaughtExceptionHandler systemDefaultHandler = TrackingDog.getSystemDefaultHandler();
        if(null != systemDefaultHandler){
            systemDefaultHandler.uncaughtException(thread, ex);
        }else{
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
