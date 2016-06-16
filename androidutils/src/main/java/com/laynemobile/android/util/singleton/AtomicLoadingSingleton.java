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

import java.util.concurrent.atomic.AtomicReference;

abstract class AtomicLoadingSingleton<T, P> extends AbstractLoadingSingleton<T, P> {
    private final AtomicReference<T> reference;

    private AtomicLoadingSingleton() {
        this.reference = new AtomicReference<>(null);
    }

    static <T, P> AtomicLoadingSingleton<T, P> create(@NonNull final Loader<T, P> loader) {
        return new AtomicLoadingSingleton<T, P>() {
            @NonNull @Override protected T loadInstance(P p) {
                return loader.loadInstance(p);
            }
        };
    }

    @Nullable @Override public final T instance() {
        return this.reference.get();
    }

    @NonNull @Override public final T instance(P p) {
        final T instance;
        final AtomicReference<T> ref = this.reference;
        if ((instance = ref.get()) == null) {
            ref.compareAndSet(null, checkLoadInstance(p));
            return ref.get();
        }
        return instance;
    }
}
