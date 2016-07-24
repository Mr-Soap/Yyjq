package com.whu.cs.lyxxcy.imageloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

/**
 * Using LazyList via https://github.com/thest1/LazyList/tree/master/src/com/fedorvlasov/lazylist
 * for the example since its super lightweight
 * I barely modified this file
 */
public class ImageLoader {  
    private static final String TAG = "ImageLoader";
    private static final boolean DEBUG = true;
    private int  requiredSize = 50;
    private boolean isUseMediaStoreThumbnails = true;
    private boolean needCropSquareBitmap = false;
    MemoryCache memoryCache=new MemoryCache();
    private FileCache fileCache;
    private Context mContext;
    private Map<ImageView, String> imageViews=Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    ExecutorService executorService;
    Handler handler=new Handler();//handler to display images in UI thread
    
	private Thread mPoolThread;
	private Handler mPoolThreadHander;
	private volatile Semaphore mSemaphore = new Semaphore(1);
	/**
	 * 引入�?个�?�为1的信号量，由于线程池内部也有�?个阻塞线程，防止加入任务的�?�度过快，使LIFO效果不明�?
	 */
	private volatile Semaphore mPoolSemaphore;
	/**
	 * 任务队列
	 */
	private LinkedList<Runnable> mTasks;
	/**
	 * 队列的调度方�?
	 */
	private Type mType = Type.LIFO;
	/**
	 * 队列的调度方�?
	 * 
	 * @author zhy
	 * 
	 */
	public enum Type
	{
		FIFO, LIFO
	}

	
	private Object lock = new Object();
	private boolean mScrollingLock = false;
	
