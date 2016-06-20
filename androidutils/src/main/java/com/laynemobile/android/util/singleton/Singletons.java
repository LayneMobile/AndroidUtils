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

public enum Singletons {
    Atomic(new Delegate() {
        @Override public <T, P> LoadingSingleton<T, P> loading(LoadingSingleton.Loader<T, P> loader) {
            return AtomicLoadingSingleton.create(loader);
        }
    }),
    DoubleCheck(new Delegate() {
        @Override public <T, P> LoadingSingleton<T, P> loading(LoadingSingleton.Loader<T, P> loader) {
            return DoubleCheckSingleton.create(loader);
        }
    }),
    Synchronized(new Delegate() {
        @Override public <T, P> LoadingSingleton<T, P> loading(LoadingSingleton.Loader<T, P> loader) {
            return SynchronizedSingleton.create(loader);
        }
    });

    private final Delegate delegate;

    Singletons(Delegate delegate) {
        this.delegate = delegate;
    }

    public <T, P> LoadingSingleton<T, P> createLoading(LoadingSingleton.Loader<T, P> loader) {
        return delegate.loading(loader);
    }

    public <T> Singleton<T> create(LazySingleton.InstanceCreator<T> instanceCreator) {
        return forwarding(createLoading(loader(instanceCreator)));
    }

    private interface Delegate {
        <T, P> LoadingSingleton<T, P> loading(LoadingSingleton.Loader<T, P> loader);
    }

    private static <T> Singleton<T> forwarding(LoadingSingleton<T, LazySingleton.Params> loadingSingleton) {
        return new ForwardingLazySingleton<>((AbstractLoadingSingleton<T, LazySingleton.Params>) loadingSingleton);
    }

    private static <T> LoaderCreator<T> loader(LazySingleton.InstanceCreator<T> instanceCreator) {
        return new LoaderCreator<>(instanceCreator);
    }

    private static class LoaderCreator<T> implements LoadingSingleton.Loader<T, LazySingleton.Params> {
        private final LazySingleton.InstanceCreator<T> instanceCreator;

        private LoaderCreator(LazySingleton.InstanceCreator<T> instanceCreator) {
            this.instanceCreator = instanceCreator;
        }

        @NonNull @Override public T loadInstance(LazySingleton.Params params) {
            return instanceCreator.newInstance();
        }
    }
}
