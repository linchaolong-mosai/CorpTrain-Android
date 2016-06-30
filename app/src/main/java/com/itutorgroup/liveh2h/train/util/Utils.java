package com.itutorgroup.liveh2h.train.util;

import android.content.Context;
import android.os.Environment;
import android.widget.ImageView;

import com.itutorgroup.liveh2h.train.network.AppAction;
import com.mosai.utils.AppUtils;
import com.mosai.utils.EncodeUtil;
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
    public static String getMaterialsDir(Context context){
        return Environment.getExternalStorageDirectory()+File.separator + context.getPackageName();
    }

    public static String getLocalFile(Context context,String filename){
       return Environment.getExternalStorageDirectory()+File.separator + context.getPackageName() + File.separator+filename;
    }
    public static String getImgUrlNotTrim(String id,String name){
        return (String.format("%s%s/%s", AppAction.IMG_RESOURSE_COURSE_URL,id,name));
    }
    public static String getImgUrl(String id,String name){
        return replaceSpace(String.format("%s%s/%s", AppAction.IMG_RESOURSE_COURSE_URL,id,name));
    }
    public static void displayImage(String url, ImageView imageView, DisplayImageOptions options){
        ImageLoader.getInstance().displayImage(url,imageView,options);
    }
    public static void displayImage(String id,String name, ImageView imageView, DisplayImageOptions options){
        ImageLoader.getInstance().displayImage(getImgUrl(id,name),imageView,options);
    }
    public static String getFileUrl(String resourceId,String name){
        return replaceSpace(String.format("%s%s/%s", AppAction.FILE_RESOURSE_COURSE_URL,resourceId,name));
    }
    public static boolean renameToNewFile(String src, String dest)
    {

        File srcDir = new File(src);  //就文件夹路径
        boolean isOk = srcDir.renameTo(new File(dest));  //dest新文件夹路径，通过renameto修改
        System.out.println("renameToNewFile is OK ? :" +isOk);
        return isOk;


    }
    public static String getAvatar(int userId){
        return getAvatar(userId,0);
    }
    public static String getAvatar(int userId,int version){
        return String.format(AppAction.RESOURCE_URL+"data/users/avatar/%d_medium.jpg?version=%d",userId,version);
    }
    public static String getFeedbackSubject(Context context){
        return String.format("H2H Learn Android %s", AppUtils.getVersionCode(context));
    }
    public static String encodeEmail(String src,String sign){
        return src.replace(sign,EncodeUtil.encode(sign));
    }
    public static String encodeEmail(String src){
        return encodeEmail(src,"+");
    }
    public static String replaceSpace(String src){
        return src.replace(" ","%20");
    }
}
