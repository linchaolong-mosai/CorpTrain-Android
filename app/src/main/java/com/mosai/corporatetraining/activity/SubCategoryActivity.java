package com.mosai.corporatetraining.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mosai.corporatetraining.R;
import com.mosai.corporatetraining.adpter.CategoryAdapter;
import com.mosai.corporatetraining.bean.CategoryRoot;
import com.mosai.corporatetraining.bean.Categories;
import com.mosai.corporatetraining.entity.HttpResponse;
import com.mosai.corporatetraining.network.AppAction;
import com.mosai.corporatetraining.network.HttpResponseHandler;

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
}
