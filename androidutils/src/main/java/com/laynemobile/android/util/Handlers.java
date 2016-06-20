/*
 * Copyright 2016 Layne Mobile, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.laynemobile.android.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.laynemobile.android.util.singleton.LazySingleton.InstanceCreator;
import com.laynemobile.android.util.singleton.Singleton;
import com.laynemobile.android.util.singleton.Singletons;

public final class Handlers {
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    private Handlers() { throw new AssertionError("no instances"); }

    public static Handler mainHandler() {
        return MAIN_HANDLER;
    }

    public static Handler newMainHandler() {
        return newMainHandler(null);
    }

    public static Handler newMainHandler(@Nullable Handler.Callback callback) {
        return new Handler(Looper.getMainLooper(), callback);
    }

    /** Lazy-loaded background looper. */
    private static Looper backgroundLooper() {
        return Background.LOOPER;
    }

    /** Lazy-loaded background handler. */
    public static Handler backgroundHandler() {
        return Background.HANDLER;
    }

    public static Handler newBackgroundHandler() {
        return newBackgroundHandler(null);
    }

    public static Handler newBackgroundHandler(@Nullable Handler.Callback callback) {
        return new Handler(backgroundLooper(), callback);
    }

    private static final class Background {
        private static final Looper LOOPER;
        private static final Handler HANDLER;

        static {
            final HandlerThread thread = new HandlerThread("BackgroundHandler");
            thread.start();
            LOOPER = thread.getLooper();
            HANDLER = new Handler(LOOPER);
        }
    }

    private static final class Background2 {
        private static final Singleton<Background2> singleton
                = Singletons.DoubleCheck.create(new InstanceCreator<Background2>() {
            @NonNull @Override public Background2 newInstance() {
                final HandlerThread thread = new HandlerThread("BackgroundHandler");
                thread.start();
                return new Background2(thread.getLooper());
            }
        });

        private final Looper looper;
        private final Handler handler;

        private Background2(Looper looper) {
            this.looper = looper;
            this.handler = new Handler(looper);
        }

        private static Background2 instance() {
            return singleton.instance();
        }
    }
}
