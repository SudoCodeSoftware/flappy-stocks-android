package com.sudo_code.flappystocks;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.view.SurfaceView;

import java.util.Iterator;
import java.util.LinkedList;

public class GameView extends SurfaceView {

    private Paint mPaint;
    private final int mDataPointsOnScreen = 100;
    private double mYCenter;
    private double mYScale;
    private final double mYMoveRate = 0.01d;
    private long mLastPriceTime;    //In milliseconds
    private LinkedList<Double> mPriceData;

    public LinkedList<Double> getPriceData() {
        return mPriceData;
    }

    public int getMaxPriceDataSize() {
        return mDataPointsOnScreen;
    }

    public void setLastPriceTime(long lastPriceTime) {
        mLastPriceTime = lastPriceTime;
    }

    public GameView(Context context) {
        super(context);

        mYCenter = 0.f;
        mYScale = 1.d;

        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(5.f);
        mPaint.setTextSize(50);

        mPriceData = new LinkedList<Double>();

        AsyncTask retrieveFeedTask = new NetworkingTask().execute(this);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mPriceData.size() != 0) {
            mYCenter = mYCenter * (1.d - mYMoveRate) + mPriceData.getLast() * mYMoveRate;

            long elapsedTimeSinceLastPrice = System.currentTimeMillis() - mLastPriceTime;

            double prevX = (double) canvas.getWidth() - mPriceData.size() * canvas.getWidth() / mDataPointsOnScreen;
            double currX = prevX + (double) canvas.getWidth() / mDataPointsOnScreen;

            prevX -= (double) elapsedTimeSinceLastPrice / 100.d * canvas.getWidth() / mDataPointsOnScreen;
            currX -= (double) elapsedTimeSinceLastPrice / 100.d * canvas.getWidth() / mDataPointsOnScreen;

            Iterator<Double> priceIter = mPriceData.listIterator();
            double prevPrice = mPriceData.getFirst();
            while (priceIter.hasNext()) {
                double currentPrice = priceIter.next();

                double prevY = (double) canvas.getHeight() / 2.d + mYScale * (prevPrice - mYCenter);
                double currY = (double) canvas.getHeight() / 2.d + mYScale * (currentPrice - mYCenter);

                canvas.drawLine((float) prevX, (float) prevY, (float) currX, (float) currY, mPaint);

                currX += (double) canvas.getWidth() / mDataPointsOnScreen;
                prevX += (double) canvas.getWidth() / mDataPointsOnScreen;


                prevPrice = currentPrice;
            }
        }

        canvas.drawText("test", 50, 50, mPaint);

        invalidate();   //Make it recall onDraw
    }
}
