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

package com.pranavpandey.android.dynamic.support.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.listener.DynamicColorListener;
import com.pranavpandey.android.dynamic.support.picker.color.DynamicColorShape;
import com.pranavpandey.android.dynamic.support.picker.color.DynamicColorView;
import com.pranavpandey.android.dynamic.theme.Theme;

/**
 * A simple base adapter to hold an array of colors and displays them in an adapter view.
 */
public class DynamicColorsAdapter extends BaseAdapter {

    /**
     * Listener to get the callback when a color is selected.
     */
    private DynamicColorListener mDynamicColorListener;

    /**
     * Array of colors to be handled by this adapter.
     */
    private @ColorInt Integer[] mData;

    /**
     * The selected color.
     */
    private @ColorInt int mSelectedColor;

    /**
     * Shape of the color swatches.
     *
     * @see DynamicColorShape
     */
    private @DynamicColorShape int mColorShape;

    /**
     * {@code true} to enable alpha for the colors.
     */
    private boolean mAlpha;

    /**
     * Contrast with color for the swatches.
     */
    private @ColorInt int mContrastWithColor;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param colors The array of colors to be handled by this adapter.
     * @param colorShape The shape of the color swatches.
     * @param alpha {@code true} to enable alpha for the color.
     * @param dynamicColorListener The listener to get the callback when a color is selected.
     */
    public DynamicColorsAdapter(@NonNull @ColorInt Integer[] colors,
            @DynamicColorShape int colorShape, boolean alpha,
            @NonNull DynamicColorListener dynamicColorListener) {
        this(colors, Theme.Color.UNKNOWN, colorShape, alpha,
                Theme.Color.UNKNOWN, dynamicColorListener);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param colors The array of colors to be handled by this adapter.
     * @param colorShape The shape of the color swatches.
     * @param alpha {@code true} to enable alpha for the color.
     * @param contrastWithColor The contrast with color for the swatches.
     * @param dynamicColorListener The listener to get the callback when a color is selected.
     */
    public DynamicColorsAdapter(@NonNull @ColorInt Integer[] colors,
            @DynamicColorShape int colorShape, boolean alpha, @ColorInt int contrastWithColor,
            @NonNull DynamicColorListener dynamicColorListener) {
        this(colors, Theme.Color.UNKNOWN, colorShape, alpha,
                contrastWithColor, dynamicColorListener);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param colors The array of colors to be handled by this adapter.
     * @param selectedColor The selected color.
     * @param colorShape The shape of the color swatches.
     * @param alpha {@code true} to enable alpha for the color.
     * @param contrastWithColor The contrast with color for the swatches.
     * @param dynamicColorListener The listener to get the callback when a color is selected.
     */
    public DynamicColorsAdapter(@NonNull @ColorInt Integer[] colors,
            @ColorInt int selectedColor, @DynamicColorShape int colorShape, boolean alpha,
            @ColorInt int contrastWithColor, @NonNull DynamicColorListener dynamicColorListener) {
        this.mData = colors;
        this.mSelectedColor = selectedColor;
        this.mColorShape = colorShape;
        this.mAlpha = alpha;
        this.mContrastWithColor = contrastWithColor;
        this.mDynamicColorListener = dynamicColorListener;
    }

    @Override
	public View getView(final int position,
            @Nullable View convertView, @NonNull ViewGroup parent) {
		ViewHolder viewHolder;

        if (convertView == null) {
        	convertView = LayoutInflater.from(parent.getContext()).inflate(
        	        R.layout.ads_layout_color_view, parent, false);
        	viewHolder = new ViewHolder(convertView);
        	convertView.setTag(viewHolder);
        } else {
        	viewHolder = (ViewHolder) convertView.getTag();
        }

        int color = (int) getItem(position);
        final DynamicColorView dynamicColorView = viewHolder.getDynamicColorView();
        dynamicColorView.setColor(color);
        dynamicColorView.setColorShape(mColorShape);
        dynamicColorView.setAlpha(mAlpha);
        if (mSelectedColor != Theme.Color.UNKNOWN) {
            dynamicColorView.setSelected(mSelectedColor == color);
        }

        dynamicColorView.setTooltip();
        Dynamic.setOnClickListener(dynamicColorView, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDynamicColorListener != null) {
                    mDynamicColorListener.onColorSelected(
                            null, position, dynamicColorView.getColor());
                    mSelectedColor = dynamicColorView.getColor();

                    notifyDataSetChanged();
                }
            }
        });

        if (mContrastWithColor != Theme.Color.UNKNOWN) {
            Dynamic.setContrastWithColor(dynamicColorView, mContrastWithColor);
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return mData.length;
    }

    @Override
    public Object getItem(int position) {
        return mData[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get the data handled by this adapter.
     *
     * @return The array of colors to be handled by this adapter.
     */
    public @NonNull @ColorInt Integer[] getData() {
        return mData;
    }

    /**
     * Sets array of colors to be handled by this adapter.
     *
     * @param data The array of colors to be set.
     */
    public void setData(@NonNull @ColorInt Integer[] data) {
        this.mData = data;

        notifyDataSetChanged();
    }

    /**
     * Get the dynamic color listener.
     *
     * @return The listener to get the callback when a color is selected.
     */
    public @Nullable DynamicColorListener getDynamicColorListener() {
        return mDynamicColorListener;
    }

    /**
     * Sets the listener to get the callback when a color is selected.
     *
     * @param dynamicColorListener The listener to be set.
     */
    public void setDynamicColorListener(
            @NonNull DynamicColorListener dynamicColorListener) {
        this.mDynamicColorListener = dynamicColorListener;

        notifyDataSetChanged();
    }

    /**
     * Get the selected color.
     *
     * @return The selected color.
     */
    public int getSelectedColor() {
        return mSelectedColor;
    }

    /**
     * Sets the selected color.
     *
     * @param selectedColor The color to be selected.
     */
    public void setSelectedColor(@ColorInt int selectedColor) {
        this.mSelectedColor = selectedColor;

        notifyDataSetChanged();
    }

    /**
     * Get the shape of the color swatches.
     *
     * @return The shape of the color swatches.
     */
    public @DynamicColorShape int getColorShape() {
        return mColorShape;
    }

    /**
     * Sets the shape of the color swatches.
     *
     * @param colorShape The color shape to be set.
     */
    public void setColorShape(@DynamicColorShape int colorShape) {
        this.mColorShape = colorShape;

        notifyDataSetChanged();
    }

    /**
     * Checks whether alpha is enabled for the colors.
     *
     * @return {@code true} to enable alpha for the colors.
     */
    public boolean isAlpha() {
        return mAlpha;
    }

    /**
     * Sets the alpha support for the colors.
     *
     * @param alpha {@code true} to enable alpha.
     */
    public void setAlpha(boolean alpha) {
        this.mAlpha = alpha;

        notifyDataSetChanged();
    }

    /**
     * View holder class to hold the color view.
     */
    static class ViewHolder {

        /**
         * Color view to display color on the adapter view.
         */
        private final DynamicColorView dynamicColorView;

        /**
         * Constructor to initialize views from the supplied layout.
         *
         * @param view The view for this view holder.
         */
    	ViewHolder(@NonNull View view) {
    	    dynamicColorView = view.findViewById(R.id.ads_color_view);
    	}

        /**
         * Get the color view to display color on the adapter view.
         *
         * @return The color view to display color on the adapter view.
         */
        DynamicColorView getDynamicColorView() {
            return dynamicColorView;
        }
    }
}

