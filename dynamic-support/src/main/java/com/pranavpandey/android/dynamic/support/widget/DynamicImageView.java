/*
 * Copyright 2018-2020 Pranav Pandey
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
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicWidget;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;

/**
 * An {@link AppCompatImageView} to apply {@link DynamicTheme} according to the supplied
 * parameters.
 */
public class DynamicImageView extends AppCompatImageView implements DynamicWidget {

    /**
     * Color type applied to this view.
     *
     * @see Theme.ColorType
     */
    private @Theme.ColorType int mColorType;

    /**
     * Background color type for this view so that it will remain in contrast with this
     * color type.
     */
    private @Theme.ColorType int mContrastWithColorType;

    /**
     * Color applied to this view.
     */
    private @ColorInt int mColor;

    /**
     * Background color for this view so that it will remain in contrast with this color.
     */
    private @ColorInt int mContrastWithColor;

    /**
     * The background aware functionality to change this view color according to the background.
     * It was introduced to provide better legibility for colored views and to avoid dark view
     * on dark background like situations.
     *
     * <p>If this is enabled then, it will check for the contrast color and do color
     * calculations according to that color so that this text view will always be visible on
     * that background. If no contrast color is found then, it will take the default
     * background color.
     *
     * @see Theme.BackgroundAware
     * @see #mContrastWithColor
     */
    private @Theme.BackgroundAware int mBackgroundAware;

    public DynamicImageView(@NonNull Context context) {
        this(context, null);
    }

    public DynamicImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        loadFromAttributes(attrs);
    }

    public DynamicImageView(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        loadFromAttributes(attrs);
    }

    /**
     * Returns the filter mode to be used to tint this view.
     *
     * @return The filter mode to be used to tint this view.
     */
    public PorterDuff.Mode getFilterMode() {
        return PorterDuff.Mode.SRC_IN;
    }

    @Override
    public void loadFromAttributes(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, 
                R.styleable.DynamicImageView);

        try {
            mColorType = a.getInt(
                    R.styleable.DynamicImageView_ads_colorType,
                    Theme.ColorType.NONE);
            mContrastWithColorType = a.getInt(
                    R.styleable.DynamicImageView_ads_contrastWithColorType,
                    Theme.ColorType.BACKGROUND);
            mColor = a.getColor(
                    R.styleable.DynamicImageView_ads_color,
                    WidgetDefaults.ADS_COLOR_UNKNOWN);
            mContrastWithColor = a.getColor(
                    R.styleable.DynamicImageView_ads_contrastWithColor,
                    WidgetDefaults.getContrastWithColor(getContext()));
            mBackgroundAware = a.getInteger(
                    R.styleable.DynamicImageView_ads_backgroundAware,
                    WidgetDefaults.getBackgroundAware());
        } finally {
            a.recycle();
        }

        initialize();
    }

    @Override
    public void initialize() {
        if (mColorType != Theme.ColorType.NONE
                && mColorType != Theme.ColorType.CUSTOM) {
            mColor = DynamicTheme.getInstance().resolveColorType(mColorType);
        }

        if (mContrastWithColorType != Theme.ColorType.NONE
                && mContrastWithColorType != Theme.ColorType.CUSTOM) {
            mContrastWithColor = DynamicTheme.getInstance()
                    .resolveColorType(mContrastWithColorType);
        }

        setColor();
    }

    @Override
    public @Theme.ColorType int getColorType() {
        return mColorType;
    }

    @Override
    public void setColorType(@Theme.ColorType int colorType) {
        this.mColorType = colorType;

        initialize();
    }

    @Override
    public @Theme.ColorType int getContrastWithColorType() {
        return mContrastWithColorType;
    }

    @Override
    public void setContrastWithColorType(@Theme.ColorType int contrastWithColorType) {
        this.mContrastWithColorType = contrastWithColorType;

        initialize();
    }

    @Override
    public @ColorInt int getColor() {
        return mColor;
    }

    @Override
    public void setColor(@ColorInt int color) {
        this.mColorType = Theme.ColorType.CUSTOM;
        this.mColor = color;

        setColor();
    }

    @Override
    public @ColorInt int getContrastWithColor() {
        return mContrastWithColor;
    }

    @Override
    public void setContrastWithColor(@ColorInt int contrastWithColor) {
        this.mContrastWithColorType = Theme.ColorType.CUSTOM;
        this.mContrastWithColor = contrastWithColor;

        setColor();
    }

    @Override
    public void setBackgroundAware(@Theme.BackgroundAware int backgroundAware) {
        this.mBackgroundAware = backgroundAware;

        setColor();
    }

    @Override
    public @Theme.BackgroundAware int getBackgroundAware() {
        return mBackgroundAware;
    }

    @Override
    public boolean isBackgroundAware() {
        return DynamicTheme.getInstance().resolveBackgroundAware(
                mBackgroundAware) != Theme.BackgroundAware.DISABLE;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        setAlpha(enabled ? WidgetDefaults.ADS_ALPHA_ENABLED : WidgetDefaults.ADS_ALPHA_DISABLED);
    }

    @Override
    public void setColor() {
        if (mColor != WidgetDefaults.ADS_COLOR_UNKNOWN) {
            if (isBackgroundAware() && mContrastWithColor != WidgetDefaults.ADS_COLOR_UNKNOWN) {
                mColor = DynamicColorUtils.getContrastColor(mColor, mContrastWithColor);
            }

            setColorFilter(mColor, getFilterMode());
        }

        if (mColorType == Theme.ColorType.NONE) {
            clearColorFilter();
        }
    }
}
