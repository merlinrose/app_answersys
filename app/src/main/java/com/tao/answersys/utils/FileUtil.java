package com.tao.answersys.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;

/**
 * 文件工具类
 * @author LiangTao
 *
 */
public class FileUtil {
	/**
	 * 获取指定路径图片的Bitmap对象
	 * @param path
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static Bitmap getLowQualityBitmap(String path) {
		File file = new File(path);
		Bitmap bitmap = null;
		FileInputStream fis = null;
		if (file != null) {
			try {
				fis = new FileInputStream(file);
				java.text.DateFormat df = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
				bitmap = BitmapFactory.decodeStream(fis);
				
				if (bitmap.getHeight() > 500) {
					bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/5, bitmap.getHeight()/5, true);
					bitmap = drawTextToBitmap(bitmap, df.format(new Date()));
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return bitmap;
	}
	
	/**
	 * 获取照片缩率图
	 * @param path
	 * @return
	 */
	public static Bitmap getImageThumbnail(String path) {
		File file = new File(path);
		Bitmap bitmap = null;
		FileInputStream fis = null;
		if (file != null) {
			try {
				fis = new FileInputStream(file);
				bitmap = BitmapFactory.decodeStream(fis);

				if (bitmap.getHeight() > 64) {
					bitmap = Bitmap.createScaledBitmap(bitmap, 120, 90, true);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return bitmap;
	}
	
	/**
	 * 获取指定路径视频的缩略图
	 * @param filePath
	 * @return
	 */
	public static Bitmap getVideoThumbnail(String filePath) {  
        Bitmap bitmap = null;  
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();  
        try {  
            retriever.setDataSource(filePath);  
            bitmap = retriever.getFrameAtTime();  
        }   
        catch(IllegalArgumentException e) {  
            e.printStackTrace();  
        }   
        catch (RuntimeException e) {  
            e.printStackTrace();  
        }   
        finally {  
            try {  
                retriever.release();  
            }   
            catch (RuntimeException e) {  
                e.printStackTrace();  
            }  
        }  
        return bitmap;  
    }  

	/**
	 * 创建视频文件，用于存储录制结果
	 * @param context
	 * @return
	 * @throws IOException
	 */
	public static File createVideoFile(Context context) throws IOException {
	    String timeStamp = new Date().getTime() + "";
	    
	    File storageDir = context.getExternalFilesDir("videos");
	    File video = File.createTempFile(
	    	timeStamp,   	/* prefix */
	        ".mp4",         /* suffix */
	        storageDir      /* directory */
	    );
	    return video;
	}
	
	/**
	 * 创建图像文件，用于存储拍照图像
	 * @param context
	 * @return
	 * @throws IOException
	 */
	public static File createImageFile(Context context) throws IOException {
	    String timeStamp = new Date().getTime() + "";
	   
	    File storageDir = context.getExternalFilesDir("pictures");
	    File image = File.createTempFile(
	    	timeStamp,   	/* prefix */
	        ".jpg",         /* suffix */
	        storageDir      /* directory */
	    );
	    
	    return image;
	}
	
	public static File createAppFile(Context context) throws IOException {
	    File storageDir = context.getExternalFilesDir("temp");
	    File tempFile = File.createTempFile("update", ".apk", storageDir);
	    
	    return tempFile;
	}
	
	/**
	 * 保存bitmap到本地
	 * @param path
	 * @param mBitmap
	 */
	public static void saveBitmap(String path, Bitmap mBitmap) {
		File f = new File(path);
		try {
			if (f.exists()) {
				f.delete();
			}
			f.createNewFile();
		} catch (IOException e) {
		}
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 绘制文字水印
	 * @param gContext
	 * @param bitmap
	 * @param gText
	 * @return
	 */
	public static Bitmap drawTextToBitmap(Bitmap bitmap, String gText) {
		android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();

		// set default bitmap config if none
		if (bitmapConfig == null) {
			bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
		}
		// resource bitmaps are imutable,
		// so we need to convert it to mutable one
		bitmap = bitmap.copy(bitmapConfig, true);

		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

		paint.setColor(Color.WHITE);
		paint.setTextSize(25);
		paint.setDither(true); // 获取跟清晰的图像采样
		paint.setFilterBitmap(true);// 过滤一些
		Rect bounds = new Rect();
		paint.getTextBounds(gText, 0, gText.length(), bounds);
		int x = (int) (bitmap.getWidth() - 15 * gText.length());
		int y = bitmap.getHeight() - 40;
		canvas.drawText(gText, x, y, paint);

		return bitmap;
	}

}
