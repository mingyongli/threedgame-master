package com.ws3dm.app.util;

import android.os.CountDownTimer;

/**
 * Describution :  * 计时器 获取短信验证码倒计时
 * 
 * Author : DKjuan
 * 
 * Date : 2018/6/20 14:42
 * 
 * 初始化
 *  mTimer = new DownTimer();
     mTimer.setOnCountDownTimerListener(new DownTimer.OnCountDownTimerListener() {
        @Override
        public void onTick(long millisUntilFinished) {
            tv_scend.setText(millisUntilFinished + "ms\n" + millisUntilFinished / 1000 + "s");
        }
        
        @Override
        public void onFinish() {
            tv_scend.setText("已停止");
        }
    });
 
 获取当前状态
 DownTimer.TimerState state = mTimer.getTimerState();
 
 设置状态
 mTimer.start();//开始
 
 super.onPause();//暂停
 mTimer.pause();

 super.onResume();//恢复
 mTimer.resume();

 mTimer.stop();//停止
 
 mTimer.reset();//重新开始
 mTimer.start()
 
 **/
public class DownTimer {
     private final long DEFAULT_MILLIS_FUTURE = 60000;
     private final long DEFAULT_COUNT_DOWN_INTERVAL = 1000;

     private CountDownTimer mTimer;
     /**
      * 倒计时时间
      */
     private long mMillisInFuture = DEFAULT_MILLIS_FUTURE;
     /**
      * 间隔时间
      */
     private long mCountDownInterval = DEFAULT_COUNT_DOWN_INTERVAL;
     /**
      * 倒计时剩余时间
      */
     private long mMillisUntilFinished;

     private OnCountDownTimerListener mOnCountDownTimerListener;

     private TimerState mTimerState = TimerState.FINISH;

     public DownTimer() {

     }

     public DownTimer(long millisInFuture, long countDownInterval) {
         this.mMillisInFuture = millisInFuture;
         this.mCountDownInterval = countDownInterval;
     }

     public void start() {
         if (mTimerState != TimerState.START) {
             if (mTimer == null) {
                 reset();
             }
             mTimer.start();
             mTimerState = TimerState.START;
         }
     }

     public void pause() {
         if (mTimer != null && mTimerState == TimerState.START) {
             mTimer.cancel();
             mTimer = null;
             mTimerState = TimerState.PAUSE;
         }
     }

     public void resume() {
         if (mTimerState == TimerState.PAUSE) {
             mTimer = createCountDownTimer(mMillisUntilFinished, mCountDownInterval);
             mTimer.start();
             mTimerState = TimerState.START;
         }
     }

     public void stop() {
         if (mTimer != null) {
             mTimer.cancel();
             mTimer = null;
             mMillisUntilFinished = 0;
             mTimerState = TimerState.FINISH;
         }
     }

     public void reset() {
         stop();
         mTimer = createCountDownTimer(mMillisInFuture, mCountDownInterval);
     }


     public boolean isStart() {
         return mTimerState == TimerState.START;
     }

     public boolean isFinish() {
         return mTimerState == TimerState.FINISH;
     }

     protected CountDownTimer createCountDownTimer(long millisInFuture, long countDownInterval) {
         return new CountDownTimer(millisInFuture, countDownInterval) {
             @Override
             public void onTick(long millisUntilFinished) {
                 mMillisUntilFinished = millisUntilFinished;
                 if (mOnCountDownTimerListener != null) {
                     mOnCountDownTimerListener.onTick(mMillisUntilFinished);
                 }
             }

             @Override
             public void onFinish() {
                 if (mOnCountDownTimerListener != null) {
                     mOnCountDownTimerListener.onFinish();
                 }
             }
         };
     }

     public void setMillisInFuture(long millisInFuture) {
         this.mMillisInFuture = millisInFuture;
     }

     public void setCountDownInterval(long countDownInterval) {
         this.mCountDownInterval = countDownInterval;
     }

     public void setOnCountDownTimerListener(OnCountDownTimerListener listener) {
         this.mOnCountDownTimerListener = listener;
     }

     public long getMillisUntilFinished() {
         return mMillisUntilFinished;
     }

     public TimerState getTimerState() {
         return mTimerState;
     }

     /**
      * 倒计时监听
      * author  dengyuhan
      * created 2017/5/16 11:42
      */
     public interface OnCountDownTimerListener{
         void onTick(long millisUntilFinished);

         void onFinish();
     }

     public enum TimerState {
         START,PAUSE,FINISH
     }
}
