package com.mosai.corporatetraining.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.mosai.corporatetraining.R;
import com.mosai.corporatetraining.adpter.ClassResourceAdapter;
import com.mosai.corporatetraining.bean.classesforcourse.Classes;
import com.mosai.corporatetraining.bean.quiz.Quiz;
import com.mosai.corporatetraining.bean.resourseforclass.Resources;
import com.mosai.corporatetraining.bean.resourseforclass.ResourcesRoot;
import com.mosai.corporatetraining.bean.survey.SurveyQuestion;
import com.mosai.corporatetraining.bean.usercourse.Courses;
import com.mosai.corporatetraining.constants.Constants;
import com.mosai.corporatetraining.entity.HttpResponse;
import com.mosai.corporatetraining.event.Event;
import com.mosai.corporatetraining.network.AppAction;
import com.mosai.corporatetraining.network.HttpResponseHandler;
import com.mosai.corporatetraining.util.LogUtils;
import com.mosai.corporatetraining.util.Utils;
import com.mosai.corporatetraining.util.ViewUtil;
import com.mosai.ui.NoScrollListview;
import com.mosai.utils.FileUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 描述:
 * 作者：周年斌
 * 时间：2016/5/26 0026 16:28
 * 邮箱：nianbin@mosainet.com
 */
public class ClassResourceActivity extends ABaseToolbarActivity {
    private DisplayImageOptions options;
    private ImageView ivIcon;
    private Courses courses;
    private Classes classes;
    private TextView tvName, tvDes, tvTitle;
    private NoScrollListview lv;
    private ClassResourceAdapter adapter;
    private List<Resources> resources = new ArrayList<>();

