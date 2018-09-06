package com.example.money.heart;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * Created by Money on 2018/8/8.
 */

public class Heart extends Fragment {

    bluetooth_40 Bl_heart0;
    Thread thread1heart;
    Message ss1heart;
    LineChart mChartheart,mChart2,mChart3;
    Activity thiscontext;
    ArrayList<String> setdatableheart ;
    Button a1,a2;
    ListView listView_use;
    RelativeLayout relayout1,relayout2;
    TextView bpmtxt,spo2txt;
    Message ss1;
     ArrayAdapter<String> adapter1;
     String[] DATA ={"123","456"};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragement_heart, container, false);
        mChartheart =(LineChart)root.findViewById(R.id.heartchart);
        a1 =(Button)root.findViewById(R.id.button2);
        a2 =(Button)root.findViewById(R.id.btn7);
        listView_use =(ListView)root.findViewById(R.id.listView);
        relayout1=(RelativeLayout)root.findViewById(R.id.r1);
        relayout2=(RelativeLayout)root.findViewById(R.id.r2);
        bpmtxt=(TextView)root.findViewById(R.id.bpm_txt);
        spo2txt=(TextView)root.findViewById(R.id.spo2_txt);
        mChart2=(LineChart)root.findViewById(R.id.chart2) ;
        mChart3=(LineChart)root.findViewById(R.id.chart3) ;

