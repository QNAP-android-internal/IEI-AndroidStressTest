/*
 * Copyright(c) 2018 Bob Shen <ayst.shen@foxmail.com>
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

package com.ayst.stresstest.test.video;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;

import com.ayst.stresstest.R;
import com.ayst.stresstest.test.base.BaseTimingTestFragment;
import com.ayst.stresstest.test.base.TestType;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class VideoTestFragment extends BaseTimingTestFragment {

    @BindView(R.id.tv_path)
    TextView mPathTv;
    @BindView(R.id.tv_error)
    TextView mErrorTv;
    @BindView(R.id.container_content)
    LinearLayout mContentContainer;
    @BindView(R.id.video_view)
    VideoView mVideoView;
    @BindView(R.id.container_video)
    RelativeLayout mVideoContainer;
    Unbinder unbinder;

    private String mPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
        mPath = sdcard + "/Download/bbb_full.ffmpeg.720x480.mp4.libx264_500kbps_25fps.libfaac_stereo_128kbps_44100Hz.mp4";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        setTitle(R.string.video_test);
        setType(TestType.TYPE_VIDEO_TEST);

        View contentView = inflater.inflate(R.layout.fragment_video_test, container, false);
        setFullContentView(contentView);

        unbinder = ButterKnife.bind(this, contentView);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPathTv.setText(mPath);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected void updateImpl() {
        super.updateImpl();

        if (isRunning()) {
            mContentContainer.setVisibility(View.GONE);
            mVideoContainer.setVisibility(View.VISIBLE);
        } else {
            mVideoContainer.setVisibility(View.INVISIBLE);
            mPathTv.setText(mPath);
            mContentContainer.setVisibility(View.VISIBLE);
            if (mResult == Result.POOR || mResult == Result.FAIL) {
                mErrorTv.setVisibility(View.VISIBLE);
            } else {
                mErrorTv.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onStartClicked() {
        if (!isFileExist()) {
            showToast(R.string.video_test_null_file);
            return;
        }
        super.onStartClicked();
    }

    @Override
    public void start() {
        playVideo();

        super.start();
    }

    @Override
    public void stop() {
        stopVideo();

        super.stop();
    }

    @Override
    public boolean isSupport() {
        return true;
    }

    private boolean playVideo() {
        if (mPath == null) {
            showToast(R.string.video_test_select_file_tips);
            return false;
        }

        MediaController mc = new MediaController(mActivity);
        mVideoView.setMediaController(mc);
        //videoView.setVideoURI(Uri.parse(""));
        mVideoView.setVideoPath(mPath);

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG, "onCompletion, restart");
                mVideoView.start();
            }
        });

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
            }
        });

        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mErrorTv.setText(getString(R.string.video_test_play_error) + "(" + what + "," + extra + ")");

                markFailure();
                mVideoView.start();

                return false;
            }
        });

        mVideoView.requestFocus();
        mVideoView.start();

        return true;
    }

    private void stopVideo() {
        mVideoView.stopPlayback();
    }

    private boolean isFileExist() {
        Log.d(TAG, "path: " + mPath);
        if (new File(mPath).exists()) {
            Log.d(TAG, "File exist ");
            return true;
        } else {
            Log.d(TAG, "File not exist ");
        }
        return false;
    }
}