    @Override
    protected void initDatas() {
        courses = (Courses) getIntent().getSerializableExtra("courses");
        classes = (Classes) getIntent().getSerializableExtra("classes");
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.bg_course_default_cover)
                .showImageOnFail(R.drawable.bg_course_default_cover)
                .considerExifParams(true).displayer(new FadeInBitmapDisplayer(300)).build();
        adapter = new ClassResourceAdapter(this, resources, R.layout.item_listformat_resource);
        lv.setAdapter(adapter);
        tvTitle.setText(classes.getClassInfo().getSubject());
        tvDes.setText(classes.getClassInfo().getDescription());
        tvName.setText(classes.getClassInfo().getSubject());
        Utils.displayImage(classes.getClassInfo().getClassId(), classes.getClassInfo().getImageName(), ivIcon, options);
//        Utils.displayImage(courses.getCourseInfo().getCourseId(),courses.getCourseInfo().getImageName(),ivIcon,options);
    }


    @Override
    protected int setContent() {
        return R.layout.activity_class_resource;
    }

    @Override
    protected void initView() {
        ivIcon = ViewUtil.findViewById(this, R.id.iv_icon);
        tvTitle = ViewUtil.findViewById(this, R.id.tv_title);
        tvName = ViewUtil.findViewById(this, R.id.tv_name);
        tvDes = ViewUtil.findViewById(this, R.id.tv_description);
        lv = ViewUtil.findViewById(this, R.id.lv);
    }
    private void viewDocOnline(String url,Resources resource){
        Intent intent = new Intent(context,WebViewActivity.class);
        intent.putExtra("filename",resource.getName());
        intent.putExtra("url",url);
        intent.putExtra("resource",resource);
        startActivity(intent);
    }
    private void viewVideoOnline(String url,Resources resource){
        Intent intent = new Intent(context,VideoActivity.class);
        intent.putExtra("resource",resource);
        intent.putExtra("url",url);
        startActivity(intent);
    }
    private void viewVideoLocal(String filepath,Resources resource){
        Intent intent = new Intent(context,VideoActivity.class);
        intent.putExtra("resource",resource);
        intent.putExtra("path",filepath);
        startActivity(intent);
    }
    private void viewImgOnline(String url,Resources resource){

    }
    private void viewImgLocal(String filepath,Resources resource){
        Intent intent = new Intent(context,VideoActivity.class);
        intent.putExtra("resource",resource);
        intent.putExtra("path",filepath);
        startActivity(intent);
    }
    @Override
    protected void addListener() {
        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtils.e("onitemclick");
                Resources resource = ClassResourceActivity.this.resources.get(position);
                String path = Utils.getLocalFile(context, resource.getResourceId() + "_" + resource.getName());
                String filepath = Utils.getLocalFile(context, Constants.downloadedtag+resource.getResourceId() + "_" + resource.getName());
                String url = Utils.getFileUrl(resource.getResourceId(), resource.getName().replace(" ", "%20"));
                if (resource.getResourceType() == Constants.ResourceTypeQuiz) {
                    getQuiz(resource);
                } else if (resource.getResourceType() == Constants.ResourceTypeSurvey) {
                    getSurvey(resource);
                } else if(resource.getResourceType() == Constants.ResourceTypeImage){
                    if(new File(filepath).exists()){
                        viewImgLocal(filepath,resource);
                    }else{
                       viewImgOnline(url,resource);
                    }
                } else if(resource.getResourceType() == Constants.ResourceTypeVideo){
                    if(new File(filepath).exists()){
                        viewVideoLocal(path,resource);
                    }else{
                        viewVideoOnline(url,resource);
                    }
                } else{
                    if(new File(filepath).exists()){
                        submitPercentDoc(resource);
                        openDoc(filepath,url,resource.getName(),resource);
                    }else{
                        viewDocOnline(url,resource);
                    }
                }

            }
        });
        adapter.setClickStateTextViewCallback(new ClassResourceAdapter.ClickStateTextViewCallback() {
            @Override
            public void callback(Resources resource,int position) {
                LogUtils.e("callback");
                String url = Utils.getFileUrl(resource.getResourceId(), resource.getName().replace(" ", "%20"));
                String filepath = Utils.getLocalFile(context, Constants.downloadedtag+resource.getResourceId() + "_" + resource.getName());
                if (resource.getResourceType() == Constants.ResourceTypeQuiz) {
                    getQuiz(resource);
                } else if (resource.getResourceType() == Constants.ResourceTypeSurvey) {
                    getSurvey(resource);
                } else if(resource.getResourceType() == Constants.ResourceTypeImage){
                    if(resource.exist){
                        viewImgLocal(filepath,resource);
                    }else{
                        downloadFile(resource,position);
                    }
                } else if(resource.getResourceType() == Constants.ResourceTypeVideo){
                    if(resource.exist){
                        viewVideoLocal(filepath,resource);
                    }else{
                        downloadFile(resource,position);
                    }
                } else{
                    if(new File(filepath).exists()){
                        submitPercentDoc(resource);
                        openDoc(filepath,url,resource.getName(),resource);
                    }else{
                        downloadFile(resource,position);
                    }
                }
            }
        });
        EventBus.getDefault().register(this);
        getClassResource();
    }
    private void submitPercentDoc(final Resources resources){
        if(resources.percent!=100){
            AppAction.submitResourcePercent(context, resources.getClassId(), resources.getResourceId(), 100, new HttpResponseHandler(context,HttpResponse.class) {
                @Override
                public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                            resources.percent=100;
                            adapter.notifyDataSetChanged();
                }
                @Override
                public void onResponeseStart() {
                    showProgressDialog();
                }

                @Override
                public void onResponesefinish() {

                    dismissProgressDialog();
                }

                @Override
                public void onResponeseFail(int statusCode, HttpResponse response) {
                    showHintDialog(response.message);
                }
            });

        }
    }
    public void onEventMainThread(Event.SubmitPercent submitPercent){
        Resources resources = submitPercent.resources;
        if(resources.getResourceType()==Constants.ResourceTypeVideo){

        }else{
            resources.percent=100;
        }
        this.resources.set(this.resources.indexOf(resources),resources);
        adapter.notifyDataSetChanged();
    }
    private void getSurvey(final Resources resource) {
        AppAction.getQuestionslistBySurveyId(this, resource.getResourceId(), new HttpResponseHandler(context,HttpResponse.class) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                try {
                    ArrayList<SurveyQuestion> surveyQuestions = new ArrayList<SurveyQuestion>();
                    JSONArray questions = new JSONObject(responseString).getJSONArray("questions");
                    for (int i = 0; i < questions.length(); i++) {
                        JSONObject question = questions.getJSONObject(i);
                        SurveyQuestion surveyQuestion = new Gson().fromJson(question.toString(), SurveyQuestion.class);
                        surveyQuestions.add(surveyQuestion);
                    }
                    Intent intent = new Intent(context, SurveyQuestionsActivity.class);
                    intent.putExtra("questions", surveyQuestions);
                    intent.putExtra("resource", resource);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onResponeseStart() {
                showProgressDialog();
            }

            @Override
            public void onResponesefinish() {
                dismissProgressDialog();
            }

            @Override
            public void onResponeseFail(int statusCode, HttpResponse response) {
                showHintDialog(response.message);
            }
        });
    }

    private void getQuiz(final Resources resource) {
        AppAction.getQuizByQuizId(this, resource.getResourceId(), new HttpResponseHandler(context,HttpResponse.class) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                try {
                    JSONObject result = new JSONObject(responseString);
                    Quiz quiz = new Gson().fromJson(result.optJSONObject("quiz").toString(), Quiz.class);
                    Intent intent = new Intent(context, QuizActivity.class);
                    intent.putExtra("quiz", quiz);
                    intent.putExtra("resource", resource);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onResponeseStart() {
                showProgressDialog();
            }

            @Override
            public void onResponesefinish() {
                dismissProgressDialog();
            }

            @Override
            public void onResponeseFail(int statusCode, HttpResponse response) {
                showHintDialog(response.message);
            }
        });
    }
    private void getResourcesPercent(){
        AppAction.getResourcesPercentByClassId(this, classes.getClassInfo().getClassId(), new HttpResponseHandler(context,ResourcesRoot.class) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    JSONObject ids = jsonObject.optJSONObject("completed_percent_list");
                    Iterator<String> keys = ids.keys();
                    while(keys.hasNext()){
                        String key = keys.next();
                        for(Resources resources:ClassResourceActivity.this.resources){
                            if(TextUtils.equals(key,resources.getResourceId())){
                                resources.percent = ids.optInt(key);
                                continue;
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onResponeseStart() {
                showProgressDialog();
            }

            @Override
            public void onResponesefinish() {
                dismissProgressDialog();
            }

            @Override
            public void onResponeseFail(int statusCode, HttpResponse response) {
                showHintDialog(response.message);
            }
        });
    }

    private void getClassResource() {
        AppAction.getResourceByClassId(this, classes.getClassInfo().getClassId(), new HttpResponseHandler(context,ResourcesRoot.class) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                ResourcesRoot root = (ResourcesRoot) response;
                resources.clear();
                resources.addAll(root.getResources());
                for(Resources resource : resources){
                    String filepath = Utils.getLocalFile(context, Constants.downloadedtag+resource.getResourceId() + "_" + resource.getName());
                    resource.exist=new File(filepath).exists();
                }
                getResourcesPercent();
//                adapter.notifyDataSetChanged();
            }
            @Override
            public void onResponeseStart() {
                showProgressDialog();
            }

            @Override
            public void onResponesefinish() {
                dismissProgressDialog();
            }

            @Override
            public void onResponeseFail(int statusCode, HttpResponse response) {
                showHintDialog(response.message);
            }
        });
    }

    private HttpUtils httpUtils = new HttpUtils();
