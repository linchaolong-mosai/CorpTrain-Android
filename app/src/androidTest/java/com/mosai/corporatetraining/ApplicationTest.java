package com.mosai.corporatetraining;

import android.app.Application;
import android.os.Environment;
import android.test.ApplicationTestCase;

import com.mosai.corporatetraining.util.LogUtils;

import java.io.File;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testRename(){
        String src = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+getContext().getPackageName()+File.separator+"CCCCCCCC-CCCC-CCCC-CCCC-111111111116_24_medium.jpg";
        String dest = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+getContext().getPackageName()+File.separator+"888888888CCCCCCCC-CCCC-CCCC-CCCC-111111111116_24_medium.jpg";
        LogUtils.e(renameToNewFile(src,dest)+"");
    }
    private boolean renameToNewFile(String src, String dest)
    {
        File srcDir = new File(src);  //就文件夹路径
        boolean isOk = srcDir.renameTo(new File(dest));  //dest新文件夹路径，通过renameto修改
        System.out.println("renameToNewFile is OK ? :" +isOk);
        LogUtils.e(isOk+"");
        return isOk;
    }
}