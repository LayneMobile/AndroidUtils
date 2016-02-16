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

package com.laynemobile.android.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.laynemobile.android.R;
import com.laynemobile.android.util.Util;

public class AndroidDrawer extends FrameLayout {
    private int widthMargin;
    private int maxWidth;

    public AndroidDrawer(Context context) {
        super(context);
        init(context, null, 0);
    }

    public AndroidDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public AndroidDrawer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(21) public AndroidDrawer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NavigationView, defStyleAttr,
                R.style.Widget_Design_NavigationView);
        Util.setViewBackground(this, a.getDrawable(R.styleable.NavigationView_android_background));
        if (a.hasValue(android.support.design.R.styleable.NavigationView_elevation)) {
            ViewCompat.setElevation(this, (float) a.getDimensionPixelSize(R.styleable.NavigationView_elevation, 0));
        }
        ViewCompat.setFitsSystemWindows(this,
                a.getBoolean(R.styleable.NavigationView_android_fitsSystemWindows, false));
        this.maxWidth = a.getDimensionPixelSize(R.styleable.NavigationView_android_maxWidth, 0);
        this.widthMargin = Util.getActionBarSize(context);
        a.recycle();
    }

    @Override protected void onMeasure(int widthSpec, int heightSpec) {
        int width = MeasureSpec.getSize(widthSpec);
        int maxWidth = Math.min(width - this.widthMargin, this.maxWidth);
        switch (MeasureSpec.getMode(widthSpec)) {
            case MeasureSpec.AT_MOST:
                widthSpec = MeasureSpec.makeMeasureSpec(Math.min(width, maxWidth), MeasureSpec.EXACTLY);
                break;
            case MeasureSpec.UNSPECIFIED:
                widthSpec = MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.EXACTLY);
            case MeasureSpec.EXACTLY:
        }
        super.onMeasure(widthSpec, heightSpec);
    }
}
