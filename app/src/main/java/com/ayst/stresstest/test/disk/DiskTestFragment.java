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

package com.ayst.stresstest.test.disk;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.ayst.stresstest.R;
import com.ayst.stresstest.test.base.BaseCountTestWithTimerFragment;
import com.ayst.stresstest.test.base.TestType;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DiskTestFragment extends BaseCountTestWithTimerFragment {

    Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        setTitle(R.string.disk_test);
        setType(TestType.TYPE_DISK_TEST);

        View contentView = inflater.inflate(R.layout.fragment_disk_test, container, false);
        setFullContentView(contentView);

        unbinder = ButterKnife.bind(this, contentView);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected boolean testOnce() {
        String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
        // Check path: /mnt/user/0/emulated/0/Download
        if (new File(sdcard + "/Download/").exists()) {
            Log.d(TAG, "Download directory exist ");
            return true;
        } else {
            Log.d(TAG, "Download directory not exist ");
        }
        return false;
    }

    @Override
    public boolean isSupport() {
        return true;
    }
}
