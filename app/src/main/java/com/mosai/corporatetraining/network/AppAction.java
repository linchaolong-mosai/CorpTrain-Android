package com.mosai.corporatetraining.network;

import android.content.Context;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Rays on 16/5/12.
 */
public class AppAction {
    private static final String BASE_URL = "https://train-qa.liveh2h.com/";
    public static final String IMG_RESOURSE_COURSE_URL = BASE_URL + "resources/";


    public static String getUrl(String url) {
        return BASE_URL + url;
    }

    /**
     * 登录
     *
     * @param context
     * @param email
     * @param password
     */
    public static void login(Context context, String email, String password, AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("action", "authUser");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("email", email);
        map.put("password", password);
        AsyncHttp.getInstance().execute(context, getUrl("tutormeetweb/corplogin.do"), params, map, AsyncHttp.METHOD_POST, responseHandler);
    }

    /**
     * 找回密码
     * @param context
     * @param email
     * @param responseHandler
     */
    public static void forgotPassword(Context context, String email, AsyncHttpResponseHandler responseHandler) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("action", "forgotPwd");
        map.put("email", email);
        map.put("locale", Locale.getDefault().getLanguage());
        AsyncHttp.getInstance().execute(context, getUrl("tutormeetweb/message.do"), map,
                AsyncHttp.METHOD_POST, "application/x-www-form-urlencoded", responseHandler);
    }

    /**
     * 获取当前用户信息
     * @param context
     * @param responseHandler
     */
    public static void getCurrentCtUser(Context context, AsyncHttpResponseHandler responseHandler) {
        AsyncHttp.getInstance().execute(context, getUrl("tutormeetweb/api/ctUser"), AsyncHttp.METHOD_GET, responseHandler);
    }

    /**
     * 修改用户信息
     * @param context
     * @param name
     * @param phone
     * @param responseHandler
     */
    /*public static void updateUserInfo(Context context, String name, String phone, AsyncHttpResponseHandler responseHandler) {
        UserPF userPF = UserPF.getInstance();
        RequestParams params = new RequestParams();
        params.put("name", name == null ? userPF.getString(UserPF.USER_NAME, "") : name);
        params.put("phone", phone == null ? userPF.getString(UserPF.PHONE, "") : phone);
        params.put("email", userPF.getString(UserPF.USER_EMAIL, ""));
        params.put("userSn", userPF.getInt(UserPF.USER_ID, 0));
        params.put("ctUserId", userPF.getString(UserPF.CT_USER_ID, ""));
        params.put("ctCompanyId", userPF.getString(UserPF.CT_COMPANY_ID, ""));
        params.put("ctRole", userPF.getInt(UserPF.CT_ROLE, 0));
        params.put("timeZone", userPF.getString(UserPF.TIME_ZONE, ""));
        params.put("ctGroupIds", userPF.getString(UserPF.CT_GROUPS, ""));
        AsyncHttp.getInstance().execute(context, "api/ctUser/update", params, AsyncHttp.METHOD_PUT, responseHandler);
    }*/

    /**
     * 修改密码
     * @param context
     * @param original
     * @param password
     * @param responseHandler
     */
    public static void changePassword(Context context, String original, String password, AsyncHttpResponseHandler responseHandler) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("action", "updateUser");
        map.put("original", original);
        map.put("password", password);
        AsyncHttp.getInstance().execute(context, getUrl("tutormeetweb/corpuser.do"), map,
                AsyncHttp.METHOD_POST, "application/x-www-form-urlencoded", responseHandler);
    }

    /**
     * 修改用户名
     * @param context
     * @param name
     * @param responseHandler
     */
    public static void changeName(Context context, String name, AsyncHttpResponseHandler responseHandler) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("action", "updateUser");
        map.put("name", name);
        AsyncHttp.getInstance().execute(context, getUrl("tutormeetweb/corpuser.do"), map, AsyncHttp.METHOD_POST, responseHandler);
    }

    /**
     * 修改电话号码
     * @param context
     * @param phone
     * @param responseHandler
     */
    public static void changePhoneNumber(Context context, String phone, AsyncHttpResponseHandler responseHandler) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("action", "updateUser");
        map.put("phone", phone);
        AsyncHttp.getInstance().execute(context, getUrl("tutormeetweb/corpuser.do"), map, AsyncHttp.METHOD_POST, responseHandler);
    }

    public static void submitFeedback(Context context, String subject, String text, AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("subject", subject);
        params.put("text", text);
        params.setUseJsonStreamer(true);
        AsyncHttp.getInstance().execute(context, getUrl("tutormeetweb/api/ctUser/feedback"),
                params, AsyncHttp.METHOD_POST, "application/json", responseHandler);
    }


    /**
     * 上传图片
     * @param context
     * @param path
     * @param responseHandler
     */
    public static void uploadMyicon(Context context, String path,AsyncHttpResponseHandler responseHandler){
        RequestParams params = new RequestParams();
        try {
            params.put("file",new File(path));
            AsyncHttp.getInstance().getClient().removeAllHeaders();



            AsyncHttp.getInstance().execute(context, "https://train-qa.liveh2h.com/tutormeetupload/changeavatar.do",
                    params, AsyncHttp.METHOD_POST,"multipart/form-data;boundary=----WebKitFormBoundarytl61TC9tokeItvRA;image/JPEG", responseHandler);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     *Get top category list
     * @param context
     * @param responseHandler
     */
    public static void getTopCategoryList(Context context,AsyncHttpResponseHandler responseHandler){
        //https://train-qa.liveh2h.com/tutormeetweb/api/category/subcategories
        AsyncHttp.getInstance().execute(context,"https://train-qa.liveh2h.com/tutormeetweb/api/category/subcategories",new RequestParams(),AsyncHttp.METHOD_GET,null,responseHandler);
    }

    /**
     *Get sub category list by category_id
     * @param context
     * @param categoryid
     * @param responseHandler
     */
    public static void getSubCategorylist(Context context,String categoryid,AsyncHttpResponseHandler responseHandler){
        //https://train-qa.liveh2h.com/tutormeetweb/api/category/subcategories
        AsyncHttp.getInstance().execute(context,"https://train-qa.liveh2h.com/tutormeetweb/api/category/subcategories/"+categoryid,new RequestParams(),AsyncHttp.METHOD_GET,null,responseHandler);
    }


    /**
     * Get course list by category_id
     * @param context
     * @param categoryid
     * @param responseHandler
     */
    public static void getCourselist(Context context,String categoryid,AsyncHttpResponseHandler responseHandler){
        //https://train-qa.liveh2h.com/tutormeetweb/api/category/subcategories
        AsyncHttp.getInstance().execute(context,"https://train-qa.liveh2h.com/tutormeetweb/api/category/courses/"+categoryid,new RequestParams(),AsyncHttp.METHOD_GET,null,responseHandler);
    }

    /**
     * Get user courses
     * @param context
     * @param responseHandler
     */
    public static void getUserCourses(Context context,AsyncHttpResponseHandler responseHandler){
        AsyncHttp.getInstance().execute(context,"https://train-qa.liveh2h.com/tutormeetweb/api/course/user?filter_type=1&offset=0&limit=100&filter_string",new RequestParams(),AsyncHttp.METHOD_GET,null,responseHandler);
    }
}
