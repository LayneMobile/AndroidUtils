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

package com.laynemobile.android.util.singleton;

import android.support.annotation.NonNull;

public abstract class DoubleLockSingleton<T> extends LazySingleton<T> {
    private final Object mLock = new Object();
    private volatile T mInstance;

    protected DoubleLockSingleton() {}

    public static <T> DoubleLockSingleton<T> create(@NonNull final InstanceCreator<T> instanceCreator) {
        return new DoubleLockSingleton<T>() {
            @NonNull @Override protected T newInstance() {
                return instanceCreator.newInstance();
            }
        };
    }

    @NonNull @Override public final T instance() {
        T instance;
        if ((instance = mInstance) == null) {
            synchronized (mLock) {
                if ((instance = mInstance) == null) {
                    return mInstance = newInstance();
                }
            }
        }
        return instance;
    }
}
