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

package com.laynemobile.android.gson;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

public class GsonParcelable extends GsonObject implements Parcelable {
    public static final Parcelable.Creator<? extends GsonParcelable> CREATOR = new BaseCreator<GsonParcelable>() {
        @Override protected Gson gson() {
            return GsonObject.gson();
        }

        @Override public GsonParcelable[] newArray(int size) {
            return new GsonParcelable[size];
        }
    };

    protected void initFromParcel(Parcel source) {
        // subclass implementation
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public final void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getClass().getName());
        dest.writeString(toJson());
        addToParcel(dest, flags);
    }

    protected void addToParcel(Parcel dest, int flags) {
        // subclass implementation
    }

    public static abstract class BaseCreator<T extends GsonParcelable> implements Parcelable.Creator<T> {
        protected abstract Gson gson();

        @Override public T createFromParcel(Parcel source) {
            String className = source.readString();
            String json = source.readString();
            try {
                Class<? extends T> type = (Class<? extends T>) Class.forName(className);
                T t = gson().fromJson(json, type);
                t.initFromParcel(source);
                return t;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
