package br.com.thiengo.pockerhijack.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.LinearLayout;

import br.com.thiengo.pockerhijack.R;
import br.com.thiengo.pockerhijack.domain.Video;
import br.com.thiengo.pockerhijack.domain.VideoManager;


public class VideoBubbleService extends Service {
    private WindowManager windowManager;
    private VideoManager videoManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Video video = intent.getParcelableExtra( Video.KEY );
        if( video != null ){
            videoManager = getVideoManager();
            videoManager.updateViewRoot( video );
        }
        return Service.START_REDELIVER_INTENT;
    }

    private VideoManager getVideoManager(){
        if( videoManager == null ){
            newVideoManager();
        }
        return videoManager;
    }

    private void newVideoManager(){
        LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate( R.layout.video_float_window, null, false );

        videoManager = new VideoManager( windowManager, layout );
        windowManager.addView( videoManager.getViewRoot(), videoManager.getParams() );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        windowManager.removeView( videoManager.getViewRoot() );
    }
}
