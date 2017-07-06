package br.com.thiengo.pockerhijack.domain;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import br.com.thiengo.pockerhijack.MainActivity;
import br.com.thiengo.pockerhijack.R;
import br.com.thiengo.pockerhijack.extras.Util;


public class VideoManager implements View.OnTouchListener {
    private WindowManager windowManager;
    private LinearLayout viewRoot;
    private WindowManager.LayoutParams params;

    private boolean isClicked = false;
    private boolean isInRightSide = false;
    private int width;
    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;

    public VideoManager(WindowManager windowManager, LinearLayout layout ){
        this.windowManager = windowManager;
        setViewRoot( layout );
        setParams();
        setWidth();
    }

    private void setWidth() {
        Display display = windowManager.getDefaultDisplay();

        if( Util.isPlusEqualsApi13() ){
            Point size = new Point();
            display.getSize( size );
            width = size.x;
        }
        else{
            width = display.getWidth();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch ( event.getAction() ){
            case MotionEvent.ACTION_DOWN:
                actionDownUpdate( event );
                return true;
            case MotionEvent.ACTION_UP:
                actionUpUpdate( event );
                return true;
            case MotionEvent.ACTION_MOVE:
                actionMoveUpdate( event );
                return true;
        }

        return false;
    }

    private void actionDownUpdate( MotionEvent event ){
        initialX = params.x;
        initialY = params.y;
        initialTouchX = event.getRawX();
        initialTouchY = event.getRawY();
        isClicked = true;
    }

    private void actionUpUpdate( MotionEvent event ){
        int desiredPosition;
        int posX = params.x + viewRoot.getWidth() / 2;

        if( posX < width / 2 ){
            desiredPosition = 0;
            isInRightSide = false;
        }
        else{
            desiredPosition = width;
            isInRightSide = true;
        }

        slowDrawViewRootMove( desiredPosition );
        callActivityIfClicked();
    }

    private void actionMoveUpdate( MotionEvent event ){
        int extraVal = isInRightSide ? viewRoot.getWidth() * -1 : 0;

        params.x = initialX + extraVal + (int)( event.getRawX() - initialTouchX );
        params.y = initialY + (int)( event.getRawY() - initialTouchY );

        windowManager.updateViewLayout(viewRoot, params );
        isClicked = false;
    }

    private void callActivityIfClicked(){
        if( isClicked ){
            Intent intent = new Intent( viewRoot.getContext(), MainActivity.class);
            intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
            viewRoot.getContext().startActivity(intent);
        }
    }

    private void slowDrawViewRootMove(int desiredPosition ){
        int incDec = params.x < desiredPosition ? 1 : -1;

        while( params.x < desiredPosition
                || params.x > desiredPosition ){

            params.x += incDec;
            windowManager.updateViewLayout(viewRoot, params );
        }
    }

    public LinearLayout getViewRoot() {
        return viewRoot;
    }

    public void setViewRoot(LinearLayout viewRoot) {
        this.viewRoot = viewRoot;
        /*
         * getChildAt(0) É O ImageButton QUE PERMITE A
         * MUDANÇA DE POSIÇÃO DO QUADRO COM WebView.
         * */
        this.viewRoot.getChildAt(0).setOnTouchListener( this );
    }

    public WindowManager.LayoutParams getParams() {
        return params;
    }

    public void setParams() {
        this.params = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSPARENT
        );

        this.params.gravity = Gravity.TOP | Gravity.START;
        this.params.x = 0;
        this.params.y = 150;
    }

    public void updateViewRoot(Video video ){
        WebView wv = ((WebView) viewRoot.findViewById(R.id.wv_video));
        wv.getSettings().setJavaScriptEnabled( true );
        wv.setBackgroundColor(Color.TRANSPARENT);
        wv.setBackgroundResource(R.drawable.background_web_view);
        wv.loadUrl( "http://www.youtube.com/embed/"+ video.getYouTubeId() +"?autoplay=1&vq=small" );

        // PARA NÃO ABRIR O VÍDEO NO NAVEGADOR PADRÃO DO DEVICE.
        wv.setWebChromeClient( new WebChromeClient() );
        wv.setWebViewClient( new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
    }
}
