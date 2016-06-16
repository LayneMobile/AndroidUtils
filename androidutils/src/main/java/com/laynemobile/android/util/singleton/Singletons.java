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

public final class Singletons {
    private Singletons() { throw new AssertionError("no instances"); }

    public static <T, P> AbstractLoadingSingleton<T, P> loadingSingleton(Style style,
            LoadingSingleton.Loader<T, P> loader) {
        switch (style) {
            case Atomic:
                return AtomicLoadingSingleton.create(loader);
            case DoubleCheck:
                return DoubleCheckSingleton.create(loader);
            case Synchronized:
                return SynchronizedSingleton.create(loader);
            default:
                throw new IllegalArgumentException("unknown singleton style -> " + style);
        }
    }

    public static <T> Singleton<T> singleton(Style style, LazySingleton.InstanceCreator<T> instanceCreator) {
        return forwarding(loadingSingleton(style, loader(instanceCreator)));
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

    public enum Style {
        Atomic,
        DoubleCheck,
        Synchronized
    }
}
