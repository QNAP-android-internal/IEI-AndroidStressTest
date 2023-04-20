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

package com.ayst.stresstest.test.usb;

import static android.app.Activity.RESULT_OK;
import static com.ayst.stresstest.test.usb.USBUriManager.USB1_FILE_SELECT_CODE;
import static com.ayst.stresstest.test.usb.USBUriManager.USB2_FILE_SELECT_CODE;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.documentfile.provider.DocumentFile;

import com.ayst.stresstest.R;
import com.ayst.stresstest.test.base.BaseCountTestWithTimerFragment;

import butterknife.BindView;
import butterknife.Unbinder;

public class USBTestFragment extends BaseCountTestWithTimerFragment {

    @BindView(R.id.tv_path)
    TextView mPathTv;
    @BindView(R.id.tv_error)
    TextView mErrorTv;
    @BindView(R.id.container_content)
    LinearLayout mContentContainer;
    Unbinder unbinder;

    private String mPath;
    private Uri mUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
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
        } else {
            mPathTv.setText(mPath);
        }
    }

    @Override
    public boolean isSupport() {
        return true;
    }

    @Override
    protected boolean testOnce() {
        DocumentFile file = DocumentFile.fromSingleUri(mActivity.getApplicationContext(), mUri);
        if (file == null) {
            Log.d(TAG, "onActivityResult, File is null");
            return false;
        } else {
            if (!file.exists()) {
                Log.d(TAG, "onActivityResult, File not exist");
                return false;
            }
        }
        Log.d(TAG, "onActivityResult, File exist");
        return true;
    }

    void showFileChooser(int code) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, getString(R.string.video_test_select_file)), code);
        } catch (ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            showToast("Please install a File Manager.");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        do {
            if (resultCode == RESULT_OK) {
                // Get the Uri of the selected file
                mUri = data.getData();
                Log.d(TAG, "onActivityResult, File Uri: " + mUri.toString());
                // Get the path
                mPath = mUri.toString();
                Log.d(TAG, "onActivityResult, File Path: " + mPath);
                switch (requestCode) {
                    case USB1_FILE_SELECT_CODE:
                        USBUriManager.getInstance().putUSBUri(mUri, 0);
                        break;
                    case USB2_FILE_SELECT_CODE:
                        USBUriManager.getInstance().putUSBUri(mUri, 1);
                        break;
                }
                if (USBUriManager.getInstance().isChooseSameUSB()) {
                    Log.d(TAG, "onActivityResult, choose same usb storage");
                    showToast(R.string.video_test_invalid_file);
                    break;
                }
                if (!TextUtils.isEmpty(mPath)) {
                    super.onStartClicked();
                } else {
                    Log.d(TAG, "onActivityResult, file path is invalid");
                    showToast(R.string.video_test_invalid_file);
                }
            }
        } while (false);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
