package com.example.money.heart;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends Activity {

    bluetooth_40 Bl_0;
    bluetooth_41 Bl_1;
    TextView bpm_txt;
    Thread thread1,thread2;
    Message ss1,ss2;
    LineChart mChart,mChart2 ;
    ILineDataSet set,set2;
    int usedata =0,usedata1 =0,savecount=0;
    float datause;
    FirebaseDatabase database;
    ArrayList setdatable ;
    float [] setdata2 =new float[100];
    //-------------------------------------------------
    ListView listView_use;
    RelativeLayout relayout1,relayout2;
    TextView bpmtxt,spo2txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mChart = (LineChart) findViewById(R.id.chart1);


        bpm_txt=(TextView)findViewById(R.id.text);
        //------------------------------------
        listView_use=(ListView)findViewById(R.id.listView);
        relayout1=(RelativeLayout)findViewById(R.id.r1);
        relayout2=(RelativeLayout)findViewById(R.id.r2);
        bpmtxt=(TextView)findViewById(R.id.bpm_txt);
        spo2txt=(TextView)findViewById(R.id.spo2_txt);
       // Bl_0 =new bluetooth_40(this);

        //Bl_0.mBleName =new  ArrayList<>();
       // Bl_0.btArrayAdapter= new ArrayAdapter<String>(this,
             //   android.R.layout.simple_list_item_1,Bl_0.mBleName );
     /*   listView_use.setAdapter(Bl_0.btArrayAdapter);
        listView_use.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("'jim"," onItemClick :"+Bl_0.mBleName.get(position));
                Bl_0.DEVICE_adress =Bl_0.mBleName.get(position);
                Bl_0.Connectoutside();
                changepage();
            }
        });
        setdatable =new ArrayList();*/

       Set_chart();

        thread1 =new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        if(Bl_0.drawstus==true){

                            addEntry();
                        }
                     /*  if(Bl_0.readdata_ble!=null){
                            datause =Float.valueOf(Bl_0.readdata_ble);
                            usedata=(int)(datause);
                            // usedata=(int)(datause);
                        }*/
                    /*   ss1 =new Message();
                        ss1.what =1;
                        mHandler.sendMessage(ss1);
                        if(Bl_0.readdata_ble!=null){
                          usedata =Integer.parseInt(Bl_0.readdata_ble);
                        }
                        if(Bl_0.readdata_ble1!=null){
                            datause =Float.valueOf(Bl_0.readdata_ble1);
                            usedata1=(int)(datause);
                           // usedata=(int)(datause);
                        }
                      addEntry();*/
                      //savearydata();
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
       // thread1.start();
    }
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    bpm_txt.setText(""+usedata1);
                    break;
            }
        }
    };
    private void addEntry(){
        LineData data = mChart.getData();

        // 每一个LineDataSet代表一条线，每张统计图表可以同时存在若干个统计折线，这些折线像数组一样从0开始下标。
        // 本例只有一个，那么就是第0条折线
        ILineDataSet set = data.getDataSetByIndex(0);

        // 如果该统计折线图还没有数据集，则创建一条出来，如果有则跳过此处代码。
        if (set == null) {
            set = createSet();
            data.addDataSet(set);
        }

        // 先添加一个x坐标轴的值
        // 因为是从0开始，data.getXValCount()每次返回的总是全部x坐标轴上总数量，所以不必多此一举的加1
        for(int i=0;i<bluetooth_40.blesetAry.length;i++){

            data.addEntry(new Entry(set.getEntryCount(), (float) (bluetooth_40.blesetAry[i])), 0);
            data.notifyDataChanged();
            mChart.notifyDataSetChanged();
        }
        /*for(int i=0;i<setdata2.length;i++){
          data.addEntry(new Entry(set.getEntryCount(), (float) (setdata2[i])), 0);
            //Log.i("JIM","ARRAYBUFFER:  "+setdata2.length);
           // Log.i("JIM","ARRAYBUFFER:  "+setdata2[i]);
            data.notifyDataChanged();
            mChart.notifyDataSetChanged();
        }*/



        // 像ListView那样的通知数据更新
        mChart.notifyDataSetChanged();

        // 当前统计图表中最多在x轴坐标线上显示的总量
        mChart.setVisibleXRangeMaximum(200);

        // 将坐标移动到最新
        // 此代码将刷新图表的绘图
        mChart.moveViewToX(data.getEntryCount() );
       // if(data != null &&data.getEntryCount()>200)
        Log.i("jim","Coun"+data.getEntryCount());
        if(data != null &&data.getEntryCount()>30)
        {
            data.removeDataSet(data.getDataSetCount() - 20);
            data.notifyDataChanged();
            mChart.notifyDataSetChanged();
            //mChart.invalidate();
        }
    }
    // 初始化数据集，添加一条统计折线，可以简单的理解是初始化y坐标轴线上点的表征
    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, " ");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(3f);
        set.setColor(ColorTemplate.getHoloBlue());

        set.setHighlightEnabled(false);
        set.setDrawValues(false);
        set.setDrawCircles(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        return set;
    }
    private  void Set_chart(){




        // mChart.setDescription("Zhang Phil @ http://blog.csdn.net/zhangphil");
        //  mChart.setNoDataTextDescription("暂时尚无数据");

        mChart.setTouchEnabled(true);

        // 可拖曳
        mChart.setDragEnabled(true);

        // 可缩放
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);

        mChart.setPinchZoom(true);
        mChart.setDescription(null);
        // 设置图表的背景颜色
        mChart.setBackgroundColor(Color.WHITE);

        LineData data = new LineData();

        // 数据显示的颜色
        data.setValueTextColor(Color.BLACK);

        // 先增加一个空的数据，随后往里面动态添加
        mChart.setData(data);

        // 图表的注解(只有当数据集存在时候才生效)
        Legend l = mChart.getLegend();

        // 可以修改图表注解部分的位置
        // l.setPosition(LegendPosition.LEFT_OF_CHART);

        // 线性，也可是圆
        l.setForm(Legend.LegendForm.LINE);

        // 颜色
        l.setTextColor(Color.BLACK);

        // x坐标轴
        XAxis xl = mChart.getXAxis();
        xl.setTextColor(Color.BLACK);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);

        // 几个x坐标轴之间才绘制？
        // xl.setSpaceBetweenLabels(5);

        // 如果false，那么x坐标轴将不可见
        xl.setEnabled(true);

        // 将X坐标轴放置在底部，默认是在顶部。
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);

        // 图表左边的y坐标轴线
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);

        // 最大值
        leftAxis.setAxisMaximum(500f);

        // 最小值
        leftAxis.setAxisMinimum(-300f);

        // 不一定要从0开始
        leftAxis.setStartAtZero(false);

        leftAxis.setDrawGridLines(false);

        YAxis rightAxis = mChart.getAxisRight();
        // 不显示图表的右边y坐标轴线
        rightAxis.setEnabled(false);

    }
    private  void changepage(){
        if(Bl_0.BLe_stus==true){
            Log.i("'jim"," connecttrue");
            relayout2.setVisibility(View.GONE);
            relayout1.setVisibility(View.VISIBLE);

        }
    }
    public void onClick(View view){
        switch (view.getId()){

        }
    }


}
