package com.example.money.heart;

import android.util.Log;

/**
 * Created by Money on 2018/8/2.
 */

public class Analysis_value {
    public  float Bpm_data,static_heart,max_heart,age_use=23,sport_level =70,heartgoal;

    private  void Heartrate_calculate(){
    //---做心率計算
        max_heart =220-age_use; //最大心律
        heartgoal =max_heart *sport_level/100; //運動時目標心率，sport_level 可以自行設定，分為五種強度，強度<5>90-100，強度<4>80-90,強度<3>70-80強度<2>60-70,強度<1>50-60

    }
}
