/*
 * Copyright 2019 Apple Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package com.shazam.fork.system.adb;

import com.android.ddmlib.AdbInitOptions;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @see "com.android.builder.testing.ConnectedDeviceProvider"
 */
public class Adb {
    private final AndroidDebugBridge bridge;

    public Adb(File sdk) {
        AndroidDebugBridge.init(AdbInitOptions.DEFAULT);
        File adbPath = FileUtils.getFile(sdk, "platform-tools", "adb");
        bridge = AndroidDebugBridge.createBridge(adbPath.getAbsolutePath(), false, 30, SECONDS);

        for (int attempt = 0; attempt < 60 && !this.isReady(bridge); attempt++) {
            sleep(500);
        }

        if (!isReady(bridge)) {
            throw new RuntimeException("Timeout getting device list.", null);
        }
    }

    private boolean isReady(AndroidDebugBridge bridge) {
        return bridge.getCurrentAdbVersion() != null && bridge.hasInitialDeviceList();
    }

    public Collection<IDevice> getDevices() {
        return Arrays.asList(bridge.getDevices());
    }

    public void terminate() {
        AndroidDebugBridge.terminate();
    }

    private void sleep(int sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException ignored) {
        }
    }
}
