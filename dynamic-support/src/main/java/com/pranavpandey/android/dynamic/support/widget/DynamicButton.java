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
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.utils.DynamicTintUtils;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicCornerWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicTintWidget;
import com.pranavpandey.android.dynamic.support.widget.base.DynamicWidget;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;

/**
 * An {@link MaterialButton} to apply {@link DynamicTheme} according to the supplied parameters.
 */
public class DynamicButton extends MaterialButton implements
        DynamicWidget, DynamicCornerWidget<Integer>, DynamicTintWidget {

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
     * Color applied to this view after considering the background aware properties.
     */
    private @ColorInt int mAppliedColor;

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

    /**
     * {@code true} if style applied to this view is borderless.
     */
    private boolean mStyleBorderless;

    /**
     * {@code true} to tint background according to the widget color.
     */
    private boolean mTintBackground;

    public DynamicButton(@NonNull Context context) {
        this(context, null);
    }

    public DynamicButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        loadFromAttributes(attrs);
    }

    public DynamicButton(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        loadFromAttributes(attrs);
    }

    /**
     * @return {@code true} if style applied to this view is borderless.
     */
    public boolean isStyleBorderless() {
        return mStyleBorderless;
    }

    /**
     * Set the value of {@link #mStyleBorderless}.
     *
     * @param styleBorderless {@code true} if style applied to this view
     *                        is borderless.
     */
    public void setStyleBorderless(boolean styleBorderless) {
        this.mStyleBorderless = styleBorderless;

        setColor();
    }

    @Override
    public void loadFromAttributes(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.DynamicButton);

        try {
            mColorType = a.getInt(
                    R.styleable.DynamicButton_ads_colorType,
                    Theme.ColorType.TINT_BACKGROUND);
            mContrastWithColorType = a.getInt(
                    R.styleable.DynamicButton_ads_contrastWithColorType,
                    Theme.ColorType.BACKGROUND);
            mColor = a.getColor(
                    R.styleable.DynamicButton_ads_color,
                    Theme.Color.UNKNOWN);
            mContrastWithColor = a.getColor(
                    R.styleable.DynamicButton_ads_contrastWithColor,
                    Defaults.getContrastWithColor(getContext()));
            mBackgroundAware = a.getInteger(
                    R.styleable.DynamicButton_ads_backgroundAware,
                    Defaults.getBackgroundAware());
            mStyleBorderless = a.getBoolean(
                    R.styleable.DynamicButton_ads_styleBorderless,
                    Defaults.ADS_STYLE_BORDERLESS);
            mTintBackground = a.getBoolean(
                    R.styleable.DynamicButton_ads_tintBackground,
                    Defaults.ADS_TINT_BACKGROUND);
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

        setCorner(DynamicTheme.getInstance().get().getCornerRadius());
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
    public @ColorInt int getColor(boolean resolve) {
        return resolve ? mAppliedColor : mColor;
    }

    @Override
    public @ColorInt int getColor() {
        return getColor(true);
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
    public boolean isTintBackground() {
        return mTintBackground;
    }

    @Override
    public void setTintBackground(boolean tintBackground) {
        this.mTintBackground = tintBackground;

        setColor();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        setAlpha(enabled ? Defaults.ADS_ALPHA_ENABLED : Defaults.ADS_ALPHA_DISABLED);
    }

    @Override
    public void setCorner(Integer cornerRadius) {
        setCornerRadius(cornerRadius);
    }

    @Override
    public Integer getCorner() {
        return getCornerRadius();
    }

    @Override
    public void setColor() {
        if (mColor != Theme.Color.UNKNOWN) {
            mAppliedColor = mColor;
            if (isBackgroundAware() && mContrastWithColor != Theme.Color.UNKNOWN) {
                mAppliedColor = DynamicColorUtils.getContrastColor(mColor, mContrastWithColor);
            }

            DynamicTintUtils.setViewBackgroundTint(this, mContrastWithColor,
                    mTintBackground ? mAppliedColor : DynamicColorUtils.getTintColor(
                            mContrastWithColor), mStyleBorderless, false);

            if (!mStyleBorderless) {
                if (mTintBackground) {
                    setTextColor(DynamicResourceUtils.getColorStateList(
                            mContrastWithColor,
                            DynamicColorUtils.getTintColor(mAppliedColor),
                            DynamicColorUtils.getTintColor(mAppliedColor), false));
                } else {
                    setTextColor(DynamicResourceUtils.getColorStateList(
                            mContrastWithColor,
                            DynamicColorUtils.getContrastColor(mAppliedColor,
                                    DynamicColorUtils.getTintColor(mContrastWithColor)),
                            DynamicColorUtils.getContrastColor(mAppliedColor,
                                    DynamicColorUtils.getTintColor(mContrastWithColor)),
                            false));
                }
            } else {
                setTextColor(DynamicResourceUtils.getColorStateList(
                        DynamicColorUtils.getTintColor(mContrastWithColor),
                        mAppliedColor, mAppliedColor, false));
            }
        }
    }
}