package com.birdboys.birdbuddy;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by ariggs on 11/20/17.
 */

/**
 * Asynchronous downloader of thumbnails, using HandlerThread.
 * @param <T> Abstract type, will likely be a RecyclerView Holder.
 * @see HandlerThread
 */
public class ThumbnailDownloader<T> extends HandlerThread{
    private static final String TAG = "ThumbnailDownloader";
    private static final int MESSAGE_DOWNLOAD = 0;

    private Context context;
    private boolean hasQuit = false;
    private Handler requestHandler;
    private ConcurrentMap<T, String> requestMap = new ConcurrentHashMap<>();
    private Handler responseHandler;
    private ThumbnailDownloadListener<T> thumbnailDownloadListener;

    /**
     * Controller for download progress.
     * @param <T> Abstract type, will likely be a RecyclerView Holder.
     */
    public interface ThumbnailDownloadListener<T> {
        void onThumbnailDownloaded(T target, BitmapDrawable thumbnail);
    }

    /**
     * Sets the ThumbnailDownloadLister.
     * @param listener The ThumbnailDownloadLister to set.
     * @param thisContext The current context.
     * @see ThumbnailDownloader
     */
    public void setThumbnailDownloadListener(ThumbnailDownloadListener<T> listener, Context thisContext) {
        thumbnailDownloadListener = listener;
        context = thisContext;
    }

    /**
     * Constructor for the ThumbnailDownloader.
     * @param handler The Handler to use for asynchronous loading.
     * @param thisContext The current context.
     * @see Handler
     */
    public ThumbnailDownloader(Handler handler, Context thisContext) {
        super(TAG);
        responseHandler = handler;
        context = thisContext;
    }

    @Override
    protected void onLooperPrepared() {
        requestHandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                if(message.what == MESSAGE_DOWNLOAD) {
                    T target = (T) message.obj;
                    Log.i(TAG, "Got a request for URL: " + requestMap.get(target));
                    handleRequest(target);
                }
            }
        };
    }

    @Override
    public boolean quit() {
        hasQuit = true;
        return super.quit();
    }

    /**
     * Queue a thumbnail for downloading.
     * @param target The target to place the downloaded image in.
     * @param url The complete url to download the image from.
     */
    public void queueThumbnail(T target, String url){
        Log.i(TAG, "Got a URL: " + url);

        if(url == null) {
            requestMap.remove(target);
        } else {
            requestMap.put(target, url);
            requestHandler.obtainMessage(MESSAGE_DOWNLOAD, target).sendToTarget();
        }
    }

    /**
     * Clears the current queue.
     */
    public void clearQueue() {
        requestHandler.removeMessages(MESSAGE_DOWNLOAD);
        requestMap.clear();
    }

    /**
     * Handles the request, for the given target.
     * @param target The target to handle a request for.
     */
    private void handleRequest(final T target) {

        final String url = requestMap.get(target);

        if(url == null) {
            return;
        }

        final BitmapDrawable bitmap = SiteScrape.getBitmapDrawable(context, url);
        Log.i(TAG, "Bitmap created.");

        responseHandler.post(new Runnable() {
            public void run() {
                if(requestMap.get(target) != url || hasQuit) {
                    return;
                }

                requestMap.remove(target);
                thumbnailDownloadListener.onThumbnailDownloaded(target, bitmap);
            }
        });
    }
}