    public ImageLoader(Context context){
        fileCache=new FileCache(context);
        mContext = context;
        executorService=Executors.newFixedThreadPool(2);
		mPoolSemaphore = new Semaphore(24);
		mTasks = new LinkedList<Runnable>();
		mPoolThread = new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				mPoolThreadHander = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						executorService.execute(getTask());
					}
				};

				Looper.loop();
			}
		};
		mPoolThread.start();
    }
   
    public void DisplayImage(String url, ImageView imageView)
    {
        imageViews.put(imageView, url);
        Bitmap bitmap=memoryCache.get(url);
        if(needCropSquareBitmap){
            if(imageView.getLayoutParams().width != requiredSize || imageView.getLayoutParams().height != requiredSize ) {
                if (DEBUG)
                    Log.i(TAG, "change imageview LayoutParams ");
                LayoutParams params = imageView.getLayoutParams();  
                params.height=requiredSize;  
                params.width =requiredSize;  
                imageView.setLayoutParams(params);         	
            }        	
        }

        if(bitmap!=null)
            imageView.setImageBitmap(bitmap);
        else
        {
            queuePhoto(url, imageView);
            //imageView.setImageDrawable(null);//如果这样，在bitmap很大的时候滑动非常卡
            imageView.setImageDrawable(new ColorDrawable(0xfffcfcfc));
             
        }
    }
    
	public void lock() {
        if (DEBUG)
            Log.i(TAG, "lock");
		mScrollingLock = true;
	}

	public void unlock() {
		mScrollingLock = false;
		synchronized (lock) {
	        if (DEBUG)
	            Log.i(TAG, "unlock");
			lock.notifyAll();
		}
	}
	
	public void setRequiredSize(int size) {
		requiredSize = size;
	}
    
	public void setIsUseMediaStoreThumbnails(boolean f){
		isUseMediaStoreThumbnails = f;
	}
	public void setNeedCropSquareBitmap(boolean n) {
		needCropSquareBitmap = n;
	}
    private void queuePhoto(String url, ImageView imageView)
    {
        PhotoToLoad p=new PhotoToLoad(url, imageView);
        addTask(new PhotosLoader(p));
        //executorService.execute(new PhotosLoader(p));
    }
  
    private Bitmap getBitmap(String url) 
    {
        final long startTime = SystemClock.uptimeMillis();
		Uri uri = Uri.parse(url);
		Bitmap bitmap=null;
		final String scheme = uri.getScheme();
		/*
		 * 根据不同的图片来源执行不同的步骤�?
		 * 1.如果来源于本地文件，则文件缓存这步就不需�?
		 * 2.如果是图片的Uri则直接从Thumbnail中获取bitmap，文件缓存和decodeFile这两步都不需�?
		 * 3.如果来源于网络，则先�?查是否有文件缓存，根据结果再决定是否从网络下载，decodeFile也是必须�?
		 */
		if ( scheme == null || ContentResolver.SCHEME_FILE.equals( scheme ) ) {
			bitmap = decodeFile(new File(url));
		} else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
			BitmapFactory.Options options = new BitmapFactory.Options();  
	        options.inDither = false;
	        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
	        if (isUseMediaStoreThumbnails) {
	        	bitmap = MediaStore.Images.Thumbnails.getThumbnail(mContext.getContentResolver(), ContentUris.parseId(uri), Images.Thumbnails.MICRO_KIND,options);
	        } else {
	        	bitmap = getCustomThumbnail(uri,requiredSize);
	        }
		} else if ( "http".equals( scheme ) || "https".equals( scheme ) ) {
	        File f=fileCache.getFile(url);
	        //from SD cache
	        bitmap = decodeFile(f);
	        if(bitmap != null)
	            return bitmap;
	        //from web
	        try {
	            URL imageUrl = new URL(url);
	            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
	            conn.setConnectTimeout(30000);
	            conn.setReadTimeout(30000);
	            conn.setInstanceFollowRedirects(true);
	            InputStream is=conn.getInputStream();
	            OutputStream os = new FileOutputStream(f);
	            Utils.CopyStream(is, os);
	            os.close();
	            bitmap = decodeFile(f);
	            return bitmap;
	        } catch (Throwable ex){
	           ex.printStackTrace();
	           if(ex instanceof OutOfMemoryError)
	               memoryCache.clear();
	           return null;
	        }			 
		}
		//截取正中间的正方形部�?
		if(needCropSquareBitmap){
			bitmap = Utils.createCropScaledBitmap(bitmap,requiredSize,requiredSize);
		}
        // Calculate memory usage and performance statistics
        final int memUsageKb = (bitmap.getRowBytes() * bitmap.getHeight()) / 1024;
        final long stopTime = SystemClock.uptimeMillis();
        // Publish results
        Log.i(TAG, "Time taken: " + (stopTime - startTime)
                + " ms. Memory used for scaling: " + memUsageKb + " kb."); 
		return bitmap;

    }

    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f){   	

        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1=new FileInputStream(f);
            BitmapFactory.decodeStream(stream1,null,o);
            stream1.close();
            int scale = Utils.calculateInSampleSize(o, requiredSize, requiredSize);           
            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize= scale;
            Log.i(TAG, "scale = " + scale);            
            
            FileInputStream stream2=new FileInputStream(f);
            Bitmap bitmap=BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();  
            return bitmap;
        } catch (FileNotFoundException e) {
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    //直接从图库中获得数据�?
    private  Bitmap getCustomThumbnail(Uri uri,int size){
        try {
	        InputStream input = mContext.getContentResolver().openInputStream(uri);
	        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
	        onlyBoundsOptions.inJustDecodeBounds = true;
	        onlyBoundsOptions.inDither=true;//optional
	        onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
	        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
	        input.close();
	        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
	            return null;
	        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;
	        double ratio = (originalSize > size) ? (originalSize / size) : 1.0;
	        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
	        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
	        bitmapOptions.inDither=true;//optional
	        bitmapOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
	        input = mContext.getContentResolver().openInputStream(uri);
	        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
	        input.close();
	        return bitmap;
        }catch (FileNotFoundException e) {
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
    }
    
    //Task for the queue
    private class PhotoToLoad
    {
        public String url;
        public ImageView imageView;
        public PhotoToLoad(String u, ImageView i){
            url=u; 
            imageView=i;
        }
    }
    
    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;
        PhotosLoader(PhotoToLoad photoToLoad){
            this.photoToLoad=photoToLoad;
        }
        
        @Override
        public void run() {
            try{
                if(imageViewReused(photoToLoad)){
                    if (DEBUG)
                        //Log.i(TAG, "mismatch occur befre bitmap");
                	 return;
                }
                
                Bitmap bmp=getBitmap(photoToLoad.url);
                memoryCache.put(photoToLoad.url, bmp);
                if(imageViewReused(photoToLoad)){
                    if (DEBUG)
                       // Log.i(TAG, "mismatch occur after bitmap");
                    return;
                }
                
				if (mScrollingLock) {
					synchronized (lock) {
						try {
							lock.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}    
				
                BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad);
                handler.post(bd);
               
            }catch(Throwable th){
                th.printStackTrace();
            }
        }
    }
    
    boolean imageViewReused(PhotoToLoad photoToLoad){
        String tag=imageViews.get(photoToLoad.imageView);
        if(tag==null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }
    
    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;
        public BitmapDisplayer(Bitmap b, PhotoToLoad p){bitmap=b;photoToLoad=p;}
        public void run()
        {
            if(imageViewReused(photoToLoad))
                return;
            if(bitmap!=null)
                photoToLoad.imageView.setImageBitmap(bitmap);
            else
            	photoToLoad.imageView.setImageDrawable(new ColorDrawable(0xfffcfcfc));
        }
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }
    
	/**
	 * 取出�?个任�?
	 * 
	 * @return
	 */
	private synchronized Runnable getTask()
	{
		if (mType == Type.FIFO)
		{
			return mTasks.removeFirst();
		} else if (mType == Type.LIFO)
		{
			return mTasks.removeLast();
		}
		return null;
	}
	/**
	 * 添加�?个任�?
	 * 
	 * @param runnable
	 */
	private synchronized void addTask(Runnable runnable)
	{
		try
		{
			// 请求信号量，防止mPoolThreadHander为null
			if (mPoolThreadHander == null)
				mSemaphore.acquire();
		} catch (InterruptedException e)
		{
		}
		mTasks.add(runnable);
		mPoolThreadHander.sendEmptyMessage(0x110);
	}	
    
}
