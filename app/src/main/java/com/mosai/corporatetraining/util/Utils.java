package com.mosai.corporatetraining.util;

import android.content.Context;
import android.os.Environment;
import android.widget.ImageView;

import com.mosai.corporatetraining.network.AppAction;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

/**
 * 描述:
 * 作者：周年斌
 * 时间：2016/5/18 0018 14:40
 * 邮箱：zhounianbin@mastercom.cn
 */
public class Utils {
    public static boolean checkLocalFile(Context context,String filename){
        return new File(getLocalFile(context,filename)).exists();
    }
    public static String getLocalFile(Context context,String filename){
       return Environment.getExternalStorageDirectory()+File.separator + context.getPackageName() + File.separator+filename;
    }
    public static String getImgUrl(String id,String name){
        return String.format("%s%s/%s", AppAction.IMG_RESOURSE_COURSE_URL,id,name);
    }
    public static void displayImage(String url, ImageView imageView, DisplayImageOptions options){
        ImageLoader.getInstance().displayImage(url,imageView,options);
    }
    public static void displayImage(String id,String name, ImageView imageView, DisplayImageOptions options){
        ImageLoader.getInstance().displayImage(getImgUrl(id,name),imageView,options);
    }
    public static String getFileUrl(String resourceId,String name){
        return String.format("%s%s/%s", AppAction.FILE_RESOURSE_COURSE_URL,resourceId,name);
    }
}
