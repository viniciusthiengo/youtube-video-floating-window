package br.com.thiengo.pockerhijack;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import br.com.thiengo.pockerhijack.extras.Util;

public class FloatingWindowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floating_window);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if( getSupportActionBar() != null ){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateViews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void updateViews(){
        TextView tvNotificationText = (TextView) findViewById(R.id.tv_notification_text);
        Button btNotificationText = (Button) findViewById(R.id.bt_notification);

        if( Util.isSystemAlertPermissionGranted(this) ){
            tvNotificationText.setText( getResources().getString( R.string.notification_ok ) );
            btNotificationText.setVisibility( View.GONE );
        }
        else{
            tvNotificationText.setText( getResources().getString( R.string.notification_denied ) );
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    public void callAndroidSettings( View view ){
        String packageName = getPackageName();
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse( "package:"+packageName ));
        startActivity( intent );
    }
}
