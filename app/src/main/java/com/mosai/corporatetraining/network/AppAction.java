package com.mosai.corporatetraining.network;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mosai.corporatetraining.local.UserPF;
import com.mosai.corporatetraining.util.ConverStr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
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
//    private static final String BASE_URL = "https://train-qa.liveh2h.com/tutormeetweb/";
    public static final String URL = "https://train-qa.liveh2h.com/";
    public static final String AVATAR_URL= URL+"tutormeetupload/changeavatar.do";
    private static final String BASE_URL = URL+"corptraining/";
    public static final String IMG_RESOURSE_COURSE_URL = "https://train-qa.liveh2h.com/" + "resources/";
    public static final String FILE_RESOURSE_COURSE_URL = "https://train-qa.liveh2h.com/" + "resources/";

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
        AsyncHttp.getInstance().execute(context, getUrl("corplogin.do"), params, map, AsyncHttp.METHOD_POST, responseHandler);
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
        AsyncHttp.getInstance().execute(context, getUrl("message.do"), map,
                AsyncHttp.METHOD_POST, "application/x-www-form-urlencoded", responseHandler);
    }

    /**
     * 获取当前用户信息
     * @param context
     * @param responseHandler
     */
    public static void getCurrentCtUser(Context context, AsyncHttpResponseHandler responseHandler) {
        AsyncHttp.getInstance().execute(context, getUrl("api/ctUser"), AsyncHttp.METHOD_GET, responseHandler);
    }


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
        AsyncHttp.getInstance().execute(context, getUrl("corpuser.do"), map,
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
        AsyncHttp.getInstance().execute(context, getUrl("corpuser.do"), map, AsyncHttp.METHOD_POST, responseHandler);
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
        AsyncHttp.getInstance().execute(context, getUrl("corpuser.do"), map, AsyncHttp.METHOD_POST, responseHandler);
    }

    public static void submitFeedback(Context context, String subject, String text, AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("subject", subject);
        params.put("text", text);
        params.setUseJsonStreamer(true);
        AsyncHttp.getInstance().execute(context, getUrl("api/ctUser/feedback"),
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
            params.put("userid",UserPF.getInstance().getInt(UserPF.USER_ID,0));
            AsyncHttp.getInstance().getClient().removeAllHeaders();
            AsyncHttp.getInstance().getClient().addHeader(AsyncHttpClient.HEADER_CONTENT_TYPE,"multipart/form-data");
            AsyncHttp.getInstance().getClient().addHeader("apiToken", UserPF.getInstance().getString(UserPF.API_TOKEN, ""));
            AsyncHttp.getInstance().getClient().post(context,AVATAR_URL,params,responseHandler);
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
        //https://train-qa.liveh2h.com/corptraining/api/category/subcategories
        AsyncHttp.getInstance().execute(context,BASE_URL+"api/category/subcategories",new RequestParams(),AsyncHttp.METHOD_GET,null,responseHandler);
    }

    /**
     *Get sub category list by category_id
     * @param context
     * @param categoryid
     * @param responseHandler
     */
    public static void getSubCategorylist(Context context,String categoryid,AsyncHttpResponseHandler responseHandler){
        //https://train-qa.liveh2h.com/corptraining/api/category/subcategories
        AsyncHttp.getInstance().execute(context,BASE_URL+"api/category/subcategories/"+categoryid,new RequestParams(),AsyncHttp.METHOD_GET,null,responseHandler);
    }


    /**
     * Get course list by category_id
     * @param context
     * @param categoryid
     * @param responseHandler
     */
    public static void getCourselist(Context context,String categoryid,AsyncHttpResponseHandler responseHandler){
        //https://train-qa.liveh2h.com/corptraining/api/category/subcategories
        AsyncHttp.getInstance().execute(context,BASE_URL+"api/category/courses/"+categoryid,new RequestParams(),AsyncHttp.METHOD_GET,null,responseHandler);
    }

    /**
     * Get user courses
     * @param context
     * @param responseHandler
     */
    public static void getUserCourses(Context context,AsyncHttpResponseHandler responseHandler){
//        AsyncHttp.getInstance().execute(context,BASE_URL+"corptraining/api/course/user?filter_type=1&offset=0&limit=100&filter_string",new RequestParams(),AsyncHttp.METHOD_GET,null,responseHandler);
          getUserCourseByType(context,SEARCH_COURSE_FILTER_TYPE_ALL,responseHandler);
    }

    /**
     * 通过关键字获取课程
     * @param context
     * @param filter
     * @param responseHandler
     */
    public static void getAllUserCoursesByFilter(Context context,String filter,AsyncHttpResponseHandler responseHandler){
        AsyncHttp.getInstance().execute(context,BASE_URL+"api/course/user?filter_type=1&offset=0&limit=100&filter_string="+filter,new RequestParams(),AsyncHttp.METHOD_GET,null,responseHandler);
//        getUserCourseByType(context,SEARCH_COURSE_FILTER_TYPE_ALL,responseHandler);
    }
    /**
     * 获取自己的课程类型
     * @param context
     * @param type
     * @param responseHandler
     */
    public static void getUserCourseByType(Context context,int type,AsyncHttpResponseHandler responseHandler){
        AsyncHttp.getInstance().execute(context,BASE_URL+"api/course/user?filter_type="+type+"&offset=0&limit=100&filter_string",new RequestParams(),AsyncHttp.METHOD_GET,null,responseHandler);

    }
    /**
     *
     * @param context
     * @param filter
     * @param responseHandler
     */
    public static void getUserCoursesBySearch(Context context,String filter,AsyncHttpResponseHandler responseHandler){
        AsyncHttp.getInstance().execute(context,

                BASE_URL+ "api/course/user?filter_type=1&offset=0&limit=100&filter_string="+filter
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
        AsyncHttp.getInstance().execute(context,getUrl("api/course/rating"),null,hashmap,AsyncHttp.METHOD_PUT,null,responseHandler);
    }

    /**
     * 加入课程
     * @param context
     * @param courseId
     * @param responseHandle
     */

    public static void joinCourse(Context context,String courseId,AsyncHttpResponseHandler responseHandle){
        //corptraining/api/course/join/
        //https://train-qa.liveh2h.com/tutormeetweb/api/course/completePercent/<course_id>
//        HashMap<String,Object> hashmap = new HashMap<>();
//        hashmap.put("completePercent",1);
//        AsyncHttp.getInstance().execute(context,getUrl("api/course/joinOptionalCourse/")+courseId,hashmap,AsyncHttp.METHOD_PUT,responseHandle);
//        AsyncHttp.getInstance().postJsonBody(context,getUrl("api/course/completePercent/")+courseId,null,responseHandle);
        AsyncHttp.getInstance().execute(context,getUrl("api/course/joinOptionalCourse/")+courseId,AsyncHttp.METHOD_POST,responseHandle);
    }

    /**
     * 获取课程评论
     * @param context
     * @param courseId
     * @param asyncHttpResponseHandler
     */
    public static void getCommentsByCourseId(Context context,String courseId,AsyncHttpResponseHandler asyncHttpResponseHandler){
        AsyncHttp.getInstance().execute(context,getUrl("api/course/comments/"+courseId),AsyncHttp.METHOD_GET,asyncHttpResponseHandler);
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
        try {
            comment = ConverStr.toISO_8859_1(comment);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        hashmap.put("comment",comment);
        AsyncHttp.getInstance().execute(context,getUrl("api/course/comment/")+courseId,hashmap,AsyncHttp.METHOD_PUT,responseHandler);

    }
    public static void updateCourseFavorite(Context context,String courseId,boolean isFavorite,AsyncHttpResponseHandler responseHandler){
        HashMap<String,Object> hashmap = new HashMap<>();
        hashmap.put("courseId",courseId);
        hashmap.put("favorite",isFavorite?1:0);
        AsyncHttp.getInstance().execute(context,getUrl("api/course/favorite"),hashmap,AsyncHttp.METHOD_PUT,responseHandler);
    }
    /**
     * 通过CourseId获取课程内容
     * @param context
     * @param courseId
     * @param responseHandler
     */
    public static void getCourseByCourseId(Context context,String courseId,AsyncHttpResponseHandler responseHandler){
        AsyncHttp.getInstance().execute(context,getUrl("api/course/")+courseId,AsyncHttp.METHOD_GET,responseHandler);
    }

    /**
     * 获取course的具体课程class
     * @param context
     * @param courseId
     * @param responseHandler
     */
    public static void getClassesByCourseId(Context context,String courseId,AsyncHttpResponseHandler responseHandler){
        //https://train-qa.liveh2h.com/corptraining/api/course/classes/<course_id>
        AsyncHttp.getInstance().execute(context,getUrl("api/course/classes/")+courseId,AsyncHttp.METHOD_GET,responseHandler);
    }

    /**
     * 获取class的recourse
     * @param context
     * @param classId
     * @param responseHandler
     */
    public static void getResourceByClassId(Context context,String classId,AsyncHttpResponseHandler responseHandler){
        //https://train-qa.liveh2h.com/corptraining/api/class/resources/<class_id>
        AsyncHttp.getInstance().execute(context,getUrl("api/class/resources/")+classId,AsyncHttp.METHOD_GET,responseHandler);
    }

    /**
     * 获取quiz
     * @param context
     * @param quizId
     * @param responseHandler
     */
    public static void getQuizByQuizId(Context context,String quizId,AsyncHttpResponseHandler responseHandler){
        //https://train-qa.liveh2h.com/corptraining/api/basicQuiz/<quiz_id>
        AsyncHttp.getInstance().execute(context,getUrl("api/basicQuiz/")+quizId,AsyncHttp.METHOD_GET,responseHandler);
    }

    /**
     * 获取quiz的questions
     * @param context
     * @param quizId
     * @param responseHandler
     */
    public static void getQuestionslistByQuizId(Context context,String quizId,AsyncHttpResponseHandler responseHandler){
        //https://train-qa.liveh2h.com/corptraining/api/basicQuiz/question/list/<quiz_id>
        AsyncHttp.getInstance().execute(context,getUrl("api/basicQuiz/question/list/")+quizId,AsyncHttp.METHOD_GET,responseHandler);
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
        //https://train-qa.liveh2h.com/corptraining/api/basicQuiz/summary/user/<class_id>/<quiz_id>
        String url = String.format("%s%s/%s/%s",getUrl("api/basicQuiz/summary/"),userId,classId,quizId);
        AsyncHttp.getInstance().execute(context,url,AsyncHttp.METHOD_GET,responseHandler);
    }

    /**
     * 提交Quiz答案
     * @param context
     * @param classId
     * @param questionId
     * @param answerIndex
     * @param asyncHttpResponseHandler
     */
    public static void submitQuizAnswer(Context context,String classId,String questionId,int answerIndex,AsyncHttpResponseHandler asyncHttpResponseHandler){
        HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("answer",answerIndex);
        params.put("classId",classId);
        params.put("questionId",questionId);
        AsyncHttp.getInstance().postJsonBody(context,getUrl("api/basicQuiz/answer"),params,asyncHttpResponseHandler);
//        params.setUseJsonStreamer(true);
//        AsyncHttp.getInstance().execute(context,getUrl("corptraining/api/basicQuiz/answer"),AsyncHttp.METHOD_POST,"application/json",asyncHttpResponseHandler);
    }

    /**
     * 获取Quiz总结
     * @param context
     * @param user
     * @param classId
     * @param quizId
     * @param responseHandler
     */
    public static void getQuizSummary(Context context,String user,String classId,String quizId,AsyncHttpResponseHandler responseHandler){
        //https://train-qa.liveh2h.com/corptraining/api/basicQuiz/summary/user/<class_id>/<quiz_id>
        String url = String.format("%s%s/%s/%s",getUrl("api/basicQuiz/summary/"),user,classId,quizId,responseHandler);
        AsyncHttp.getInstance().execute(context,url,AsyncHttp.METHOD_GET,responseHandler);
    }
    /**
     * 获取surveyId的questions
     * @param context
     * @param surveyId
     * @param responseHandler
     */
    public static void getQuestionslistBySurveyId(Context context,String surveyId,AsyncHttpResponseHandler responseHandler){
        //https://train-qa.liveh2h.com/corptraining/api/basicSurvey/question/list/<quiz_id>
        AsyncHttp.getInstance().execute(context,getUrl("api/basicSurvey/question/list/")+surveyId,AsyncHttp.METHOD_GET,responseHandler);
    }

    /**
     * 提交Survey答案
     * @param context
     * @param classId
     * @param questionId
     * @param answerIndex
     * @param asyncHttpResponseHandler
     */
    public static void submitSurveyAnswer(Context context,String classId,String questionId,int answerIndex,AsyncHttpResponseHandler asyncHttpResponseHandler){
        HashMap<String,Object> params = new HashMap<String,Object>();
        params.put("answer",answerIndex);
        params.put("classId",classId);
        params.put("questionId",questionId);
        AsyncHttp.getInstance().postJsonBody(context,getUrl("api/basicSurvey/answer"),params,asyncHttpResponseHandler);
    }

    /**
     * get Survey summary
     * @param context
     * @param user
     * @param classId
     * @param surveyId
     * @param responseHandler
     */
    public static void getSurveySummary(Context context,String user,String classId,String surveyId,AsyncHttpResponseHandler responseHandler){
        //https://train-qa.liveh2h.com/corptraining/api/basicQuiz/summary/user/<class_id>/<quiz_id>
        String url = String.format("%s%s/%s/%s",getUrl("api/basicSurvey/summary/"),user,classId,surveyId,responseHandler);
        AsyncHttp.getInstance().execute(context,url,AsyncHttp.METHOD_GET,responseHandler);
    }
}
