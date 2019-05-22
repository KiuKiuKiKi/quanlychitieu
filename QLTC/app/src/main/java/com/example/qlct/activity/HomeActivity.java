package com.example.qlct.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.qlct.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.lo_view)
    LinearLayout loView;
    @BindView(R.id.lo_chart)
    LinearLayout loChart;
    @BindView(R.id.txt_total_revenue)
    TextView txtTotalRevenue;
    @BindView(R.id.txt_total_expenditure)
    TextView txtTotalExpenditure;
    @BindView(R.id.txt_surplus)
    TextView txtSurplus;
    @BindView(R.id.txt_chart_demo)
    TextView txtChartDemo;
    @BindView(R.id.img_chart)
    ImageView imgChart;
    @BindView(R.id.img_text)
    ImageView imgText;
    @BindView(R.id.rdg_time)
    RadioGroup rdgTime;
    @BindView(R.id.rb_month)
    RadioButton rbMonth;
    @BindView(R.id.rb_year)
    RadioButton rbYear;
    @BindView(R.id.chart)
    BarChart chart;

    private boolean isText = true;

    private float barWidth = 0.3f;
    private float barSpace = 0f;
    private float groupSpace = 0.4f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_overview);
        ButterKnife.bind(this);
        fillData();
        initView();
        initChart();
    }

    private void fillData() {
        txtTotalRevenue.setText("month text");
        loChart.setVisibility(View.GONE);
    }

    private void initChart() {
        chart.setDescription(null);
        chart.setPinchZoom(false);
        chart.setScaleEnabled(false);
        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);

        int groupCount = 6;

        ArrayList xVals = new ArrayList();

        xVals.add("Jan");
        xVals.add("Feb");
        xVals.add("Mar");
        xVals.add("Apr");
        xVals.add("May");
        xVals.add("Jun");

        ArrayList yVals1 = new ArrayList();
        ArrayList yVals2 = new ArrayList();

        yVals1.add(new BarEntry(1, (float) 1));
        yVals2.add(new BarEntry(1, (float) 2));
        yVals1.add(new BarEntry(2, (float) 3));
        yVals2.add(new BarEntry(2, (float) 4));
        yVals1.add(new BarEntry(3, (float) 5));
        yVals2.add(new BarEntry(3, (float) 6));
        yVals1.add(new BarEntry(4, (float) 7));
        yVals2.add(new BarEntry(4, (float) 8));
        yVals1.add(new BarEntry(5, (float) 9));
        yVals2.add(new BarEntry(5, (float) 10));
        yVals1.add(new BarEntry(6, (float) 11));
        yVals2.add(new BarEntry(6, (float) 12));

        BarDataSet set1, set2;
        set1 = new BarDataSet(yVals1, "A");
        set1.setColor(Color.RED);
        set2 = new BarDataSet(yVals2, "B");
        set2.setColor(Color.BLUE);
        BarData data = new BarData(set1, set2);
        data.setValueFormatter(new LargeValueFormatter());
        chart.setData(data);
        chart.getBarData().setBarWidth(barWidth);
        chart.getXAxis().setAxisMinimum(0);
        chart.getXAxis().setAxisMaximum(0 + chart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        chart.groupBars(0, groupSpace, barSpace);
        chart.getData().setHighlightEnabled(false);
        chart.invalidate();

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
        l.setYOffset(20f);
        l.setXOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        //X-axis
        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMaximum(6);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));
//Y-axis
        chart.getAxisRight().setEnabled(false);
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(true);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f);
    }

    private void initView() {
        rdgTime.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rb_month){
                    if(isText){
                        txtTotalRevenue.setText("month text");
                    } else {
                        txtChartDemo.setText("month chart");
                    }
                } else {
                    if(isText){
                        txtTotalRevenue.setText("year text");
                    } else {
                        txtChartDemo.setText("year chart");
                    }
                }
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    @OnClick(R.id.img_chart)
    void showChart(){
        loChart.setVisibility(View.VISIBLE);
        loView.setVisibility(View.GONE);
        imgChart.setVisibility(View.GONE);
        imgText.setVisibility(View.VISIBLE);
        isText = false;
        if(rdgTime.getCheckedRadioButtonId() == R.id.rb_month){
            txtChartDemo.setText("month chart");
        } else {
            txtChartDemo.setText("year chart");
        }
    }

    @OnClick(R.id.img_text)
    void showText(){
        loChart.setVisibility(View.GONE);
        loView.setVisibility(View.VISIBLE);
        imgChart.setVisibility(View.VISIBLE);
        imgText.setVisibility(View.GONE);
        isText = true;
        if(rdgTime.getCheckedRadioButtonId() == R.id.rb_month){
            txtChartDemo.setText("month chart");
        } else {
            txtChartDemo.setText("year chart");
        }
    }
}
