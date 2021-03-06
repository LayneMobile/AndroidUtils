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
import android.support.annotation.Nullable;

abstract class DoubleCheckSingleton<T, P> extends AbstractLoadingSingleton<T, P> {
    private final Object lock = new Object();
    private volatile T instance;

    private DoubleCheckSingleton() {}

    static <T, P> DoubleCheckSingleton<T, P> create(@NonNull final Loader<T, P> loader) {
        return new DoubleCheckSingleton<T, P>() {
            @NonNull @Override protected T loadInstance(P p) {
                return loader.loadInstance(p);
            }
        };
    }

    @Nullable @Override public final T instance() {
        return instance;
    }

    @NonNull @Override public final T instance(P p) {
        T i;
        if ((i = instance) == null) {
            synchronized (lock) {
                if ((i = instance) == null) {
                    return instance = checkLoadInstance(p);
                }
            }
        }
        return i;
    }
}
