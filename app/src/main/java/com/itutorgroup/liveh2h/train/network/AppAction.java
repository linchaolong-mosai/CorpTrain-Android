package com.itutorgroup.liveh2h.train.network;

import android.content.Context;

import com.itutorgroup.liveh2h.train.local.UserPF;
import com.itutorgroup.liveh2h.train.util.ConverStr;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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

    private static final String URL = "https://app.liveh2h.com/";
    private static final String BASE_URL = URL + "corptraining/";
    private static final String BASE_TME_URL = URL + "tutormeetweb/";
    private static final String CORP_LOGIN = BASE_TME_URL + "corplogin.do";
    private static final String CORP_USER = BASE_TME_URL + "corpuser.do";
    private static final String MESSAGE = BASE_TME_URL + "message.do";
    public static final String AVATAR_URL = "https://upload.liveh2h.com/" + "tutormeetupload/changeavatar.do";
    public static final String RESOURCE_URL = "https://imgsrv.liveh2h.com/";
    public static final String IMG_RESOURSE_COURSE_URL = RESOURCE_URL + "resources/";
    public static final String FILE_RESOURSE_COURSE_URL = RESOURCE_URL + "resources/";

    private static String getUrl(String url) {
        return BASE_URL + url;
    }

    /**
     * forceUpdate
     *
     */
    public static void forceUpdate(Context context, AsyncHttpResponseHandler responseHandler){
        AsyncHttp.getInstance().execute(context, getUrl("api/system/forceUpdateVersion/android"), AsyncHttp.METHOD_GET, responseHandler);
    }

    /**
     * login
     *
     * @param context
     * @param email
     * @param password
     */
    public static void login(Context context, String email, String password, AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("action", "authUser");
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("password", password);
        AsyncHttp.getInstance().execute(context, CORP_LOGIN, params, map, AsyncHttp.METHOD_POST, responseHandler);
    }

    /**
     * forgotPassword
     * @param context
     * @param email
     * @param responseHandler
     */
    public static void forgotPassword(Context context, String email, AsyncHttpResponseHandler responseHandler) {
        Map<String, Object> map = new HashMap<>();
        map.put("action", "forgotPwd");
        map.put("email", email);
        map.put("locale", Locale.getDefault().getLanguage());
        AsyncHttp.getInstance().execute(context, MESSAGE, map,
                AsyncHttp.METHOD_POST, "application/x-www-form-urlencoded", responseHandler);
    }

    /**
     * getCurrentCtUser
     * @param context
     * @param responseHandler
     */
    public static void getCurrentCtUser(Context context, AsyncHttpResponseHandler responseHandler) {
        AsyncHttp.getInstance().execute(context, getUrl("api/ctUser"), AsyncHttp.METHOD_GET, responseHandler);
    }


    /**
     * changePassword
     * @param context
     * @param original
     * @param password
     * @param responseHandler
     */
    public static void changePassword(Context context, String original, String password, AsyncHttpResponseHandler responseHandler) {
        Map<String, Object> map = new HashMap<>();
        map.put("action", "updateUser");
        map.put("original", original);
        map.put("password", password);
        AsyncHttp.getInstance().execute(context, CORP_USER, map,
                AsyncHttp.METHOD_POST, "application/x-www-form-urlencoded", responseHandler);
    }

    /**
     * changeName
     * @param context
     * @param name
     * @param responseHandler
     */
    public static void changeName(Context context, String name, AsyncHttpResponseHandler responseHandler) {
        Map<String, Object> map = new HashMap<>();
        map.put("action", "updateUser");
        map.put("name", name);
        AsyncHttp.getInstance().execute(context, CORP_USER, map, AsyncHttp.METHOD_POST, responseHandler);
    }

    /**
     * changePhoneNumber
     * @param context
     * @param phone
     * @param responseHandler
     */
    public static void changePhoneNumber(Context context, String phone, AsyncHttpResponseHandler responseHandler) {
        Map<String, Object> map = new HashMap<>();
        map.put("action", "updateUser");
        map.put("phone", phone);
        AsyncHttp.getInstance().execute(context, CORP_USER, map, AsyncHttp.METHOD_POST, responseHandler);
    }

    /**
     * submitFeedback
     * @param context
     * @param subject
     * @param text
     * @param responseHandler
     */
    public static void submitFeedback(Context context, String subject, String text, AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("subject", subject);
        params.put("text", text);
        params.setUseJsonStreamer(true);
        AsyncHttp.getInstance().execute(context, getUrl("api/ctUser/feedback"),
                params, AsyncHttp.METHOD_POST, "application/json", responseHandler);
    }


    /**
     * uploadMyicon
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
            AsyncHttp.getInstance().getClient().addHeader("API_TOKEN", UserPF.getInstance().getString(UserPF.API_TOKEN, ""));
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
        //https://cn2.liveh2h.com/corptraining/api/category/subcategories
        AsyncHttp.getInstance().execute(context,BASE_URL+"api/category/subcategories",new RequestParams(),AsyncHttp.METHOD_GET,null,responseHandler);
    }

    /**
     *Get sub category list by category_id
     * @param context
     * @param categoryid
     * @param responseHandler
     */
    public static void getSubCategorylist(Context context,String categoryid,AsyncHttpResponseHandler responseHandler){
        //https://cn2.liveh2h.com/corptraining/api/category/subcategories
        AsyncHttp.getInstance().execute(context,BASE_URL+"api/category/subcategories/"+categoryid,new RequestParams(),AsyncHttp.METHOD_GET,null,responseHandler);
    }



    /**
     * Get course list by category_id
     * @param context
     * @param categoryid
     * @param responseHandler
     */
    public static void getCourselist(Context context,String categoryid,AsyncHttpResponseHandler responseHandler){
        //https://cn2.liveh2h.com/corptraining/api/category/subcategories
        //http://124.42.240.97/corptraining/course/list?category_id=B623ED47-FE06-4C64-AFCE-7BDBCEE93738&descending=true&limit=10&offset=0
        RequestParams params = new RequestParams();
        params.add("category_id", categoryid);
        params.add("descending", "true");
        AsyncHttp.getInstance().execute(context,BASE_URL+"course/list",params,AsyncHttp.METHOD_GET,null,responseHandler);
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
     * getAllUserCoursesByFilter
     * @param context
     * @param filter
     * @param responseHandler
     */
    public static void getAllUserCoursesByFilter(Context context,String filter,AsyncHttpResponseHandler responseHandler){
        AsyncHttp.getInstance().execute(context,BASE_URL+"api/course/user?filter_type=1&offset=0&limit=100&filter_string="+filter,new RequestParams(),AsyncHttp.METHOD_GET,null,responseHandler);
//        getUserCourseByType(context,SEARCH_COURSE_FILTER_TYPE_ALL,responseHandler);
    }
    /**
     * getUserCourseByType
     * @param context
     * @param type
     * @param responseHandler
     */
    public static void getUserCourseByType(Context context,int type,AsyncHttpResponseHandler responseHandler){
        AsyncHttp.getInstance().execute(context,BASE_URL+"api/course/user?filter_type="+type+"&offset=0&limit=100&filter_string",new RequestParams(),AsyncHttp.METHOD_GET,null,responseHandler);

    }
    /**
     *getUserCoursesBySearch
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
     * submitCourseRating
     * @param context
     * @param courseId
     * @param rating
     * @param responseHandler
     */
    public static void submitCourseRating(Context context, String courseId, float rating, AsyncHttpResponseHandler responseHandler) {
        HashMap<String,Object> hashmap = new HashMap<>();
        hashmap.put("courseId",courseId);
        hashmap.put("rating",rating);
        AsyncHttp.getInstance().putJsonBody(context,getUrl("api/course/rating"),hashmap,responseHandler);
    }

    /**
     * joinCourse
     * @param context
     * @param courseId
     * @param responseHandle
     */

    public static void joinCourse(Context context,String courseId,AsyncHttpResponseHandler responseHandle){
        AsyncHttp.getInstance().execute(context,getUrl("api/course/joinOptionalCourse/")+courseId,AsyncHttp.METHOD_POST,responseHandle);
    }

    /**
     * getCommentsByCourseId
     * @param context
     * @param courseId
     * @param asyncHttpResponseHandler
     */
    public static void getCommentsByCourseId(Context context,String courseId,AsyncHttpResponseHandler asyncHttpResponseHandler){
        AsyncHttp.getInstance().execute(context,getUrl("api/course/comments/"+courseId),AsyncHttp.METHOD_GET,asyncHttpResponseHandler);
    }

    /**
     * submitCourseComment
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
        AsyncHttp.getInstance().putJsonBody(context,getUrl("api/course/comment/")+courseId,hashmap,responseHandler);

    }
    public static void updateCourseFavorite(Context context,String courseId,boolean isFavorite,AsyncHttpResponseHandler responseHandler){
        HashMap<String,Object> hashmap = new HashMap<>();
        hashmap.put("courseId",courseId);
        hashmap.put("favorite",isFavorite?1:0);
        AsyncHttp.getInstance().putJsonBody(context,getUrl("api/course/favorite"),hashmap,responseHandler);
    }
    /**
     * getCourseByCourseId
     * @param context
     * @param courseId
     * @param responseHandler
     */
    public static void getCourseByCourseId(Context context,String courseId,AsyncHttpResponseHandler responseHandler){
        AsyncHttp.getInstance().execute(context,getUrl("api/course/")+courseId,AsyncHttp.METHOD_GET,responseHandler);
    }

    /**
     * getClassesByCourseId
     * @param context
     * @param courseId
     * @param responseHandler
     */
    public static void getClassesByCourseId(Context context,String courseId,AsyncHttpResponseHandler responseHandler){
        //https://cn2.liveh2h.com/corptraining/api/course/classes/<course_id>
        AsyncHttp.getInstance().execute(context,getUrl("api/course/classes/")+courseId,AsyncHttp.METHOD_GET,responseHandler);
    }

    /**
     * getResourceByClassId
     * @param context
     * @param classId
     * @param responseHandler
     */
    public static void getResourceByClassId(Context context,String classId,AsyncHttpResponseHandler responseHandler){
        //https://cn2.liveh2h.com/corptraining/api/class/resources/<class_id>
        AsyncHttp.getInstance().execute(context,getUrl("api/class/resources/")+classId,AsyncHttp.METHOD_GET,responseHandler);
    }

    /**
     * getQuizByQuizId
     * @param context
     * @param quizId
     * @param responseHandler
     */
    public static void getQuizByQuizId(Context context,String quizId,AsyncHttpResponseHandler responseHandler){
        AsyncHttp.getInstance().execute(context,getUrl("api/basicQuiz/")+quizId,AsyncHttp.METHOD_GET,responseHandler);
    }

    /**
     * getQuestionslistByQuizId
     * @param context
     * @param quizId
     * @param responseHandler
     */
    public static void getQuestionslistByQuizId(Context context,String quizId,AsyncHttpResponseHandler responseHandler){
        AsyncHttp.getInstance().execute(context,getUrl("api/basicQuiz/question/list/")+quizId,AsyncHttp.METHOD_GET,responseHandler);
    }

    /**
     * getSummaryById
     * @param context
     * @param userId
     * @param classId
     * @param quizId
     * @param responseHandler
     */
    public static void getSummaryById(Context context,String userId,String classId,String quizId,AsyncHttpResponseHandler responseHandler){
        String url = String.format("%s%s/%s/%s",getUrl("api/basicQuiz/summary/"),userId,classId,quizId);
        AsyncHttp.getInstance().execute(context,url,AsyncHttp.METHOD_GET,responseHandler);
    }

    /**
     * submitQuizAnswer
     * @param context
     * @param classId
     * @param questionId
     * @param answerIndex
     * @param asyncHttpResponseHandler
     */
    public static void submitQuizAnswer(Context context,String classId,String questionId,int answerIndex,AsyncHttpResponseHandler asyncHttpResponseHandler){
        HashMap<String,Object> params = new HashMap<>();
        params.put("answer",answerIndex);
        params.put("classId",classId);
        params.put("questionId",questionId);
        AsyncHttp.getInstance().postJsonBody(context,getUrl("api/basicQuiz/answer"),params,asyncHttpResponseHandler);
    }

    /**
     * getQuizSummary
     * @param context
     * @param user
     * @param classId
     * @param quizId
     * @param responseHandler
     */
    public static void getQuizSummary(Context context,String user,String classId,String quizId,AsyncHttpResponseHandler responseHandler){
        String url = String.format("%s%s/%s/%s",getUrl("api/basicQuiz/summary/"),user,classId,quizId,responseHandler);
        AsyncHttp.getInstance().execute(context,url,AsyncHttp.METHOD_GET,responseHandler);
    }
    /**
     * getQuestionslistBySurveyId
     * @param context
     * @param surveyId
     * @param responseHandler
     */
    public static void getQuestionslistBySurveyId(Context context,String surveyId,AsyncHttpResponseHandler responseHandler){
        AsyncHttp.getInstance().execute(context,getUrl("api/basicSurvey/question/list/")+surveyId,AsyncHttp.METHOD_GET,responseHandler);
    }

    /**
     * submitSurveyAnswer
     * @param context
     * @param classId
     * @param questionId
     * @param answerIndex
     * @param asyncHttpResponseHandler
     */
    public static void submitSurveyAnswer(Context context,String classId,String questionId,int answerIndex,AsyncHttpResponseHandler asyncHttpResponseHandler){
        HashMap<String,Object> params = new HashMap<>();
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
        String url = String.format("%s%s/%s/%s",getUrl("api/basicSurvey/summary/"),user,classId,surveyId,responseHandler);
        AsyncHttp.getInstance().execute(context,url,AsyncHttp.METHOD_GET,responseHandler);
    }

    /**
     * getResourcesPercentByClassId
     * @param context
     * @param classId
     * @param httpResponseHandler
     */
    public static void getResourcesPercentByClassId(Context context,String classId,AsyncHttpResponseHandler httpResponseHandler){
        AsyncHttp.getInstance().execute(context,getUrl("api/resource/completePercentList/")+classId,AsyncHttp.METHOD_GET,httpResponseHandler);
    }

    /**
     * 获取class进度
     * @param context
     * @param courseId
     * @param httpResponseHandler
     */
    public static void getClassesPercentByCourseId(Context context,String courseId,AsyncHttpResponseHandler httpResponseHandler){
        AsyncHttp.getInstance().execute(context,getUrl("api/class/completePercentList/")+courseId,AsyncHttp.METHOD_GET,httpResponseHandler);
    }
    /**
     * getResourcePercentById
     * @param context
     * @param classId
     * @param resourceId
     * @param httpResponseHandler
     */
    public static void getResourcePercentById(Context context,String classId,String resourceId,AsyncHttpResponseHandler httpResponseHandler){
        AsyncHttp.getInstance().execute(context,getUrl("api/resource/completePercent/")+resourceId+"/"+classId,AsyncHttp.METHOD_GET,httpResponseHandler);
    }

    /**
     * submitResourcePercent
     * @param context
     * @param classId
     * @param resourceId
     * @param percent
     * @param responseHandle
     */
    public static void submitResourcePercent(Context context,String classId,String resourceId,int percent,AsyncHttpResponseHandler responseHandle){
        HashMap<String,Object> hashmap = new HashMap<>();
        hashmap.put("completePercent",percent);
        AsyncHttp.getInstance().putJsonBody(context,getUrl("api/resource/completePercent/")+resourceId+"/"+classId,hashmap,responseHandle);
    }

    /**
     * Increment viewcout
     * @param context
     * @param courseId
     * @param httpResponseHandler
     */
    public static void incrementViewcount(Context context,String courseId,AsyncHttpResponseHandler httpResponseHandler){
        AsyncHttp.getInstance().execute(context,getUrl("api/course/viewcount/"+courseId),AsyncHttp.METHOD_PUT,httpResponseHandler);
    }
}