//    private HttpHandler<?> httpHandler;
    private HashMap<String, HttpHandler<?>> httpHandlers = new HashMap<>();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Iterator iter = httpHandlers.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            HttpHandler<?> val = (HttpHandler<?>) entry.getValue();
            val.cancel();
            }
        EventBus.getDefault().unregister(this);
    }
    private void openDoc(String path, String url, String filename, Resources resources) {
        Intent intent = FileUtils.openFile(path);
        if(intent==null){
            viewDocOnline(url,resources);
        }else{
            startActivity(intent);
        }
    }

    private void downloadFile(final Resources recources, final int position) {
        final String filepath = Utils.getLocalFile(context, Constants.downloadedtag+recources.getResourceId() + "_" + recources.getName());
        final String path = Utils.getLocalFile(this, recources.getResourceId() + "_" + recources.getName());
        String url = Utils.getFileUrl(recources.getResourceId(), recources.getName().replace(" ", "%20"));
        if (!httpHandlers.containsKey(path)) {
            HttpHandler<?> httpHandler = httpUtils.download(url,
                    path, true, true,
                    new RequestCallBack<File>() {
                        @Override
                        public void onSuccess(ResponseInfo<File> responseInfo) {
                            LogUtils.e("下载成功");
                            Utils.renameToNewFile(path,filepath);
                            httpHandlers.get(path).cancel();
                            resources.get(position).exist=true;
                            resources.get(position).showProgress=false;
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            LogUtils.e("下载失败" + s);
                            httpHandlers.get(path).cancel();
                        }

                        @Override
                        public void onStart() {
                            super.onStart();
                        }

                        @Override
                        public void onLoading(long total, long current, boolean isUploading) {
                            super.onLoading(total, current, isUploading);
                            LogUtils.e(String.format("name:%s,total:%s,current:%s", recources.getName(), total + "", current + ""));
                           resources.get(position).showProgress=true;
                            resources.get(position).totalcount=total;
                            resources.get(position).currentcount=current;
                            adapter.notifyDataSetChanged();
                        }
                    });
            httpHandlers.put(path, httpHandler);
        }


    }
}
