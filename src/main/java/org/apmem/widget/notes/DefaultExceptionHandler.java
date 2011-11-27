package org.apmem.widget.notes;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: ApmeM
 * Date: 27.11.11
 * Time: 20:33
 * To change this template use File | Settings | File Templates.
 */
public class DefaultExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "DefaultExceptionHandler";

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        Log.e(TAG, "uncaught exception", throwable);
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            try {
                // http://developer.android.com/guide/topics/data/data-storage.html#filesExternal
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/org.apmem.widget.notes/cache/";
                Log.i(TAG, path);
                File file = new File(path + "log.txt");
                FileOutputStream str = new FileOutputStream(file);
                str.write(throwable.toString().getBytes());
            } catch (Exception ignored) {
                Log.e(TAG, "logger failed", ignored);
            }
        }
    }
}
