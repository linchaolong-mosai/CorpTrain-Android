package com.itutorgroup.liveh2h.train.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.itutorgroup.liveh2h.train.R;
import com.itutorgroup.liveh2h.train.adpter.CategoryAdapter;
import com.itutorgroup.liveh2h.train.bean.Categories;
import com.itutorgroup.liveh2h.train.bean.CategoryRoot;
import com.itutorgroup.liveh2h.train.constants.TrackName;
import com.itutorgroup.liveh2h.train.entity.HttpResponse;
import com.itutorgroup.liveh2h.train.network.AppAction;
import com.itutorgroup.liveh2h.train.network.HttpResponseHandler;
import com.itutorgroup.liveh2h.train.util.AnalyticsUtils;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryActivity extends BaseToolbarActivity {
   private ListView listView;
    private List<Categories> categories = new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private String categoryId;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        initView();
        initDatas();
        addListener();
        ((TextView) findViewById(R.id.tv_title)).setText(name);
    }

    protected void initDatas() {
        categoryAdapter = new CategoryAdapter(context,categories,R.layout.item_listformat_category);
        listView.setAdapter(categoryAdapter);
        categoryId = getIntent().getStringExtra("categoryId");
        name = getIntent().getStringExtra("name");
    }

    protected int setContent() {
        return R.layout.activity_category;
    }

    protected void initView() {
        listView = (ListView) findViewById(R.id.lv);
    }

    protected void addListener() {
        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                back();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context,CourseFindByCategoryActivity.class);
                intent.putExtra("categoryId",categories.get(position).getCategoryId());
                intent.putExtra("name",categories.get(position).getName());
                startActivity(intent);
            }
        });
        getDatas();
    }
    private void getDatas(){
        AppAction.getSubCategorylist(context, categoryId,new HttpResponseHandler(context,CategoryRoot.class) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                getSubCategoryListEvent();
                CategoryRoot categoryRoot = (CategoryRoot) response;
                    SubCategoryActivity.this.categories.clear();
                    SubCategoryActivity.this.categories.addAll(categoryRoot.getCategories());
                    categoryAdapter.notifyDataSetChanged();
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

    /****************************************Analytics**************************/
    @Override
    public String getAnalyticsTrackName() {
        return TrackName.SubcategoryScreen;
    }
    private void getSubCategoryListEvent(){
        AnalyticsUtils.setEvent(context,R.array.GetSubCategoryList);
    }
    /****************************************Analytics**************************/
}
