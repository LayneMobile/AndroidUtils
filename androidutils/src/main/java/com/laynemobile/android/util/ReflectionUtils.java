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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.reflect.Field;

public final class ReflectionUtils {
    private static final int TYPE_OBJECT = 0;
    private static final int TYPE_INT = 1;

    private ReflectionUtils() { throw new AssertionError("no instances"); }

    @NonNull
    public static Field getField(@NonNull Object object, @NonNull String fieldName) throws ReflectionException {
        return getField(object.getClass(), fieldName);
    }

    @NonNull
    public static Field getField(@NonNull Class<?> clazz, @NonNull String fieldName) throws ReflectionException {
        Field field = null;
        NoSuchFieldException exception = null;
        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
            try {
                field = c.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                exception = e;
            }
        }
        if (field == null) {
            String message = "cannot get '" + fieldName + "' field from class: " + clazz.getName();
            throw new ReflectionException(message, exception);
        }
        return field;
    }

    public static <T> T getFieldValue(@NonNull Class<?> clazz, @NonNull String fieldName) throws ReflectionException {
        Field field = getField(clazz, fieldName);
        Object value = getValue(null, field, TYPE_OBJECT);
        return cast(value);
    }

    public static <T> T getFieldValue(@NonNull Object object, @NonNull String fieldName) throws ReflectionException {
        Field field = getField(object, fieldName);
        Object value = getValue(object, field, TYPE_OBJECT);
        return cast(value);
    }

    public static int getIntFieldValue(@NonNull Class<?> clazz, @NonNull String fieldName) throws ReflectionException {
        Field field = getField(clazz, fieldName);
        Object value = getValue(null, field, TYPE_INT);
        return castInt(value);
    }

    public static int getIntFieldValue(@NonNull Object object, @NonNull String fieldName) throws ReflectionException {
        Field field = getField(object, fieldName);
        Object value = getValue(object, field, TYPE_INT);
        return castInt(value);
    }

    public static void setFieldValue(@NonNull Object object, @NonNull String fieldName,
            Object value) throws ReflectionException {
        Field field = getField(object, fieldName);
        Value v = Value.obtain();
        v.type = TYPE_OBJECT;
        v.object = value;
        setValue(object, field, v);
    }

    public static void setFieldValue(@NonNull Class<?> clazz, @NonNull String fieldName,
            Object value) throws ReflectionException {
        Field field = getField(clazz, fieldName);
        Value v = Value.obtain();
        v.type = TYPE_OBJECT;
        v.object = value;
        setValue(null, field, v);
    }

    public static void setIntFieldValue(@NonNull Object object, @NonNull String fieldName,
            int value) throws ReflectionException {
        Field field = getField(object, fieldName);
        Value v = Value.obtain();
        v.type = TYPE_INT;
        v.object = value;
        setValue(object, field, v);
    }

    public static void setIntFieldValue(@NonNull Class<?> clazz, @NonNull String fieldName,
            int value) throws ReflectionException {
        Field field = getField(clazz, fieldName);
        Value v = Value.obtain();
        v.type = TYPE_INT;
        v.object = value;
        setValue(null, field, v);
    }

    private static Object getValue(@Nullable Object object, @NonNull Field field, int type) throws ReflectionException {
        try {
            if (!field.isAccessible()) { field.setAccessible(true); }
            switch (type) {
                case TYPE_INT:
                    return field.getInt(object);
                case TYPE_OBJECT:
                default:
                    return field.get(object);
            }
        } catch (Exception e) {
            String message = "cannot get '" + field.getName() + "' value in class: " + field.getDeclaringClass();
            throw new ReflectionException(message, e);
        }
    }

    private static void setValue(@Nullable Object object, @NonNull Field field,
            @NonNull Value value) throws ReflectionException {
        try {
            if (!field.isAccessible()) { field.setAccessible(true); }
            switch (value.type) {
                case TYPE_INT:
                    field.setInt(object, (int) value.object);
                    break;
                case TYPE_OBJECT:
                default:
                    field.set(object, value.object);
                    break;
            }
        } catch (Exception e) {
            String message = "cannot set '" + field.getName() + "' value in class: " + field.getDeclaringClass();
            throw new ReflectionException(message, e);
        } finally {
            value.recycle();
        }
    }

    @SuppressWarnings("unchecked") private static <T> T cast(Object object) throws ReflectionException {
        try {
            return (T) object;
        } catch (ClassCastException e) {
            throw new ReflectionException("unable to cast field: " + object, e);
        }
    }

    private static int castInt(Object object) throws ReflectionException {
        try {
            return (int) object;
        } catch (ClassCastException e) {
            throw new ReflectionException("unable to cast int field: " + object, e);
        }
    }

    public static class ReflectionException extends Exception {
        public ReflectionException() {}

        public ReflectionException(String detailMessage) {
            super(detailMessage);
        }

        public ReflectionException(String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
        }

        public ReflectionException(Throwable throwable) {
            super(throwable);
        }
    }

    private static final class Value {
        private static final ThreadLocal<Value> sValues = new ThreadLocal<>();

        private int type;
        private Object object;

        private static Value obtain() {
            Value value = sValues.get();
            if (value == null) {
                value = new Value();
                sValues.set(value);
            }
            return value;
        }

        private void recycle() {
            type = 0;
            object = null;
        }
    }
}
