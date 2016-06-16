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

public abstract class LazySingleton<T> extends AbstractLoadingSingleton<T, LazySingleton.Params> {
    @NonNull protected abstract T newInstance();

    @NonNull @Override public abstract T instance();

    @NonNull protected final T checkNewInstance() {
        final T instance = newInstance();
        if (instance == null) {
            throw new NullPointerException("newInstance() cannot return null");
        }
        return instance;
    }

    @NonNull @Override protected final T loadInstance(Params params) {
        throw new UnsupportedOperationException("unsupported, use no argument loadInstance() method");
    }

    @NonNull @Override public final T instance(Params params) {
        throw new UnsupportedOperationException("unsupported, use no argument instance() method");
    }

    public interface InstanceCreator<T> {
        @NonNull T newInstance();
    }

    static final class Params {
        static final Params INSTANCE = new Params();

        private Params() {}
    }
}
