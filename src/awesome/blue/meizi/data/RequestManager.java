
package awesome.blue.meizi.data;

import java.io.File;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.TypedValue;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import awesome.blue.meizi.MeiziApplication;
import awesome.blue.meizi.util.BLog;
import awesome.blue.meizi.util.CacheUtils;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;

public class RequestManager {

    private static RequestQueue mRequestQueue = newRequestQueue();

    private static final int MEM_CACHE_SIZE = 1024 * 1024 * ((ActivityManager) MeiziApplication
            .getContext().getSystemService(Context.ACTIVITY_SERVICE))
            .getMemoryClass() / 3;

    private static ImageLoader mImageLoader = new ImageLoader(mRequestQueue,
            new BitmapLruCache(MEM_CACHE_SIZE));

    private static DiskBasedCache mDiskCache = (DiskBasedCache) mRequestQueue
            .getCache();

    private RequestManager() {

    }

    private static RequestQueue newRequestQueue() {
        RequestQueue requestQueue = new RequestQueue(openCache(),
                new BasicNetwork(new HurlStack()));
        requestQueue.start();
        return requestQueue;
    }

    private static Cache openCache() {
        return new DiskBasedCache(
                CacheUtils.getExternalCacheDir(MeiziApplication.getContext()),
                10 * 1024 * 1024);
    }

    public static void addRequest(Request request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }

        mRequestQueue.add(request);
    }

    public static void cancelAll(Object tag) {
        mRequestQueue.cancelAll(tag);
    }

    public static File getCachedImageFile(String url) {
        return mDiskCache.getFileForKey(url);
    }

    public static ImageLoader.ImageContainer loadImage(String requestUrl,
            ImageLoader.ImageListener imageListener) {
        return loadImage(requestUrl, imageListener, 0, 0);
    }

    public static ImageLoader.ImageContainer loadImage(String requestUrl,
            ImageLoader.ImageListener imageListener, int maxWidth, int maxHeight) {
        return mImageLoader.get(requestUrl, imageListener, maxWidth, maxHeight);
    }

    public static ImageLoader.ImageListener getImageListener(
            final ImageView view, final Drawable defaultImageDrawable,
            final Drawable errorImageDrawable) {
        return getImageListener(view, defaultImageDrawable, errorImageDrawable, false);
    }

    public static ImageLoader.ImageListener getImageListener(
            final ImageView view, final Drawable defaultImageDrawable,
            final Drawable errorImageDrawable, final boolean fixScale) {
        return new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (errorImageDrawable != null) {
                    view.setImageDrawable(errorImageDrawable);
                }
                BLog.d("BLUE", "onErrorResponse");
            }

            @Override
            public void onResponse(final ImageLoader.ImageContainer response,
                    boolean isImmediate) {
                if (response.getBitmap() != null) {
                    if (!isImmediate && defaultImageDrawable != null) {
                        TransitionDrawable transitionDrawable = new TransitionDrawable(
                                new Drawable[] {
                                        defaultImageDrawable,
                                        new BitmapDrawable(MeiziApplication
                                                .getContext().getResources(),
                                                response.getBitmap())
                                });
                        transitionDrawable.setCrossFadeEnabled(true);
                        view.setImageDrawable(transitionDrawable);
                        transitionDrawable.startTransition(100);
                    } else {
                        view.setImageBitmap(response.getBitmap());
                    }

                    if (fixScale) {
                        if (view.getWidth() != 0) {
                            int bitmapWidth = response.getBitmap().getWidth();
                            int bitmapHeight = response.getBitmap().getHeight();
                            LayoutParams params = view.getLayoutParams();
                            params.height = (int) ((float) view.getWidth() / (float) bitmapWidth * (float) bitmapHeight);
                            view.requestLayout();
                        } else {
                            ViewTreeObserver observer = view.getViewTreeObserver();
                            observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

                                @Override
                                public void onGlobalLayout() {
                                    int bitmapWidth = response.getBitmap().getWidth();
                                    int bitmapHeight = response.getBitmap().getHeight();
                                    LayoutParams params = view.getLayoutParams();
                                    params.height = (int) ((float) view.getWidth()
                                            / (float) bitmapWidth * (float) bitmapHeight);
                                    view.requestLayout();
                                }
                            });
                        }

                        // [Ou Runqiang] should make the view grow from small to
                        // large
                    }
                } else if (defaultImageDrawable != null) {
                    view.setImageDrawable(defaultImageDrawable);
                    if (fixScale) {
                        LayoutParams params = view.getLayoutParams();
                        params.height = (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, 250,
                                MeiziApplication.getContext().getResources().getDisplayMetrics());
                        view.setLayoutParams(params);
                    }
                }

            }
        };
    }
}
