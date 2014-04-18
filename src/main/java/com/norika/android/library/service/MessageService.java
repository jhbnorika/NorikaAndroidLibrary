
package com.norika.android.library.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

import com.norika.android.library.utils.DebugUtil;

/**
 * 消息服务
 * 
 * @author Norika
 * @version 1.0
 * @since 1.0
 */
public class MessageService extends Service {
    private Looper msgServiceLooper;
    private ServiceHandler msgServiceHandler;

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            // TODO something which costs much time

            DebugUtil.v("___RequestMessagesAfterNetworkRetrieves Start");
            // NorikaX 网络请求
            DebugUtil.v("___RequestMessagesAfterNetworkRetrieves End");

            // Stop the service using the startId, so that we don't stop the
            // service in the middle of handling another job.
            boolean state = stopSelfResult(msg.arg1);
            DebugUtil.v("startId stop state:" + state);
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, MessageService.class);
        context.startService(intent);
    }

    protected static void stop(Context context) {
        Intent intent = new Intent(context, MessageService.class);
        context.stopService(intent);
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service.
        // Note that we create a separate thread because the service normally
        // runs in the process's main thread, which we don't want to block.
        // We also make it background priority so CPU-intensive work will not
        // disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler.
        msgServiceLooper = thread.getLooper();
        msgServiceHandler = new ServiceHandler(msgServiceLooper);

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DebugUtil.v("personal messages's request service starting");

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the
        // job
        Message msg = msgServiceHandler.obtainMessage();
        msg.arg1 = startId;
        msgServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DebugUtil.v("all message update service was done or terminated");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
