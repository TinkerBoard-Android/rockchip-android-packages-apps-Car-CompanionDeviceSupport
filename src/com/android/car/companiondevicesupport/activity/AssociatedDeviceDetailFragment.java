/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.car.companiondevicesupport.activity;

import static com.android.car.connecteddevice.util.SafeLog.loge;

import android.annotation.NonNull;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.car.companiondevicesupport.R;
import com.android.car.companiondevicesupport.api.external.AssociatedDevice;

import java.util.List;

/** Fragment that shows the details of an associated device. */
public class AssociatedDeviceDetailFragment extends Fragment {
    private final static String TAG = "AssociatedDeviceDetailFragment";
    private AssociatedDevice mAssociatedDevice;
    private TextView mDeviceName;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.associated_device_detail_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mDeviceName = view.findViewById(R.id.device_name);

        AssociatedDeviceViewModel model = ViewModelProviders.of(getActivity())
                .get(AssociatedDeviceViewModel.class);
        setAssociatedDevices(model.getDevices().getValue());
        model.getDevices().observe(this, this::setAssociatedDevices);
        view.findViewById(R.id.remove_button)
                .setOnClickListener(v -> model.setDeviceToRemove(mAssociatedDevice));
    }

    private void setAssociatedDevices(List<AssociatedDevice> devices) {
        if (devices == null) {
            loge(TAG, "Device list is null.");
            return;
        }
        if (devices.size() == 0) {
            loge(TAG, "No device has been associated.");
            return;
        }
        if (devices.size() > 1) {
            loge(TAG, "More than one devices have been associated.");
            return;
        }
        // Currently, we only support single associated device.
        setAssociatedDevice(devices.get(0));
    }

    private void setAssociatedDevice(@NonNull AssociatedDevice device) {
        mAssociatedDevice = device;
        mDeviceName.setText(device.getDeviceName());
    }
}