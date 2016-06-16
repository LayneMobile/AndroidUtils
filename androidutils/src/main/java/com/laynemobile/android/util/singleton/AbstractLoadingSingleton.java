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

public abstract class AbstractLoadingSingleton<T, P> implements LoadingSingleton<T, P> {
    @NonNull protected abstract T loadInstance(P p);

    @NonNull protected final T checkLoadInstance(P p) {
        final T instance = loadInstance(p);
        if (instance == null) {
            throw new NullPointerException("loadInstance(" + p + ") cannot return null");
        }
        return instance;
    }
}
