/*
* Copyright (C) 2015 Andy Ke <dictfb@gmail.com>
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/


package com.itutorgroup.liveh2h.train.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.itutorgroup.liveh2h.train.R;
import com.itutorgroup.liveh2h.train.bean.resourseforclass.Resources;
import com.itutorgroup.liveh2h.train.entity.HttpResponse;
import com.itutorgroup.liveh2h.train.event.Event;
import com.itutorgroup.liveh2h.train.network.AppAction;
import com.itutorgroup.liveh2h.train.network.HttpResponseHandler;
import com.itutorgroup.liveh2h.train.util.LogUtils;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

import de.greenrobot.event.EventBus;

public class VideoActivity extends BaseActivity implements UniversalVideoView.VideoViewCallback {

    private static final String TAG = "VideoActivity";
    private static final String SEEK_POSITION_KEY = "SEEK_POSITION_KEY";
    UniversalVideoView mVideoView;
    UniversalMediaController mMediaController;

    View mVideoLayout;
//    TextView mStart;

    private int mSeekPosition;
    private int cachedHeight;
    private boolean isFullscreen;
    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vedio);
        EventBus.getDefault().register(this);
        resources = (Resources) getIntent().getSerializableExtra("resource");
        mVideoLayout = findViewById(R.id.video_layout);
        mVideoView = (UniversalVideoView) findViewById(R.id.videoView);
        mVideoView.setmCanSeekForward(false);
        mMediaController = (UniversalMediaController) findViewById(R.id.media_controller);
        mMediaController.mScaleButton.setVisibility(View.GONE);
        mMediaController.setTitle(resources.getName());
        mVideoView.setMediaController(mMediaController);
        setVideoAreaSize();
        mVideoView.setVideoViewCallback(this);
        mVideoView.start();
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                LogUtils.e("onCompletion");
                AppAction.submitResourcePercent(VideoActivity.this, resources.getClassId(), resources.getResourceId(), 100, new HttpResponseHandler(VideoActivity.this, HttpResponse.class) {
                    @Override
                    public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                        Event.SubmitPercent submitPercent = new Event.SubmitPercent();
                        resources.percent = 100;
                        submitPercent.resources = resources;
                        EventBus.getDefault().post(submitPercent);
                    }
                    @Override
                    public void onResponeseStart() {
                        showTextProgressDialog(context.getString(R.string.uploading));
                    }

                    @Override
                    public void onResponesefinish() {

                        dismissTextProgressDialog();
                    }

                    @Override
                    public void onResponeseFail(int statusCode, HttpResponse response) {
                        showHintDialog(response.message);
                    }
                });
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause ");
        if (mVideoView != null && mVideoView.isPlaying()) {
            mSeekPosition = mVideoView.getCurrentPosition();
            Log.d(TAG, "onPause mSeekPosition=" + mSeekPosition);
            mVideoView.pause();
        }
    }


    /**
     * 置视频区域大小
     */
    private void setVideoAreaSize() {
        mVideoLayout.post(new Runnable() {
            @Override
            public void run() {
                int width = mVideoLayout.getWidth();
                cachedHeight = (int) (width * 405f / 720f);
                ViewGroup.LayoutParams videoLayoutParams = mVideoLayout.getLayoutParams();
                videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                videoLayoutParams.height = cachedHeight;
                mVideoLayout.setLayoutParams(videoLayoutParams);
                setUrlOrPath();
                mVideoView.requestFocus();
            }
        });
    }




    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState Position=" + mVideoView.getCurrentPosition());
        outState.putInt(SEEK_POSITION_KEY, mSeekPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        mSeekPosition = outState.getInt(SEEK_POSITION_KEY);
        Log.d(TAG, "onRestoreInstanceState Position=" + mSeekPosition);
    }


    @Override
    public void onScaleChange(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
        if (isFullscreen) {
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mVideoLayout.setLayoutParams(layoutParams);

        } else {
            finish();

        }

    }


    @Override
    public void onPause(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onPause UniversalVideoView callback");

        LogUtils.e("onPause UniversalVideoView callback");
    }

    private boolean first = true;

    @Override
    public void onStart(MediaPlayer mediaPlayer) {
        LogUtils.e("onStart UniversalVideoView callback");
        viewLastProgress();
    }

    @Override
    public void onBufferingStart(MediaPlayer mediaPlayer) {
        LogUtils.d("onBufferingStart UniversalVideoView callback");
    }

    @Override
    public void onBufferingEnd(MediaPlayer mediaPlayer) {
        LogUtils.d("onBufferingEnd UniversalVideoView callback");
    }

    public void onEventMainThread(Event.SubmitPercent submitPercent) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        submitPercent();
    }

    private void submitPercent() {
        if (resources.percent != 100) {
            int current = mVideoView.getCurrentPosition();
            int duration = mVideoView.getDuration();
            final int percent = (int) (current * 0.01 / duration * 100 * 100);
            AppAction.submitResourcePercent(this, resources.getClassId(), resources.getResourceId(), percent, new HttpResponseHandler(this, HttpResponse.class) {
                @Override
                public void onResponeseSucess(int statusCode, HttpResponse response, String responseString) {
                    Event.SubmitPercent submitPercent = new Event.SubmitPercent();
                    submitPercent.resources = resources;
                    submitPercent.resources.percent = percent;
                    EventBus.getDefault().post(submitPercent);
                    VideoActivity.super.onBackPressed();

                }
                @Override
                public void onResponeseStart() {
                    showTextProgressDialog(context.getString(R.string.uploading));
                }

                @Override
                public void onResponesefinish() {

                    dismissTextProgressDialog();
                }

                @Override
                public void onResponeseFail(int statusCode, HttpResponse response) {
                    showHintDialog(response.message);
                }
            });

        } else {
            VideoActivity.super.onBackPressed();
        }
    }

    private void viewLastProgress() {
        if (first&&getIntent().hasExtra("path")) {
            LogUtils.e("mVideoView.getDuration()" + mVideoView.getDuration());
            if (resources.percent != 100) {
                int current = (int) (mVideoView.getDuration() * 0.01 * resources.percent);
                if (mVideoView.isPlaying()) {
                    mVideoView.pause();
                    mVideoView.seekTo(current);
                    first = false;
                    mVideoView.start();
                }
            }


        }
    }
    private void setUrlOrPath() {
        String videoUrl = getIntent().getStringExtra("url");
        String path = getIntent().getStringExtra("path");
        if (!TextUtils.isEmpty(videoUrl)) {
            mVideoView.setVideoPath(videoUrl);
        }
        if (!TextUtils.isEmpty(path)) {
            mVideoView.setVideoPath(path);
        }
    }
}
