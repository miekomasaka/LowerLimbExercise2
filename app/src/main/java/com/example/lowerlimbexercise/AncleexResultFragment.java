package com.example.lowerlimbexercise;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AncleexResultFragment extends Fragment {

    //private ImageView UttiImage;
    private PieChart mPieChartleftAncle;
    private PieChart mPieChartrightAncle;
    // private PieChart mPieChartunder;
    private LineChart mChart;
    private List<AncleexData> mLad = null;              //足関節運動データリスト


    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_ancleexresult, container, false);

        mPieChartleftAncle= (PieChart) view.findViewById(R.id.Pie_chartleftancke);
        mPieChartrightAncle = (PieChart)view.findViewById(R.id.Pie_chartrightancle);
        // mPieChartunder = (PieChart)findViewById(R.id.Pie_chartunder);

        //アクティビティからデータを取得
        FragmentActivity factivity = getActivity();
        AncleexData ancleexData = ((AncleExerciseActivity)factivity).getancleexdata();

        setupPieChartLeftAncleView(ancleexData);
        setupPieChartRightAncleView(ancleexData);
        //setupPieChartunderView();

        mChart = view.findViewById(R.id.line_chart);
        setupLineChartView();

        return view;
    }

    private void setupPieChartLeftAncleView(AncleexData ancleexData) {
        mPieChartleftAncle.setUsePercentValues(true);

        Description description = new Description();
        description.setTextColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        description.setText("左足");
        description.setTextSize(24);
        mPieChartleftAncle.setDescription(description);
        //mPieChart.setDescription("チャートの説明");

        Legend legend = mPieChartleftAncle.getLegend();
        //legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);

        // 円グラフに表示するデータ
        //角度不足,適正,角度超過
        /*float under=  ancleexData.count_under_l;
        float best=  ancleexData.count_best_l;
        float over =  ancleexData.count_over_l;
        System.out.println("under:"+under+",best:"+best+",over:"+over);
        List<Float> values = Arrays.asList(under, best, over);
        //List<Entry> entries = new ArrayList<>();
        List<PieEntry> entries = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            //entries.add(new Entry(values.get(i), i));
            entries.add(new PieEntry(values.get(i), i));
        }*/

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(ancleexData.count_under_l, "不足"));
        entries.add(new PieEntry(ancleexData.count_best_l, "適正"));
        entries.add(new PieEntry(ancleexData.count_over_l, "超過"));


        PieDataSet dataSet = new PieDataSet(entries, "チャートのラベル");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setDrawValues(true);

        List<String> labels = Arrays.asList("角度不足", "適切", "角度超過");
        //PieData pieData = new PieData(labels, dataSet);
        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(Color.WHITE);

        mPieChartleftAncle.setData(pieData);
        mPieChartleftAncle.animateXY(1000, 1000); // 表示アニメーション
    }
    private void setupLineChartView(){

        // Grid背景色
        mChart.setDrawGridBackground(true);

        // no description text
        mChart.getDescription().setEnabled(true);

        // Grid縦軸を破線
        XAxis xAxis = mChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftAxis = mChart.getAxisLeft();
        // Y軸最大最小設定
        leftAxis.setAxisMaximum(150f);
        leftAxis.setAxisMinimum(0f);
        // Grid横軸を破線
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(true);

        // 右側の目盛り
        mChart.getAxisRight().setEnabled(false);

        // add data
        setData();

        mChart.animateX(1500);



        //mChart.invalidate();

        // dont forget to refresh the drawing
        // mChart.invalidate();

    }

    private void setupPieChartRightAncleView(AncleexData ancleexData) {
        mPieChartrightAncle.setUsePercentValues(true);
        //mPieChart.setDescription("チャートの説明");

        Legend legend = mPieChartrightAncle.getLegend();
        //legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        Description description = new Description();
        description.setTextColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        description.setText("右足");
        description.setTextSize(24);
        mPieChartrightAncle.setDescription(description);

        // 円グラフに表示するデータ
        //角度不足,適正,角度超過
        /*float under=  ancleexData.count_under_r;
        float best=  ancleexData.count_best_r;
        float over =  ancleexData.count_over_r;
        System.out.println("under:"+under+",best:"+best+",over:"+over);*/
        /*List<Float> values = Arrays.asList(under, best, over);
        //List<Entry> entries = new ArrayList<>();
        List<PieEntry> entries = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            //entries.add(new Entry(values.get(i), i));
            entries.add(new PieEntry(values.get(i), "テストラベル"));
        }*/
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(ancleexData.count_under_r, "不足"));
        entries.add(new PieEntry(ancleexData.count_best_r, "適正"));
        entries.add(new PieEntry(ancleexData.count_over_r, "超過"));


        PieDataSet dataSet = new PieDataSet(entries, "チャートのラベル");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setDrawValues(true);

        //List<String> labels = Arrays.asList("角度不足", "適切", "角度超過");
        //PieData pieData = new PieData(labels, dataSet);
        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(Color.BLACK);

        mPieChartrightAncle.setData(pieData);
        mPieChartrightAncle.animateXY(1000, 1000); // 表示アニメーション
    }
    private void setData() {
        // Entry()を使ってLineDataSetに設定できる形に変更してarrayを新しく作成
        int data[] = {116, 111, 112, 121, 102, 83,
                99, 101, 74, 105, 120, 112,
                109, 102, 107, 93, 82, 99, 110,
        };

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < data.length; i++) {
            values.add(new Entry(i, data[i], null, null));
        }

        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {

            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "DataSet");

            set1.setDrawIcons(false);
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(0f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            set1.setFillColor(Color.BLUE);

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData lineData = new LineData(dataSets);

            // set data
            mChart.setData(lineData);
        }
    }
}
