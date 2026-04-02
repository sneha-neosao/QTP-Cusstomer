package util;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.grocery.QTPmart.R;

public class CurvedBottomNavigationView extends CardView {
    private Path mPath;
    private Paint mPaint;
    private Paint drawPaint;

    /** the CURVE_CIRCLE_RADIUS represent the radius of the fab button */
    private final int CURVE_CIRCLE_RADIUS = 60/2;
    // the coordinates of the first curve
    private Point mFirstCurveStartPoint = new Point();
    private Point mFirstCurveEndPoint = new Point();
    private Point mFirstCurveControlPoint1 = new Point();
    private Point mFirstCurveControlPoint2 = new Point();

    //the coordinates of the second curve
    @SuppressWarnings("FieldCanBeLocal")
    private Point mSecondCurveStartPoint = new Point();
    private Point mSecondCurveEndPoint = new Point();
    private Point mSecondCurveControlPoint1 = new Point();
    private Point mSecondCurveControlPoint2 = new Point();
    private int mNavigationBarWidth;
    private int mNavigationBarHeight;

    public CurvedBottomNavigationView(Context context) {
        super(context);
        init();
    }

    public CurvedBottomNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CurvedBottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(getResources().getColor(R.color.aqua));
        setBackgroundColor(Color.TRANSPARENT);
        setupPaint();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // get width and height of navigation bar
        // Navigation bar bounds (width & height)
        mNavigationBarWidth = getWidth();
        mNavigationBarHeight = getHeight();
        // the coordinates (x,y) of the start point before curve
        mFirstCurveStartPoint.set((mNavigationBarWidth / 6) - (CURVE_CIRCLE_RADIUS * 2) - (CURVE_CIRCLE_RADIUS / 3), 0);
        // the coordinates (x,y) of the end point after curve
            mFirstCurveEndPoint.set(mNavigationBarWidth / 6, CURVE_CIRCLE_RADIUS + (CURVE_CIRCLE_RADIUS / 4));
        // same thing for the second curve
        mSecondCurveStartPoint = mFirstCurveEndPoint;
        mSecondCurveEndPoint.set((mNavigationBarWidth / 6) + (CURVE_CIRCLE_RADIUS * 2) + (CURVE_CIRCLE_RADIUS / 3), 0);

        // the coordinates (x,y)  of the 1st control point on a cubic curve
        mFirstCurveControlPoint1.set(mFirstCurveStartPoint.x + CURVE_CIRCLE_RADIUS + (CURVE_CIRCLE_RADIUS / 4), mFirstCurveStartPoint.y);
        // the coordinates (x,y)  of the 2nd control point on a cubic curve
        mFirstCurveControlPoint2.set(mFirstCurveEndPoint.x - (CURVE_CIRCLE_RADIUS * 2) + CURVE_CIRCLE_RADIUS, mFirstCurveEndPoint.y);

        mSecondCurveControlPoint1.set(mSecondCurveStartPoint.x + (CURVE_CIRCLE_RADIUS * 2) - CURVE_CIRCLE_RADIUS, mSecondCurveStartPoint.y);
        mSecondCurveControlPoint2.set(mSecondCurveEndPoint.x - (CURVE_CIRCLE_RADIUS + (CURVE_CIRCLE_RADIUS / 4)), mSecondCurveEndPoint.y);

        mPath.reset();
        mPath.moveTo(0, 0);
        Log.e("Curvepoints","P1:"+mFirstCurveStartPoint.x
                +"\n"+"P2:"+mFirstCurveStartPoint.y
                +"\n"+"P2:"+mFirstCurveStartPoint.y
                +"\n"+"c1:"+"x: "+ mFirstCurveControlPoint1.x+"y : "+mFirstCurveControlPoint1.y
                +"\n"+"c2:"+"x: "+ mFirstCurveControlPoint2.x+"y :"+mFirstCurveControlPoint2.y+
                "\n"+"P3:"+"x:"+ mFirstCurveEndPoint.x+"y"+mFirstCurveEndPoint.y

        );
        Log.e("lineFromP1toP2","x :" +mFirstCurveStartPoint.x+",y :"+mFirstCurveStartPoint.y);
        mPath.lineTo(mFirstCurveStartPoint.x, mFirstCurveStartPoint.y);

        mPath.cubicTo(mFirstCurveControlPoint1.x, mFirstCurveControlPoint1.y,
                mFirstCurveControlPoint2.x, mFirstCurveControlPoint2.y,
                mFirstCurveEndPoint.x, mFirstCurveEndPoint.y);

        Log.e("SecondCurvePoints","P1:"+mFirstCurveStartPoint.x
                +"\n"+"P2:"+mFirstCurveStartPoint.y
                +"\n"+"P2:"+mFirstCurveStartPoint.y
                +"\n"+"c4:"+"x: "+ mSecondCurveControlPoint1.x+"y : "+mSecondCurveControlPoint1.y
                +"\n"+"c3:"+"x: "+ mSecondCurveControlPoint2.x+"y :"+mSecondCurveControlPoint2.y+
                "\n"+"P4:"+"x:"+ mSecondCurveEndPoint.x+"y"+mSecondCurveEndPoint.y

        );
        mPath.cubicTo(mSecondCurveControlPoint1.x, mSecondCurveControlPoint1.y,
                mSecondCurveControlPoint2.x, mSecondCurveControlPoint2.y,
                mSecondCurveEndPoint.x, mSecondCurveEndPoint.y);

        Log.e("lineFromP4toP5","x :" +mNavigationBarWidth+",0");
        mPath.lineTo(mNavigationBarWidth, 0);
        Log.e("lineFromP5toP6","x :" +mNavigationBarWidth+",y :"+mNavigationBarHeight);
        mPath.lineTo(mNavigationBarWidth, mNavigationBarHeight);
        Log.e("lineFromP6toP7","x :" +0+",y :"+mNavigationBarHeight);
        mPath.lineTo(0, mNavigationBarHeight);
        mPath.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
        canvas.drawCircle(mFirstCurveEndPoint.x, CURVE_CIRCLE_RADIUS/2, 10, drawPaint);
    }

    private void setupPaint() {
        drawPaint = new Paint();
        drawPaint.setColor(getResources().getColor(R.color.orange));
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(5);
        drawPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }
}