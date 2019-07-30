package com.example.flyjiang.ratehalfcircleview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.example.flyjiang.ratehalfcircleview.widegt.RateHalfCircleView;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private  String app ="";
    private  String app01 ="";
    private  String app03 ="";
    private  String app04 ="";
    RateHalfCircleView mRateHalfCircleView ;
    private static final int MSG_RATE_UPDATE = 0x110;
    float rate_now=1f;
    float rate_ing =0f;

  /*  private  Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            float rate_ing_per = 1 / ((180-4*mRateHalfCircleView.getAngle()-2*mRateHalfCircleView.getBb())*rate_now/5);
             rate_ing += rate_ing_per ;
            if(rate_ing>=rate_now-(rate_ing_per)){
                rate_ing = 0;
            }

            mRateHalfCircleView.setRate_angle(rate_ing);
            mHandler.sendEmptyMessageDelayed(MSG_RATE_UPDATE,200);
        }

    };*/

    private final MyHandler handler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRateHalfCircleView = (RateHalfCircleView) findViewById(R.id.rate_view);
        ViewGroup.LayoutParams layoutParams = mRateHalfCircleView.getLayoutParams();
        layoutParams.height =400;
        layoutParams.width =1000;    //宽可以设定为屏幕相应比率尺寸，高与宽最好保持2.5的倍数关系
        mRateHalfCircleView.setLayoutParams(layoutParams);
        mRateHalfCircleView.setRate(rate_now);//设置当前比率
        mRateHalfCircleView.setStatue(1);//设定当前会员等级
        mRateHalfCircleView.setRate_angle(0);
        handler.sendEmptyMessage(MSG_RATE_UPDATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(MSG_RATE_UPDATE);
    }

    private static class MyHandler extends Handler{

        //对Activity的弱引用
        private final WeakReference<MainActivity> mActivity;

        public MyHandler(MainActivity activity){
            mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mActivity.get();
            if(activity==null){
                super.handleMessage(msg);
                return;
            }
            switch (msg.what) {
                case MSG_RATE_UPDATE:
                    float rate_ing_per = 1 / ((180-4*activity.mRateHalfCircleView.getAngle()-2*activity.mRateHalfCircleView.getBb())*activity.rate_now/5);
                    activity.rate_ing += rate_ing_per ;
                    if(activity.rate_ing>=activity.rate_now-(rate_ing_per)){
                        activity.rate_ing = 0;
                    }

                    activity.mRateHalfCircleView.setRate_angle(activity.rate_ing);
                    activity.handler.sendEmptyMessageDelayed(MSG_RATE_UPDATE,200);
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }
}
