package com.example.flyjiang.ratehalfcircleview.widegt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.flyjiang.ratehalfcircleview.R;

/**
 * Created by ${flyjiang} on 2017/3/21.
 * 文件说明：
 */
public class RateHalfCircleView extends View {

    private Paint mPaint;
    private Paint mRateHalfPaint;//圆弧的画笔
    private Paint mSmallPaint;  //弧线上小圆的画笔
    private Paint mSmallPaint_ing; //移动的小圆弧的画笔
    Rect mBounds ; //整个绘图区域矩形
    int r = 70; //图片的半径
    int marging = 15; //最左和最右边的文字margin
    int margingbottom = 60;
    float rate = 0.8f; //当前比率
    int  rSmall = 20; //弧线上小圆的半径
    int  thickness = 6;//弧线的粗细
    int  thickness_ing = 15;//弧线的粗细
    private Bitmap image01;
    private Bitmap image02;
    private int statue = 1;//当前状态，普通会员，黄金会员，钻石会员
    public final static float PAI  = 3.14f;//圆周率

    float rate_angle = 0;

    private int per_hu = 5;

    float angle = 0f;

    float bb;

    public RateHalfCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        image01 = BitmapFactory.decodeResource(this.getContext().getResources(), R.mipmap.ic_launcher);
        image02 = BitmapFactory.decodeResource(this.getContext().getResources(), R.mipmap.add);

        mBounds = new Rect(0,0,getMeasuredWidth(),getMeasuredHeight());

        initPaint();

    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 圆弧及图表区域范围
         */
        mBounds.left = marging + r;
        mBounds.top =r;
        mBounds.right = getMeasuredWidth()-marging-r;
        mBounds.bottom  = getMeasuredHeight()-r-margingbottom-40;
        /**
         * 算出圆弧的圆心坐标和半径
         */
        float x = getMeasuredWidth()/2;
        float y = ((x-mBounds.left)*(x-mBounds.left)+mBounds.top*mBounds.top-mBounds.bottom*mBounds.bottom)/2/(mBounds.top-mBounds.bottom);
        float rv  =   mBounds.bottom-y;

        RectF mCircle = new RectF((int)(x-rv),(int)(y-rv),(int)(x+rv),mBounds.bottom);//圆弧所在的圆所在矩形
        float aa = (float) Math.sqrt( (x-rv-marging-r)*(x-rv-marging-r) +(r-y)*(r-y))/2; //小半弧
        angle = (float) Math.asin(aa/rv)*180/PAI; //起点的角度
        bb = 2*(float)(Math.asin(r/rv/2)*180/PAI);

        //画图片
        mPaint.setColor(Color.YELLOW);
        //三个圆，需用图片替代
        canvas.drawCircle(marging + r, r, r, mPaint);
        canvas.drawCircle(getMeasuredWidth() - marging - r, r, r, mPaint);


        //画圆弧
        mRateHalfPaint.setColor(Color.GREEN);
        canvas.drawArc(mCircle,2*angle+bb,(1-rate)*(180-4*angle-2*bb),false,mRateHalfPaint);

        mRateHalfPaint.setColor(Color.BLUE);
        canvas.drawArc(mCircle,2*angle+bb+(1-rate)*(180-4*angle-2*bb),rate*(180-4*angle-2*bb),false,mRateHalfPaint);



        switch ( statue){
            case 1:
                canvas.drawBitmap(image01,getMeasuredWidth()/2-r, mBounds.bottom-2*r,mPaint);
                break;
            case 2:
                canvas.drawBitmap(image02,getMeasuredWidth()/2-r, mBounds.bottom-2*r,mPaint);
                break;
        }

        //画文字
        mPaint.setTextSize(40f);
        mPaint.setColor(Color.WHITE);
        //文字需是适配下位置
        canvas.drawText("普通会员",0,2*r+margingbottom,mPaint);
        canvas.drawText("钻石会员",getMeasuredWidth()-2*r-marging,2*r+margingbottom,mPaint);
        canvas.drawText("黄金会员",x-r-marging, mBounds.bottom+r+margingbottom,mPaint);

        float cc =(float)(Math.asin((r-y)/rv)*180/PAI) ;
        float dd = (180-bb)/2-cc;
        float ee = (float) (Math.cos(dd*PAI/180) ) *r;

        //弧线上小圆的坐标
        float xSmall = rate*(getMeasuredWidth() - 2*marging - 2*r-2*ee )+marging+r+ee;
        float ySmall =(float)(Math.sqrt(rv*rv-(x-xSmall)*(x-xSmall))+y);
        //画小圆
        mSmallPaint.setColor(Color.WHITE);
        canvas.drawCircle(xSmall, ySmall, rSmall, mSmallPaint);

        //移动的弧线
        mSmallPaint_ing.setColor(Color.WHITE);
        canvas.drawArc(mCircle, (180-2 * angle -bb-per_hu - rate_angle*(180 - 4 * angle-2*bb)), per_hu, false, mSmallPaint_ing);

        mPaint.setColor(Color.YELLOW);  //中间的圆，最后画，覆盖住移动的弧线
        canvas.drawCircle(getMeasuredWidth()/2, mBounds.bottom, r, mPaint);

    }

    public void initPaint(){
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);

        mRateHalfPaint = new Paint();
        mRateHalfPaint.setStyle(Paint.Style.STROKE);
        mRateHalfPaint.setStrokeWidth(thickness);

        mSmallPaint = new Paint();
        mSmallPaint.setStyle(Paint.Style.FILL);

        mSmallPaint_ing = new Paint();
        mSmallPaint_ing.setStyle(Paint.Style.STROKE);
        mSmallPaint_ing.setStrokeWidth(thickness_ing);
    }

    public void setRate_angle(float rate_angle) {
        this.rate_angle = rate_angle;
        invalidate();
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public void setStatue(int statue) {
        this.statue = statue;
    }

    public float getAngle() {
        return angle;
    }

    public float getBb() {
        return bb;
    }
}
