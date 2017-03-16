package com.ljdc.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.j256.ormlite.dao.Dao;
import com.ljdc.R;
import com.ljdc.activitys.MainActivity;
import com.ljdc.activitys.WordExamActivity;
import com.ljdc.app.App;
import com.ljdc.database.DBHelper;
import com.ljdc.pojo.WordDevelopmentServer;
import com.ljdc.utils.Act;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Describe: TODO 复习
 * * * * ****** Created by ZOUXU ********
 */
public class ReviewFragment extends Fragment implements OnClickListener {

    // 标题栏
    TextView title;
    long weekMills;//一周间隔时间：millseconds
    long dayMills;
    private View content = null;
    private LineChart lineChart;
    private DBHelper dbHelper = null;
    private Date dateWeekAgo;
    private Date dateNow;
    private Date[] oneWeekDate;
    private List<WordDevelopmentServer> secondLevels;
    private List<WordDevelopmentServer> thirdLevels;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = DBHelper.getHelper(getActivity());
        weekMills = 7 * 24 * 60 * 60 * 1000;
        dayMills = 24 * 60 * 60 * 1000;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        content = inflater.inflate(R.layout.fragment_review, null);
        initView(inflater);
        Log.i("info", "onCreateView: --------------");
        return content;

    }

    /**
     * <b>只在</b>ViewPager中切换时调用
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i("info", "setUserVisibleHint: --------------" + isVisibleToUser);
        if (isVisibleToUser && App.WORDDEV_CHANGED) {
            Log.i("info", "setUserVisibleHint: --------------初始化数据");
            dateNow = new Date();
            dateWeekAgo = new Date(dateNow.getTime() - weekMills);
            if (oneWeekDate == null) {
                oneWeekDate = new Date[7];
            }
            for (int i = 0; i < 7; i++) {
                oneWeekDate[i] = new Date(dateWeekAgo.getTime() + dayMills * i);
            }

            initData();
            initLineChart(lineChart);
            App.WORDDEV_CHANGED = false;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("info", "onStart: --------------");
        dateNow = new Date();
        dateWeekAgo = new Date(dateNow.getTime() - weekMills);
        if (oneWeekDate == null) {
            oneWeekDate = new Date[7];
        }
        for (int i = 0; i < 7; i++) {
            oneWeekDate[i] = new Date(dateWeekAgo.getTime() + dayMills * i);
        }

        initData();
        initLineChart(lineChart);
    }

    private void initView(LayoutInflater inflater) {

        title = ((MainActivity) getActivity()).title;
        title.setText("复习");

        content.findViewById(R.id.wrodFirstExam).setOnClickListener(this);
        content.findViewById(R.id.wrodSecondExam).setOnClickListener(this);
        content.findViewById(R.id.wrodThirdExam).setOnClickListener(this);

        lineChart = (LineChart) content.findViewById(R.id.lineChart);
//        initLineChart(lineChart);
    }

    private void initData() {
        try {
            Dao dao = dbHelper.getDao(WordDevelopmentServer.class);
            //熟词 graspLevel:2
            secondLevels = dao.queryBuilder().orderBy("wordIncreaseDate", true).where().eq("graspLevel", 2).and().between("wordIncreaseDate", dateWeekAgo, dateNow).query();
            Log.i("info", "initData: " + secondLevels.size());
            thirdLevels = dao.queryBuilder().orderBy("wordIncreaseDate", true).where().eq("graspLevel", 3).and().between("wordIncreaseDate", dateWeekAgo, dateNow).query();
            Log.i("info", "initData: " + thirdLevels.size());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initLineChart(LineChart chart) {
        chart.setDescription("");
        chart.setDrawGridBackground(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setLabelCount(5);

//        折线图 右坐标轴
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
//        rightAxis.setLabelCount(5);
//        rightAxis.setDrawGridLines(false);

        // set data
        chart.setData(generateDataLineData());

        // do not forget to refresh the chart
        // chart.invalidate();
        chart.animateX(750);
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private LineData generateDataLineData() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //实际数据
        ArrayList<Entry> e1 = new ArrayList<Entry>();

        for (int i = 0; i < 7; i++) {
            e1.add(new Entry(0, i));
        }
        Entry entry;
        for (WordDevelopmentServer data : secondLevels) {
            for (int i = 0; i < 7; i++) {
                if (sdf.format(data.wordIncreaseDate).equals(sdf.format(oneWeekDate[i]))) {
                    entry = e1.get(i);
                    entry.setVal(data.wordsIncreaseNum);
                }
            }
        }

        //折线数据1
        LineDataSet d1 = new LineDataSet(e1, "熟词量");
        d1.setLineWidth(2.5f);
        d1.setCircleSize(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(true);

        //实际数据
        ArrayList<Entry> e2 = new ArrayList<Entry>();

        for (int i = 0; i < 7; i++) {
            e2.add(new Entry(0, i));
        }
        Entry entry2;
        for (WordDevelopmentServer data : thirdLevels) {
            for (int i = 0; i < 7; i++) {
                if (sdf.format(data.wordIncreaseDate).equals(sdf.format(oneWeekDate[i]))) {
                    entry2 = e2.get(i);
                    entry2.setVal(data.wordsIncreaseNum);
                }
            }
        }

        //折线数据2
        LineDataSet d2 = new LineDataSet(e2, "词汇量");
        d2.setLineWidth(2.5f);
        d2.setCircleSize(4.5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setDrawValues(true);

        //折线图折线数据集合
        ArrayList<LineDataSet> sets = new ArrayList<LineDataSet>();
        sets.add(d1);
        sets.add(d2);

        LineData cd = new LineData(getMonths(), sets);
        return cd;
    }

    /**
     * X轴 日期 7天内
     *
     * @return
     */
    private ArrayList<String> getMonths() {

        ArrayList<String> m = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        for (int i = 0; i < 7; i++) {
            m.add(sdf.format(oneWeekDate[i]));
        }
        return m;
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.wrodFirstExam:
                Toast.makeText(getContext(), "FIRST", Toast.LENGTH_SHORT).show();
                Act.toAct(getContext(), WordExamActivity.class);
                break;
            case R.id.wrodSecondExam:
                Toast.makeText(getContext(), "second", Toast.LENGTH_SHORT).show();
                Act.toAct(getContext(), WordExamActivity.class);
                break;
            case R.id.wrodThirdExam:
                Toast.makeText(getContext(), "third", Toast.LENGTH_SHORT).show();
                Act.toAct(getContext(), WordExamActivity.class);
                break;
        }
    }
}
