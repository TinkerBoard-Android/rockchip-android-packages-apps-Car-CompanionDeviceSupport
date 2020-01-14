/*
 * Copyright (C) 2019 The Android Open Source Project
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

package com.android.car.companiondevicesupport.feature.trust;

import android.annotation.NonNull;
import android.annotation.Nullable;
import android.content.Context;
import android.os.ParcelUuid;
import android.os.RemoteException;

import com.android.car.companiondevicesupport.R;
import com.android.car.companiondevicesupport.api.external.CompanionDevice;
import com.android.car.companiondevicesupport.feature.RemoteFeature;

/** Feature wrapper for trusted device. */
class TrustedDeviceFeature extends RemoteFeature {

    private static final String TAG = "TrustedDeviceFeature";

    private Callback mCallback;

    TrustedDeviceFeature(@NonNull Context context) {
        super(context,
                ParcelUuid.fromString(context.getString(R.string.trusted_device_feature_id)));
    }

    /** Send a message securely to a device. */
    void sendMessageSecurely(@NonNull CompanionDevice device, @NonNull byte[] message)
            throws RemoteException {
        getConnectedDeviceManager().sendMessageSecurely(device, getFeatureId(), message);
    }

    /** Set a {@link Callback} for events from the device. Set {@code null} to clear. */
    void setCallback(@Nullable Callback callback) {
        mCallback = callback;
    }

    @Override
    protected void onMessageReceived(CompanionDevice device, byte[] message) {
        if (mCallback != null) {
            mCallback.onMessageReceived(device, message);
        }
    }

    @Override
    protected void onDeviceError(CompanionDevice device, int error) {
        if (mCallback != null) {
            mCallback.onDeviceError(device, error);
        }
    }

    interface Callback {
        /** Called when a new {@link byte[]} message is received for this feature. */
        void onMessageReceived(@NonNull CompanionDevice device, @NonNull byte[] message);

        /** Called when an error has occurred with the connection. */
        void onDeviceError(@NonNull CompanionDevice device, int error);
    }
}