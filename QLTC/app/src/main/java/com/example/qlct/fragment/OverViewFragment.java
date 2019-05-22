package com.example.qlct.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.qlct.R;
import com.example.qlct.model.Item;
import com.example.qlct.realm.RealmController;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OverViewFragment extends Fragment {

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
    @BindView(R.id.spn_month)
    Spinner spnMonth;
    @BindView(R.id.spn_year)
    Spinner spnYear;

    private int monthNow;
    private int yearNow;

    private boolean isText = true;

    private float barWidth = 0.3f;
    private float barSpace = 0f;
    private float groupSpace = 0.4f;

    private RealmController realmController;
    private List<Item> addList = new ArrayList<>();
    private List<Item> subList = new ArrayList<>();
    private List<Item> periodicList = new ArrayList<>();

    private List<String> listYear = new ArrayList<>();

    private long allAdd;
    private long allSub;
    private long soDu;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        ButterKnife.bind(this, view);
        initView();
        getMonthNow();
        initSpinnerMonth();
        initSpinnerYear();
        getYearNow();
        getDataMonth(spnMonth.getSelectedItemPosition()+1, 2019);
//        getDataYear(spnYear.getSelectedItemPosition()+1);
        initData();
        initChart();
        return view;
    }

    private void initSpinnerMonth() {
        List<String> listMonth = new ArrayList<>();
        listMonth.add("Tháng 1"); listMonth.add("Tháng 2");
        listMonth.add("Tháng 3"); listMonth.add("Tháng 4");
        listMonth.add("Tháng 5"); listMonth.add("Tháng 6");
        listMonth.add("Tháng 7"); listMonth.add("Tháng 8");
        listMonth.add("Tháng 9"); listMonth.add("Tháng 10");
        listMonth.add("Tháng 11"); listMonth.add("Tháng 12");


        ArrayAdapter<String> adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, listMonth);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnMonth.setAdapter(adapter);
        spnMonth.setSelection(monthNow-1);
        spnMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getDataMonth(i, 2019);
                initData();
                initChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initSpinnerYear() {
        listYear = new ArrayList<>();
        listYear.add("2018"); listYear.add("2019");
        listYear.add("2020"); listYear.add("2021");
        listYear.add("2022"); listYear.add("2023");
        listYear.add("2024"); listYear.add("2025");
        listYear.add("2026"); listYear.add("2027");
        listYear.add("2028"); listYear.add("2029"); listYear.add("2030");


        ArrayAdapter<String> adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, listYear);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnYear.setAdapter(adapter);
        spnYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getDataYear(i);
                initData();
                initChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initData() {
        allAdd = 0;
        allSub = 0;
        soDu = 0;
        if(addList.size() != 0){
            for(Item i : addList){
                allAdd += i.getAmount();
            }
        }
        if(subList.size() != 0){
            for(Item i : subList){
                allSub += i.getAmount();
            }
        }
        if(periodicList.size() != 0){
            for(Item i : periodicList){
                if(i.isChecked()){
                    allSub += i.getAmount();
                }
            }
        }

        soDu = allAdd - allSub;

        txtTotalRevenue.setText(allAdd+"");
        txtTotalExpenditure.setText(allSub+"");
        txtSurplus.setText(soDu+"");
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
                    spnYear.setVisibility(View.GONE);
                    spnMonth.setVisibility(View.VISIBLE);
                    spnMonth.setSelection(monthNow-1);
                    getDataMonth(spnMonth.getSelectedItemPosition(), 2019);
                    if(isText){
//                        txtTotalRevenue.setText("month text");
                        initData();
                    } else {
//                        txtChartDemo.setText("month chart");
                        initChart();
                    }
                } else {
                    spnMonth.setVisibility(View.GONE);
                    spnYear.setVisibility(View.VISIBLE);
                    spnYear.setSelection(yearNow);
                    getDataYear(spnYear.getSelectedItemPosition());
                    if(isText){
//                        txtTotalRevenue.setText("year text");
                        initData();
                    } else {
//                        txtChartDemo.setText("year chart");
                        initChart();
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
//            txtChartDemo.setText("month chart");
            spnYear.setVisibility(View.GONE);
            spnMonth.setVisibility(View.VISIBLE);
            spnMonth.setSelection(monthNow-1);
            getDataMonth(spnMonth.getSelectedItemPosition(), 2019);
            initChart();
        } else {
//            txtChartDemo.setText("year chart");
            spnMonth.setVisibility(View.GONE);
            spnYear.setVisibility(View.VISIBLE);
            spnYear.setSelection(yearNow);
            getDataYear(spnYear.getSelectedItemPosition());
            initChart();
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
//            txtChartDemo.setText("month chart");
            spnYear.setVisibility(View.GONE);
            spnMonth.setVisibility(View.VISIBLE);
            spnMonth.setSelection(monthNow-1);
            getDataMonth(spnMonth.getSelectedItemPosition(), 2019);
            initData();
        } else {
//            txtChartDemo.setText("year chart");
            spnMonth.setVisibility(View.GONE);
            spnYear.setVisibility(View.VISIBLE);
            spnYear.setSelection(yearNow);
            getDataYear(spnYear.getSelectedItemPosition());
            initData();
        }
    }

    private void getDataMonth(int pos, int year){
        int month = pos + 1;
        realmController = new RealmController();
        addList = realmController.getItemYearMonth(1, 2019, month);
        subList = realmController.getItemYearMonth(2, 2019, month);
        periodicList = realmController.getItemYearMonth(3, 2019, month);
    }

    private void getDataYear(int pos){
        int year = Integer.parseInt(listYear.get(pos));
        realmController = new RealmController();
        addList = realmController.getItemYear(1, year);
        subList = realmController.getItemYear(2, year);
        periodicList = realmController.getItemYear(3, year);
    }

    private void getMonthNow(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String date = df.format(c);

        monthNow = Integer.parseInt(date.substring(3,5));
    }

    private void getYearNow(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String date = df.format(c);

        String year = date.substring(6);
        for (int i=0; i<listYear.size(); i++){
            if(year.equals(listYear.get(i))){
                yearNow = i;
            }
        }
    }

}
