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
    public static int  SEARCH_USER_COURSE_FILTER_TYPE_UPCOMING = 0	;
    public static int  SEARCH_COURSE_FILTER_TYPE_ALL = 1;
    public static int SEARCH_USER_COURSE_FILTER_TYPE_FAVORITE = 2;
    public static int SEARCH_USER_COURSE_FILTER_TYPE_MANDATORY = 3;
    public static int SEARCH_USER_COURSE_FILTER_TYPE_SUBSCRIBED = 4;
    public static int SEARCH_USER_COURSE_FILTER_TYPE_UNFINISHED = 5;
    public static int  SEARCH_USER_COURSE_FILTER_TYPE_FINISHED = 6;

    private static final String BASE_URL = "https://train-qa.liveh2h.com/";
    public static final String IMG_RESOURSE_COURSE_URL = BASE_URL + "resources/";
    public static final String FILE_RESOURSE_COURSE_URL = BASE_URL + "resources/";

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



            AsyncHttp.getInstance().execute(context, BASE_URL+"tutormeetupload/changeavatar.do",
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
        AsyncHttp.getInstance().execute(context,BASE_URL+"tutormeetweb/api/category/subcategories",new RequestParams(),AsyncHttp.METHOD_GET,null,responseHandler);
    }

    /**
     *Get sub category list by category_id
     * @param context
     * @param categoryid
     * @param responseHandler
     */
    public static void getSubCategorylist(Context context,String categoryid,AsyncHttpResponseHandler responseHandler){
        //https://train-qa.liveh2h.com/tutormeetweb/api/category/subcategories
        AsyncHttp.getInstance().execute(context,BASE_URL+"tutormeetweb/api/category/subcategories/"+categoryid,new RequestParams(),AsyncHttp.METHOD_GET,null,responseHandler);
    }


    /**
     * Get course list by category_id
     * @param context
     * @param categoryid
     * @param responseHandler
     */
    public static void getCourselist(Context context,String categoryid,AsyncHttpResponseHandler responseHandler){
        //https://train-qa.liveh2h.com/tutormeetweb/api/category/subcategories
        AsyncHttp.getInstance().execute(context,BASE_URL+"tutormeetweb/api/category/courses/"+categoryid,new RequestParams(),AsyncHttp.METHOD_GET,null,responseHandler);
    }

    /**
     * Get user courses
     * @param context
     * @param responseHandler
     */
    public static void getUserCourses(Context context,AsyncHttpResponseHandler responseHandler){
//        AsyncHttp.getInstance().execute(context,BASE_URL+"tutormeetweb/api/course/user?filter_type=1&offset=0&limit=100&filter_string",new RequestParams(),AsyncHttp.METHOD_GET,null,responseHandler);
          getUserCourseByType(context,SEARCH_COURSE_FILTER_TYPE_ALL,responseHandler);
    }

    /**
     * 获取自己的课程类型
     * @param context
     * @param type
     * @param responseHandler
     */
    public static void getUserCourseByType(Context context,int type,AsyncHttpResponseHandler responseHandler){
        AsyncHttp.getInstance().execute(context,BASE_URL+"tutormeetweb/api/course/user?filter_type="+type+"&offset=0&limit=100&filter_string",new RequestParams(),AsyncHttp.METHOD_GET,null,responseHandler);

    }
    /**
     *
     * @param context
     * @param filter
     * @param responseHandler
     */
    public static void getUserCoursesBySearch(Context context,String filter,AsyncHttpResponseHandler responseHandler){
        AsyncHttp.getInstance().execute(context,

                BASE_URL+ "tutormeetweb/api/course/user?filter_type=1&offset=0&limit=100&filter_string="+filter
                ,new RequestParams(),AsyncHttp.METHOD_GET,null,responseHandler);
    }

    /**
     * 课程评分
     * @param context
     * @param courseId
     * @param rating
     * @param responseHandler
     */
    public static void submitCourseRating(Context context, String courseId, float rating, AsyncHttpResponseHandler responseHandler) {
        HashMap<String,Object> hashmap = new HashMap<>();
        hashmap.put("courseId",courseId);
        hashmap.put("rating",rating);
        AsyncHttp.getInstance().execute(context,getUrl("tutormeetweb/api/course/rating"),null,hashmap,AsyncHttp.METHOD_PUT,null,responseHandler);
    }

    /**
     * 加入课程
     * @param context
     * @param courseId
     * @param responseHandle
     */
    public static void joinCourse(Context context,String courseId,AsyncHttpResponseHandler responseHandle){
        //tutormeetweb/api/course/join/
        RequestParams params = new RequestParams();
        params.setUseJsonStreamer(true);
        AsyncHttp.getInstance().execute(context,getUrl("tutormeetweb/api/course/join/")+courseId,params,AsyncHttp.METHOD_POST, "application/json",responseHandle);

    }

    /**
     * 获取课程评论
     * @param context
     * @param courseId
     * @param asyncHttpResponseHandler
     */
    public static void getCommentsByCourseId(Context context,String courseId,AsyncHttpResponseHandler asyncHttpResponseHandler){
        AsyncHttp.getInstance().execute(context,getUrl("tutormeetweb/api/course/comments/"+courseId),AsyncHttp.METHOD_GET,asyncHttpResponseHandler);
    }

    /**
     * 提交课程评论
     * @param context
     * @param courseId
     * @param comment
     * @param responseHandler
     */
    public static void submitCourseComment(Context context,String courseId,String comment,AsyncHttpResponseHandler responseHandler){
        HashMap<String,Object> hashmap = new HashMap<>();
        hashmap.put("comment",comment);
        AsyncHttp.getInstance().execute(context,getUrl("tutormeetweb/api/course/comment/")+courseId,hashmap,AsyncHttp.METHOD_PUT,responseHandler);

    }

    /**
     * 通过CourseId获取课程内容
     * @param context
     * @param courseId
     * @param responseHandler
     */
    public static void getCourseByCourseId(Context context,String courseId,AsyncHttpResponseHandler responseHandler){
        AsyncHttp.getInstance().execute(context,getUrl("tutormeetweb/api/course/")+courseId,AsyncHttp.METHOD_GET,responseHandler);
    }

    /**
     * 获取course的具体课程class
     * @param context
     * @param courseId
     * @param responseHandler
     */
    public static void getClassesByCourseId(Context context,String courseId,AsyncHttpResponseHandler responseHandler){
        //https://train-qa.liveh2h.com/tutormeetweb/api/course/classes/<course_id>
        AsyncHttp.getInstance().execute(context,getUrl("tutormeetweb/api/course/classes/")+courseId,AsyncHttp.METHOD_GET,responseHandler);
    }

    /**
     * 获取class的recourse
     * @param context
     * @param classId
     * @param responseHandler
     */
    public static void getResourceByClassId(Context context,String classId,AsyncHttpResponseHandler responseHandler){
        //https://train-qa.liveh2h.com/tutormeetweb/api/class/resources/<class_id>
        AsyncHttp.getInstance().execute(context,getUrl("tutormeetweb/api/class/resources/")+classId,AsyncHttp.METHOD_GET,responseHandler);
    }

    /**
     * 获取quiz
     * @param context
     * @param quizId
     * @param responseHandler
     */
    public static void getQuizByQuizId(Context context,String quizId,AsyncHttpResponseHandler responseHandler){
        //https://train-qa.liveh2h.com/tutormeetweb/api/basicQuiz/<quiz_id>
        AsyncHttp.getInstance().execute(context,getUrl("tutormeetweb/api/basicQuiz/")+quizId,AsyncHttp.METHOD_GET,responseHandler);
    }

    /**
     * 获取quiz的questions
     * @param context
     * @param quizId
     * @param responseHandler
     */
    public static void getQuestionslistByQuizId(Context context,String quizId,AsyncHttpResponseHandler responseHandler){
        //https://train-qa.liveh2h.com/tutormeetweb/api/basicQuiz/question/list/<quiz_id>
        AsyncHttp.getInstance().execute(context,getUrl("tutormeetweb/api/basicQuiz/question/list/")+quizId,AsyncHttp.METHOD_GET,responseHandler);
    }

    /**
     * 获取quiz的测试结果
     * @param context
     * @param userId
     * @param classId
     * @param quizId
     * @param responseHandler
     */
    public static void getSummaryById(Context context,String userId,String classId,String quizId,AsyncHttpResponseHandler responseHandler){
        //https://train-qa.liveh2h.com/tutormeetweb/api/basicQuiz/summary/user/<class_id>/<quiz_id>
        String url = String.format("%s%s/%s/%s",getUrl("tutormeetweb/api/basicQuiz/summary/"),userId,classId,quizId);
        AsyncHttp.getInstance().execute(context,url,AsyncHttp.METHOD_GET,responseHandler);
    }
    public static void submitQuizAnswer(Context context,String classId,String questionId,int answerIndex,AsyncHttpResponseHandler asyncHttpResponseHandler){
        /**
         *  tutormeetweb/api/basicQuiz/answer
         * "answer":1,
         "classId": "BBBBBBBB-BBBB-BBBB-BBBB-111111111111",
         "questionId": "FFFFFFFF-FFFF-FFFF-FFFF-111111111111"
         */

        HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("answer",answerIndex);
        params.put("classId",classId);
        params.put("questionId",questionId);
        AsyncHttp.getInstance().postJsonBody(context,getUrl("tutormeetweb/api/basicQuiz/answer"),params,asyncHttpResponseHandler);
//        params.setUseJsonStreamer(true);
//        AsyncHttp.getInstance().execute(context,getUrl("tutormeetweb/api/basicQuiz/answer"),AsyncHttp.METHOD_POST,"application/json",asyncHttpResponseHandler);
    }

    public static void getQuizSummary(Context context,String user,String classId,String quizId,AsyncHttpResponseHandler responseHandler){
        //https://train-qa.liveh2h.com/tutormeetweb/api/basicQuiz/summary/user/<class_id>/<quiz_id>
        String url = String.format("%s%s/%s/%s",getUrl("tutormeetweb/api/basicQuiz/summary/"),user,classId,quizId,responseHandler);
        AsyncHttp.getInstance().execute(context,url,AsyncHttp.METHOD_GET,responseHandler);
    }
}
