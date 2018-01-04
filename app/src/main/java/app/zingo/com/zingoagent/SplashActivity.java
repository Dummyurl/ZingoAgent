package app.zingo.com.zingoagent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


public class SplashActivity extends AppCompatActivity {

    private ImageView app_logo;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        app_logo = (ImageView)findViewById(R.id.app_log);
        text = (TextView) findViewById(R.id.text);
        Animation logo_anim = AnimationUtils.loadAnimation(this,R.anim.transition);
        app_logo.startAnimation(logo_anim);
        text.startAnimation(logo_anim);
        final Intent intent = new Intent(this,MainActivity.class);
        Thread timer = new Thread(){
            public void run(){
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(intent);
                    finish();

                }
            }
        };
        timer.start();
    }
}