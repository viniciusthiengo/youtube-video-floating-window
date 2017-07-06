package br.com.thiengo.pockerhijack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import br.com.thiengo.pockerhijack.domain.Video;
import br.com.thiengo.pockerhijack.extras.Util;
import br.com.thiengo.pockerhijack.services.VideoBubbleService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if( Util.isSystemAlertPermissionGranted(this) ){
            findViewById(R.id.bt_open_vfw).setEnabled(true);
            findViewById(R.id.bt_close_vfw).setEnabled(true);
        }
        else{
            findViewById(R.id.bt_open_vfw).setEnabled(false);
            findViewById(R.id.bt_close_vfw).setEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*
         * CASO EM SEU DOMÍNIO DO PROBLEMA O USUÁRIO APENAS VEJA O
         * VÍDEO NA FLOATING WINDOW DEPOIS DE SAIR DO APLICATIVO,
         * AQUI É AONDE DEVERÁ TER O CÓDIGO DE INICIALIZAÇÃO DE
         * FLOATING WINDOW.
         * */
        //iniciarVideoFloatingWindow();
    }



    private void iniciarVideoFloatingWindow(){
        Video video = new Video();
        video.setYouTubeId( "M5pfTIE8E8Y" );

        Intent intent = new Intent( getApplicationContext(), VideoBubbleService.class);
        intent.putExtra( Video.KEY, video );
        startService( intent );
    }

    private void encerrarVideoFloatingWindow(){
        stopService( new Intent( this, VideoBubbleService.class) );
    }



    /* CLICK LISTENER */
    public void openFloatingWindowActivity(View v){
        Intent intent = new Intent(this, FloatingWindowActivity.class);
        startActivity( intent );
    }

    public void openVideoFloatingWindow(View v){
        iniciarVideoFloatingWindow();
    }

    public void closeVideoFloatingWindow(View v){
        encerrarVideoFloatingWindow();
    }
}
