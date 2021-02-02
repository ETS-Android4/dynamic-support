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

package com.pranavpandey.android.dynamic.support.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.support.Defaults;
import com.pranavpandey.android.dynamic.theme.Theme;

/**
 * A {@link DynamicView} with an icon, title and subtitle functionality that can be used to show
 * various information according to the requirement.
 *
 * <p>Use {@link #getItemView()} method to set click listeners or to perform other operations.
 */
public class DynamicItemView extends DynamicView {

    /**
     * Icon used by this view.
     */
    private Drawable mIcon;

    /**
     * Title used by this view.
     */
    private CharSequence mTitle;

    /**
     * Subtitle used by this view.
     */
    private CharSequence mSubtitle;

    /**
     * Icon tint color type used by this view.
     */
    private @Theme.ColorType int mColorType;

    /**
     * Icon tint color used by this view.
     */
    private @ColorInt int mColor;

    /**
     * {@code true} to show horizontal divider.
     * <p>Useful to display in a list view.
     */
    private boolean mShowDivider;

    /**
     * {@code true} to fill the empty icon space if applicable.
     */
    private boolean mFillSpace;

    /**
     * Default visibility of the icon view.
     */
    private int mVisibilityIconView;

    /**
     * Root element of this view.
     */
    private ViewGroup mItemView;

    /**
     * Image view to show the icon.
     */
    private ImageView mIconView;

    /**
     * Image view to show the icon.
     */
    private ImageView mIconFooterView;

    /**
     * Text view to show the title.
     */
    private TextView mTitleView;

    /**
     * Text view to show the subtitle.
     */
    private TextView mSubtitleView;

    /**
     * View to show the divider.
     */
    private View mDivider;

    public DynamicItemView(@NonNull Context context) {
        super(context);
    }

