package com.mosai.corporatetraining;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.mosai.corporatetraining.entity.HttpResponse;
import com.mosai.corporatetraining.network.AppAction;
import com.mosai.corporatetraining.network.HttpResponseHandler;
import com.mosai.corporatetraining.util.LogUtils;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testRating(){
        AppAction.submitCourseRating(getContext(),"AAAAAAAA-AAAA-AAAA-AAAA-111111111111",2.0f, new HttpResponseHandler(HttpResponse.class) {
            @Override
            public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                LogUtils.e(response.toString());
            }

            @Override
            public void onResponeseFail(int statusCode, HttpResponse response) {
                super.onResponeseFail(statusCode, response);
                LogUtils.e(response.toString());
            }

            @Override
            public void onResponesefinish() {
                super.onResponesefinish();
                LogUtils.e("finish");
            }
        });
    }
}