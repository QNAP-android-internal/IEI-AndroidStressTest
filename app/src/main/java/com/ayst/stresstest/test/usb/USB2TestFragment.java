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

import static com.ayst.stresstest.test.usb.USBUriManager.USB2_FILE_SELECT_CODE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ayst.stresstest.R;
import com.ayst.stresstest.test.base.TestType;

import butterknife.BindView;
import butterknife.ButterKnife;

public class USB2TestFragment extends USBTestFragment {

    @BindView(R.id.tv_path)
    TextView mPathTv;
    @BindView(R.id.tv_error)
    TextView mErrorTv;
    @BindView(R.id.container_content)
    LinearLayout mContentContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        setTitle(R.string.usb2_test);
        setType(TestType.TYPE_USB_2_TEST);

        View contentView = inflater.inflate(R.layout.fragment_usb2_test, container, false);
        setFullContentView(contentView);

        unbinder = ButterKnife.bind(this, contentView);
        return view;
    }

    @Override
    public void onStartClicked() {
        if (!isRunning()) {
            showFileChooser(USB2_FILE_SELECT_CODE);
        } else {
            super.onStartClicked();
        }
    }
}