    public DynamicItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicItemView(@NonNull Context context,
            @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param context The context for this view.
     * @param icon The icon for this view.
     * @param title The title for this view.
     * @param subtitle The subtitle for this view.
     * @param color The icon tint color for this view.
     * @param showDivider {@code true} to show horizontal divider.
     */
    public DynamicItemView(@NonNull Context context, @Nullable Drawable icon,
            @Nullable CharSequence title, @Nullable CharSequence subtitle,
            @ColorInt int color, boolean showDivider) {
        super(context);

        this.mIcon = icon;
        this.mTitle = title;
        this.mSubtitle = subtitle;
        this.mColor = color;
        this.mShowDivider = showDivider;

        onUpdate();
    }

    @Override
    protected void onLoadAttributes(@Nullable AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, 
                R.styleable.DynamicItemView);

        try {
            mIcon = DynamicResourceUtils.getDrawable(getContext(),
                    a.getResourceId(
                            R.styleable.DynamicItemView_ads_icon,
                            Theme.Color.UNKNOWN));
            mTitle = a.getString(
                    R.styleable.DynamicItemView_ads_title);
            mSubtitle = a.getString(
                    R.styleable.DynamicItemView_ads_subtitle);
            mShowDivider = a.getBoolean(
                    R.styleable.DynamicItemView_ads_showDivider,
                    Defaults.ADS_SHOW_DIVIDER);
            mFillSpace = a.getBoolean(
                    R.styleable.DynamicItemView_ads_fillSpace,
                    Defaults.ADS_FILL_SPACE);
            mColorType = a.getInt(
                    R.styleable.DynamicItemView_ads_colorType,
                    Defaults.ADS_COLOR_TYPE_ICON);
            mColor = a.getColor(
                    R.styleable.DynamicItemView_ads_color,
                    Theme.Color.UNKNOWN);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected @LayoutRes int getLayoutRes() {
        return R.layout.ads_item_view;
    }

    @Override
    protected void onInflate() {
        inflate(getContext(), getLayoutRes(), this);

        mItemView = findViewById(R.id.ads_item_view);
        mIconView = findViewById(R.id.ads_item_view_icon);
        mIconFooterView = findViewById(R.id.ads_item_view_icon_footer);
        mTitleView = findViewById(R.id.ads_item_view_title);
        mSubtitleView = findViewById(R.id.ads_item_view_subtitle);
        mDivider = findViewById(R.id.ads_item_view_divider);

        mVisibilityIconView = mIconView != null ? mIconView.getVisibility() : VISIBLE;

        onUpdate();
    }

    @Override
    public void onUpdate() {
        Dynamic.set(getIconView(), getIcon());
        Dynamic.set(getTitleView(), getTitle());
        Dynamic.set(getSubtitleView(), getSubtitle());

        if (getIconView() != null) {
            Dynamic.setVisibility(getIconView(), isFillSpace() ? GONE : getVisibilityIconView());

            if (getColor() != Theme.Color.UNKNOWN) {
                Dynamic.setColor(getIconView(), getColor());
            } else if (getColorType() != Theme.ColorType.CUSTOM) {
                Dynamic.setColorType(getIconView(), getColorType());
            } else {
                getIconView().clearColorFilter();
            }
        }

        if (getDivider() != null) {
            Dynamic.setVisibility(getDivider(), isShowDivider() ? VISIBLE : GONE);
        }
        Dynamic.setVisibility(getIconFooterView(), getIconView());
    }

    @Override
    protected void onEnabled(boolean enabled) {
        Dynamic.setEnabled(getItemView(), enabled);
        Dynamic.setEnabled(getIconView(), enabled);
        Dynamic.setEnabled(getTitleView(), enabled);
        Dynamic.setEnabled(getSubtitleView(), enabled);
    }

    /**
     * Get the icon used by this view.
     *
     * @return The icon used by this view.
     */
    public @Nullable Drawable getIcon() {
        return mIcon;
    }

    /**
     * Set the icon used by this view.
     *
     * @param icon The icon to be set.
     */
    public void setIcon(@Nullable Drawable icon) {
        this.mIcon = icon;

        onUpdate();
    }

    /**
     * Get the title used by this view.
     *
     * @return The title used by this view.
     */
    public @Nullable CharSequence getTitle() {
        return mTitle;
    }

    /**
     * Set the title used by this view.
     *
     * @param title The title to be set.
     */
    public void setTitle(@Nullable CharSequence title) {
        this.mTitle = title;

        onUpdate();
    }

    /**
     * Get the subtitle used by this view.
     *
     * @return The subtitle used by this view.
     */
    public @Nullable CharSequence getSubtitle() {
        return mSubtitle;
    }

    /**
     * Set the subtitle used by this view.
     *
     * @param subtitle The subtitle to be set.
     */
    public void setSubtitle(@Nullable CharSequence subtitle) {
        this.mSubtitle = subtitle;

        onUpdate();
    }

    /**
     * Get the icon tint color type used by this view.
     *
     * @return The icon tint color type used by this view.
     */
    public @Theme.ColorType int getColorType() {
        return mColorType;
    }

    /**
     * Set the icon tint color type used by this view.
     *
     * @param colorType The icon tint color type to be set.
     *
     * @see Theme.ColorType
     */
    public void setColorType(@Theme.ColorType int colorType) {
        this.mColorType = colorType;

        onUpdate();
    }

    /**
     * Get the icon tint color used by this view.
     *
     * @return The icon tint color used by this view.
     */
    public @ColorInt int getColor() {
        return mColor;
    }

    /**
     * Set the icon tint color used by this view.
     *
     * @param color The icon tint color to be set.
     */
    public void setColor(@ColorInt int color) {
        this.mColorType = Theme.ColorType.CUSTOM;
        this.mColor = color;

        onUpdate();
    }

    /**
     * Returns whether to show the horizontal divider.
     * <p>Useful to display in a list view.
     *
     * @return {@code true} to show the horizontal divider.
     */
    public boolean isShowDivider() {
        return mShowDivider;
    }

    /**
     * Set the horizontal divider fro this view.
     * <p>Useful to display in a list view.
     *
     * @param showDivider {@code true} to show the horizontal divider.
     */
    public void setShowDivider(boolean showDivider) {
        this.mShowDivider = showDivider;

        onUpdate();
    }

    /**
     * Returns whether to fill the empty icon space if applicable.
     *
     * @return {@code true} to fill the empty icon space if applicable.
     */
    public boolean isFillSpace() {
        return mFillSpace;
    }

    /**
     * Controls the fill empty space behavior for this view.
     *
     * @param fillSpace {@code true} to fill the empty icon space.
     */
    public void setFillSpace(boolean fillSpace) {
        this.mFillSpace = fillSpace;

        onUpdate();
    }

    /**
     * Returns the visibility of the icon view.
     *
     * @return The visibility of the icon view.
     */
    public int getVisibilityIconView() {
        return mVisibilityIconView;
    }

    /**
     * Get the root element of this view.
     *
     * @return The root element of this view
     */
    public ViewGroup getItemView() {
        return mItemView;
    }

    /**
     * Get the image view to show the icon used by this view.
     *
     * @return The image view to show the icon used by this view.
     */
    public ImageView getIconView() {
        return mIconView;
    }

    /**
     * Get the image view to show the footer icon used by this view.
     *
     * @return The image view to show the footer icon used by this view.
     */
    public @Nullable ImageView getIconFooterView() {
        return mIconFooterView;
    }

    /**
     * Get the text view to show the title used by this view.
     *
     * @return The text view to show the title used by this view.
     */
    public TextView getTitleView() {
        return mTitleView;
    }

    /**
     * Get the text view to show the subtitle used by this view.
     *
     * @return The text view to show the subtitle used by this view.
     */
    public TextView getSubtitleView() {
        return mSubtitleView;
    }

    /**
     * Get the view to show the divider used by this view.
     *
     * @return The view to show the divider used by this view.
     */
    public View getDivider() {
        return mDivider;
    }
}
