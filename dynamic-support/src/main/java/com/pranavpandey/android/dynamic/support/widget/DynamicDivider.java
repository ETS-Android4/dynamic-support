/*
 * Copyright 2018-2021 Pranav Pandey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pranavpandey.android.dynamic.support.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.utils.DynamicDrawableUtils;

/**
 * An {@link DynamicBackgroundView} to implement divider.
 */
public class DynamicDivider extends DynamicBackgroundView {

    public DynamicDivider(@NonNull Context context) {
        super(context);
    }

    public DynamicDivider(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicDivider(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void loadFromAttributes(@Nullable AttributeSet attrs) {
        super.loadFromAttributes(attrs);

        if (getColorType() == Theme.ColorType.NONE
                && getColor(false) == Theme.Color.UNKNOWN) {
            setColorType(Theme.ColorType.TINT_BACKGROUND);
        }
    }

    @Override
    public void setColor() {
        super.setColor();

        if (DynamicTheme.getInstance().isHideDividers()
                && getContrastWithColor() != Theme.Color.UNKNOWN) {
            DynamicDrawableUtils.colorizeDrawable(getBackground(), getContrastWithColor());
        }
    }
}
