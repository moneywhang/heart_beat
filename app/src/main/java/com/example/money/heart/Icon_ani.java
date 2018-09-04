package com.example.money.heart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

public class Icon_ani extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_icon_ani);


        ImageView imageview = (ImageView) findViewById(R.id.imageView1);

        imageview.setImageResource(R.mipmap.logo1);

        AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
        aa.setDuration(3000);
        imageview.setAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {

            }

            public void onAnimationRepeat(Animation animation) {

            }

            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent();
              //  intent.setClass(Icon_ani.this,Totalactivity.class);
                intent.setClass(Icon_ani.this,Totalactivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

}
