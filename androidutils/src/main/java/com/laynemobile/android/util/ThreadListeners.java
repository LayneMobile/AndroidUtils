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
import android.os.Message;
import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/** This class handles listeners that need to receive callbacks on specific threads. */
public abstract class ThreadListeners<T extends ThreadListener> implements Handler.Callback {
    private static final int MSG_MAIN = 1;
    private static final int MSG_BG = 2;

    private final Listeners<T> mainListeners = new Listeners<>();
    private final Listeners<T> backgroundListeners = new Listeners<>();
    private final Listeners<T> immediateListeners = new Listeners<>();

    private final Handler mainHandler = Handlers.newMainHandler(this);
    private final Handler backgroundHandler = Handlers.newBackgroundHandler(this);

    public final void add(@NonNull T listener) {
        switch (listener.getListeningThread()) {
            case T.THREAD_IMMEDIATE:
                immediateListeners.add(listener);
                break;
            case T.THREAD_BACKGROUND:
                backgroundListeners.add(listener);
                break;
            case T.THREAD_MAIN:
            default:
                mainListeners.add(listener);
                break;
        }
    }

    public final boolean remove(@NonNull T listener) {
        switch (listener.getListeningThread()) {
            case T.THREAD_IMMEDIATE:
                return immediateListeners.remove(listener);
            case T.THREAD_BACKGROUND:
                return backgroundListeners.remove(listener);
            case T.THREAD_MAIN:
            default:
                return mainListeners.remove(listener);
        }
    }

    public final void publish(@NonNull Object args) {
        if (!mainListeners.isEmpty()) {
            Message msg = Message.obtain();
            msg.what = MSG_MAIN;
            msg.obj = args;
            mainHandler.sendMessage(msg);
        }
        if (!backgroundListeners.isEmpty()) {
            Message msg = Message.obtain();
            msg.what = MSG_BG;
            msg.obj = args;
            backgroundHandler.sendMessage(msg);
        }
        if (!immediateListeners.isEmpty()) {
            publish(args, immediateListeners.ext());
        }
    }

    protected abstract void publish(@NonNull Object args, @NonNull Collection<T> listeners);

    @Override public final boolean handleMessage(Message msg) {
        final Collection<T> listeners;
        switch (msg.what) {
            case MSG_MAIN:
                listeners = mainListeners.ext();
                break;
            case MSG_BG:
                listeners = backgroundListeners.ext();
                break;
            default:
                listeners = null;
                break;
        }
        Object obj;
        if (listeners != null && (obj = msg.obj) != null) {
            publish(obj, listeners);
            return true;
        }
        return false;
    }

    private static final class Listeners<T extends ThreadListener> {
        private volatile CopyOnWriteArraySet<T> internal;
        private volatile Set<T> external;

        private Listeners() {}

        private boolean isEmpty() {
            return internal == null || ensure().internal.isEmpty();
        }

        private void add(T t) {
            ensure().internal.add(t);
        }

        private boolean remove(T t) {
            return ensure().internal.remove(t);
        }

        private Set<T> ext() {
            return ensure().external;
        }

        private Listeners<T> ensure() {
            if (internal == null) {
                synchronized (this) {
                    if (internal == null) {
                        internal = new CopyOnWriteArraySet<>();
                        external = Collections.unmodifiableSet(internal);
                    }
                }
            }
            return this;
        }
    }
}