//
        settimgchart();
        Set_chart1();
        Set_chart2();
        return root;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onStart() {
        super.onStart();


      Bl_heart0=new bluetooth_40(thiscontext);
        Bl_heart0.mBleName =new  ArrayList<>();
        Bl_heart0.btArrayAdapter= new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,Bl_heart0.mBleName );
        listView_use.setAdapter(Bl_heart0.btArrayAdapter);

        listView_use.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("'jim"," onItemClick :"+Bl_heart0.mBleName.get(position));
                Bl_heart0.DEVICE_adress =Bl_heart0.mBleName.get(position);
                Bl_heart0.Connectoutside();
                changepage();
                rundata();
                thread1heart.start();
            }
        });
        a2.setOnClickListener(
                new Button.OnClickListener(){

                    @Override

                    public void onClick(View v) {
                        Log.i("jim","click");
                       // TODO Auto-generated method stub

                      //  rundata();
                     //   Log.i("jim","click");
                      //  thread1heart.start();
                    }

                }
        );

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        thiscontext =activity;
    }
    private void settimgchart(){
        mChartheart.setTouchEnabled(true);

        // 可拖曳
        mChartheart.setDragEnabled(true);

        // 可缩放
        mChartheart.setScaleEnabled(true);
        mChartheart.setDrawGridBackground(false);

        mChartheart.setPinchZoom(true);
        mChartheart.setDescription(null);
        // 设置图表的背景颜色
        mChartheart.setBackgroundColor(Color.WHITE);

        LineData data = new LineData();

        // 数据显示的颜色
        data.setValueTextColor(Color.BLACK);

        // 先增加一个空的数据，随后往里面动态添加
        mChartheart.setData(data);

        // 图表的注解(只有当数据集存在时候才生效)
        Legend l = mChartheart.getLegend();

        // 可以修改图表注解部分的位置
        // l.setPosition(LegendPosition.LEFT_OF_CHART);

        // 线性，也可是圆
        l.setForm(Legend.LegendForm.LINE);

        // 颜色
        l.setTextColor(Color.BLACK);

        // x坐标轴
        XAxis xl = mChartheart.getXAxis();
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
        YAxis leftAxis = mChartheart.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);

        // 最大值
        leftAxis.setAxisMaximum(80f);

        // 最小值
        leftAxis.setAxisMinimum(-50f);

        // 不一定要从0开始
        leftAxis.setStartAtZero(false);

        leftAxis.setDrawGridLines(false);

        YAxis rightAxis = mChartheart.getAxisRight();
        // 不显示图表的右边y坐标轴线
        rightAxis.setEnabled(false);
    }
    private  void Set_chart1(){




        // mChart.setDescription("Zhang Phil @ http://blog.csdn.net/zhangphil");
        //  mChart.setNoDataTextDescription("暂时尚无数据");

        mChart2.setTouchEnabled(true);

        // 可拖曳
        mChart2.setDragEnabled(true);

        // 可缩放
        mChart2.setScaleEnabled(true);
        mChart2.setDrawGridBackground(false);

        mChart2.setPinchZoom(true);
        mChart2.setDescription(null);
        // 设置图表的背景颜色
        mChart2.setBackgroundColor(Color.WHITE);

        LineData data1 = new LineData();

        // 数据显示的颜色
        data1.setValueTextColor(Color.BLACK);

        // 先增加一个空的数据，随后往里面动态添加
        mChart2.setData(data1);

        // 图表的注解(只有当数据集存在时候才生效)
        Legend l1 = mChart2.getLegend();

        // 可以修改图表注解部分的位置
        // l.setPosition(LegendPosition.LEFT_OF_CHART);

        // 线性，也可是圆
        l1.setForm(Legend.LegendForm.LINE);

        // 颜色
        l1.setTextColor(Color.BLACK);

        // x坐标轴
        XAxis x2 = mChart2.getXAxis();
        x2.setTextColor(Color.BLACK);
        x2.setDrawGridLines(false);
        x2.setAvoidFirstLastClipping(true);

        // 几个x坐标轴之间才绘制？
        // xl.setSpaceBetweenLabels(5);

        // 如果false，那么x坐标轴将不可见
        x2.setEnabled(true);

        // 将X坐标轴放置在底部，默认是在顶部。
        x2.setPosition(XAxis.XAxisPosition.BOTTOM);

        // 图表左边的y坐标轴线
        YAxis leftAxis1 = mChart2.getAxisLeft();
        leftAxis1.setTextColor(Color.BLACK);

        // 最大值
        leftAxis1.setAxisMaximum(150f);

        // 最小值
        leftAxis1.setAxisMinimum(-150f);

        // 不一定要从0开始
        leftAxis1.setStartAtZero(false);

        leftAxis1.setDrawGridLines(false);

        YAxis rightAxis1 = mChart2.getAxisRight();
        // 不显示图表的右边y坐标轴线
        rightAxis1.setEnabled(false);

    }
    private  void rundata(){
       // Log.i("jim","rundata");
        thread1heart =new Thread(new Runnable() {
            @Override

            public void run() {
                while(true){
                   // Log.i("jim","run");

                    try {
                        if(Bl_heart0.drawstus==true){

                            addEntry();
                            addEntry1();
                            addEntry2();
                            //Log.i("jim","in");
                            ss1 =new Message();
                            ss1.what =1;
                            mHandler.sendMessage(ss1);
                        }
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case  1:
                    bpmtxt.setText(Bl_heart0.readdata_ble4);
                    spo2txt.setText(Bl_heart0.readdata_ble5);
                    break;
            }

        }
    };
    private void addEntry(){
        LineData data = mChartheart.getData();

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
            mChartheart.notifyDataSetChanged();
        }
        // 像ListView那样的通知数据更新
        mChartheart.notifyDataSetChanged();

        // 当前统计图表中最多在x轴坐标线上显示的总量
        mChartheart.setVisibleXRangeMaximum(200);

        // 将坐标移动到最新
        // 此代码将刷新图表的绘图
        mChartheart.moveViewToX(data.getEntryCount() );
        // if(data != null &&data.getEntryCount()>200)
        Log.i("jim","Coun"+data.getEntryCount());
        if(data != null &&data.getEntryCount()>30)
        {
            data.removeDataSet(data.getDataSetCount() - 20);
            data.notifyDataChanged();
            mChartheart.notifyDataSetChanged();
            //mChart.invalidate();
        }
    }
    // 初始化数据集，添加一条统计折线，可以简单的理解是初始化y坐标轴线上点的表征
    private void addEntry1(){
        LineData data1 = mChart2.getData();

        // 每一个LineDataSet代表一条线，每张统计图表可以同时存在若干个统计折线，这些折线像数组一样从0开始下标。
        // 本例只有一个，那么就是第0条折线
        ILineDataSet set1 = data1.getDataSetByIndex(0);

        // 如果该统计折线图还没有数据集，则创建一条出来，如果有则跳过此处代码。
        if (set1 == null) {
            set1 = createSet1();
            data1.addDataSet(set1);
        }

        // 先添加一个x坐标轴的值
        // 因为是从0开始，data.getXValCount()每次返回的总是全部x坐标轴上总数量，所以不必多此一举的加1
        for(int i=0;i<bluetooth_40.blesetAry1.length;i++){

            data1.addEntry(new Entry(set1.getEntryCount(), (float) (bluetooth_40.blesetAry1[i])), 0);
            data1.notifyDataChanged();
            mChart2.notifyDataSetChanged();
        }
        // 像ListView那样的通知数据更新
        mChart2.notifyDataSetChanged();

        // 当前统计图表中最多在x轴坐标线上显示的总量
        mChart2.setVisibleXRangeMaximum(200);

        // 将坐标移动到最新
        // 此代码将刷新图表的绘图
        mChart2.moveViewToX(data1.getEntryCount() );
        // if(data != null &&data.getEntryCount()>200)

        if(data1 != null &&data1.getEntryCount()>30)
        {
            data1.removeDataSet(data1.getDataSetCount() - 20);
            data1.notifyDataChanged();
            mChart2.notifyDataSetChanged();
            //mChart.invalidate();
        }
    }
    private LineDataSet createSet1() {

        LineDataSet set1 = new LineDataSet(null, " ");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setLineWidth(3f);
        set1.setColor(ColorTemplate.getHoloBlue());

        set1.setHighlightEnabled(false);
        set1.setDrawValues(false);
        set1.setDrawCircles(false);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setCubicIntensity(0.2f);
        return set1;
    }
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
    private  void Set_chart2(){




        // mChart.setDescription("Zhang Phil @ http://blog.csdn.net/zhangphil");
        //  mChart.setNoDataTextDescription("暂时尚无数据");

        mChart3.setTouchEnabled(true);

        // 可拖曳
        mChart3.setDragEnabled(true);

        // 可缩放
        mChart3.setScaleEnabled(true);
        mChart3.setDrawGridBackground(false);

        mChart3.setPinchZoom(true);
        mChart3.setDescription(null);
        // 设置图表的背景颜色
        mChart3.setBackgroundColor(Color.WHITE);

        LineData data2 = new LineData();

        // 数据显示的颜色
        data2.setValueTextColor(Color.BLACK);

        // 先增加一个空的数据，随后往里面动态添加
        mChart3.setData(data2);

        // 图表的注解(只有当数据集存在时候才生效)
        Legend l2 = mChart3.getLegend();

        // 可以修改图表注解部分的位置
        // l.setPosition(LegendPosition.LEFT_OF_CHART);

        // 线性，也可是圆
        l2.setForm(Legend.LegendForm.LINE);

        // 颜色
        l2.setTextColor(Color.BLACK);

        // x坐标轴
        XAxis x3 = mChart3.getXAxis();
        x3.setTextColor(Color.BLACK);
        x3.setDrawGridLines(false);
        x3.setAvoidFirstLastClipping(true);

        // 几个x坐标轴之间才绘制？
        // xl.setSpaceBetweenLabels(5);

        // 如果false，那么x坐标轴将不可见
        x3.setEnabled(true);

        // 将X坐标轴放置在底部，默认是在顶部。
        x3.setPosition(XAxis.XAxisPosition.BOTTOM);

        // 图表左边的y坐标轴线
        YAxis leftAxis2 = mChart3.getAxisLeft();
        leftAxis2.setTextColor(Color.BLACK);

        // 最大值
        leftAxis2.setAxisMaximum(150f);

        // 最小值
        leftAxis2.setAxisMinimum(0f);

        // 不一定要从0开始
        leftAxis2.setStartAtZero(false);

        leftAxis2.setDrawGridLines(false);

        YAxis rightAxis2 = mChart3.getAxisRight();
        // 不显示图表的右边y坐标轴线
        rightAxis2.setEnabled(false);

    }
    private void addEntry2(){
        LineData data2 = mChart3.getData();

        // 每一个LineDataSet代表一条线，每张统计图表可以同时存在若干个统计折线，这些折线像数组一样从0开始下标。
        // 本例只有一个，那么就是第0条折线
        ILineDataSet set2 = data2.getDataSetByIndex(0);

        // 如果该统计折线图还没有数据集，则创建一条出来，如果有则跳过此处代码。
        if (set2 == null) {
            set2 = createSet2();
            data2.addDataSet(set2);
        }

        // 先添加一个x坐标轴的值
        // 因为是从0开始，data.getXValCount()每次返回的总是全部x坐标轴上总数量，所以不必多此一举的加1
        for(int i=0;i<bluetooth_40.blesetAry2.length;i++){

            data2.addEntry(new Entry(set2.getEntryCount(), (float) (bluetooth_40.blesetAry2[i])), 0);
            data2.notifyDataChanged();
            mChart3.notifyDataSetChanged();
        }
        // 像ListView那样的通知数据更新
        mChart3.notifyDataSetChanged();

        // 当前统计图表中最多在x轴坐标线上显示的总量
        mChart3.setVisibleXRangeMaximum(200);

        // 将坐标移动到最新
        // 此代码将刷新图表的绘图
        mChart3.moveViewToX(data2.getEntryCount() );
        // if(data != null &&data.getEntryCount()>200)

        if(data2 != null &&data2.getEntryCount()>30)
        {
            data2.removeDataSet(data2.getDataSetCount() - 20);
            data2.notifyDataChanged();
            mChart3.notifyDataSetChanged();
            //mChart.invalidate();
        }
    }
    private LineDataSet createSet2() {

        LineDataSet set2 = new LineDataSet(null, " ");
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setLineWidth(3f);
        set2.setColor(ColorTemplate.getHoloBlue());

        set2.setHighlightEnabled(false);
        set2.setDrawValues(false);
        set2.setDrawCircles(false);
        set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set2.setCubicIntensity(0.2f);
        return set2;
    }
    private  void changepage(){
        if(Bl_heart0.BLe_stus==true){
            relayout2.setVisibility(View.GONE);
            relayout1.setVisibility(View.VISIBLE);

        }
    }
}
