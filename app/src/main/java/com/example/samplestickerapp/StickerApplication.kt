/*
 * Copyright (c) WhatsApp Inc. and its affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */
package com.example.samplestickerapp

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.android.gms.ads.MobileAds

class StickerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this) {}
        Fresco.initialize(this)
    }
}