/*
 * Copyright (C) 2014 Itai Hanski
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.itaihanski.triangleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

/**
 * The {@code TriangleView} allows for simple drawing of the triangle shape. The actual size and
 * shape if the triangle is determined by the size of this view. The direction and color can be set
 * in code using the public methods or via the XML attributes: {@code tr_direction} and
 * {@code tr_color}
 *
 * @author Itai Hanski
 */
public class TriangleView extends View {

    private static final Direction DEFAULT_DIRECTION = Direction.LEFT;
    private static final int DEFAULT_COLOR = 0xff757575;

    private Paint mPaint;
    private Path mTrianglePath;
    private Direction mDirection;
    private int mColor;

    public TriangleView(Context context) {
        this(context, null);
    }

    public TriangleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TriangleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    //
    // Initialization
    //

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TriangleView);
            switch (a.getInt(R.styleable.TriangleView_tr_direction, 0)) {
                case 0:
                    mDirection = Direction.LEFT;
                    break;
                case 1:
                    mDirection = Direction.UP;
                    break;
                case 2:
                    mDirection = Direction.RIGHT;
                    break;
                case 3:
                default:
                    mDirection = Direction.DOWN;
            }
            mColor = a.getColor(R.styleable.TriangleView_tr_color, DEFAULT_COLOR);
            a.recycle();
        } else {
            mDirection = DEFAULT_DIRECTION;
            mColor = DEFAULT_COLOR;
        }

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mColor);
    }

    //
    // Public API
    //

    /**
     * Set the color of the triangle.
     * @param color the color of the triangle.
     */
    public void setColor(int color) {
        if (mColor != color) {
            mColor = color;
            if (mPaint != null) {
                mPaint.setColor(color);
            }
            mTrianglePath = null;
        }
    }

    /**
     * Set the direction of the triangle.
     * @param direction the direction of the triangle.
     */
    public void setDirection(Direction direction) {
        if (direction != mDirection) {
            mDirection = direction;
            mTrianglePath = null;
        }
    }

    //
    // View Overrides
    //

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(getTrianglePath(), mPaint);
    }

    //
    // Utility Methods
    //

    private Path getTrianglePath() {
        if (mTrianglePath == null) {
            mTrianglePath = new Path();
            int width = getWidth();
            int height = getHeight();
            Point p1, p2, p3;
            switch (mDirection) {
                case LEFT:
                    p1 = new Point(width, 0);
                    p2 = new Point(width, height);
                    p3 = new Point(0, height / 2);
                    break;
                case UP:
                    p1 = new Point(0, height);
                    p2 = new Point(width, height);
                    p3 = new Point(width / 2, 0);
                    break;
                case RIGHT:
                    p1 = new Point(0, 0);
                    p2 = new Point(0, height);
                    p3 = new Point(width, height / 2);
                    break;
                case DOWN:
                default:
                    p1 = new Point(0, 0);
                    p2 = new Point(width, 0);
                    p3 = new Point(width / 2, height);
            }
            mTrianglePath.moveTo(p1.x, p1.y);
            mTrianglePath.lineTo(p2.x, p2.y);
            mTrianglePath.lineTo(p3.x, p3.y);
        }
        return mTrianglePath;
    }

    //
    // Direction
    //

    public enum Direction {
        LEFT,
        UP,
        RIGHT,
        DOWN
    }
}
