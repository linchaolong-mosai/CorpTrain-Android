package com.mosai.corporatetraining.activity;

import android.content.Intent;
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
                        //打开本地文件
//                        openFile(filepath);
                        Intent intent = new Intent(context,ShowImageActivity.class);
                        intent.putExtra("filepath",filepath);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(context,ShowImageActivity.class);
                        intent.putExtra("url",url);
                        intent.putExtra("path",path);
                        intent.putExtra("filepath",filepath);
                        startActivity(intent);
                        downloadFile(ClassResourceActivity.this.resources.get(position),position);
                    }
                } else if(resource.getResourceType() == Constants.ResourceTypeVideo){
                    if(new File(filepath).exists()){
                        Intent intent = new Intent(context,VideoActivity.class);
                        intent.putExtra("resource",resource);
                        intent.putExtra("path",filepath);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(context,VideoActivity.class);
                        intent.putExtra("resource",resource);
                        intent.putExtra("url",url);
                        startActivity(intent);
                        downloadFile(ClassResourceActivity.this.resources.get(position),position);
                    }
                } else{
                    if(new File(filepath).exists()){
                        //打开本地文件
                        openFile(filepath);
                    }else{
                        Intent intent = new Intent(context,WebViewActivity.class);
                        intent.putExtra("filename",resource.getName());
                        intent.putExtra("url",url);
                        startActivity(intent);
                        downloadFile(ClassResourceActivity.this.resources.get(position),position);
                    }
                }

            }
        });
        getClassResource();
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

    private void getClassResource() {
        AppAction.getResourceByClassId(this, classes.getClassInfo().getClassId(), new HttpResponseHandler(context,ResourcesRoot.class) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                ResourcesRoot root = (ResourcesRoot) response;
                resources.clear();
                resources.addAll(root.getResources());
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

    private HttpUtils httpUtils = new HttpUtils();
//    private HttpHandler<?> httpHandler;
    private HashMap<String, HttpHandler<?>> httpHandlers = new HashMap<>();

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Map map = new HashMap();
        Iterator iter = httpHandlers.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
//            String key = (String) entry.getKey();
            HttpHandler<?> val = (HttpHandler<?>) entry.getValue();
            val.cancel();
            }
    }

    private void openFile(String path) {
        startActivity(FileUtils.openFile(path));
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
