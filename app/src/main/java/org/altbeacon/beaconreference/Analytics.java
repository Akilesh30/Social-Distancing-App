package org.altbeacon.beaconreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;


public class Analytics extends AppCompatActivity {
    Deque<Integer> deque= new LinkedList<Integer>();
    public ArrayList<String> Days=new ArrayList<String>();
    FirebaseDatabase database;
    DatabaseReference reference;
    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences1;
    SharedPreferences.Editor editor;
    public static String phoneshared="";
    LineDataSet set1;
    private TextView dateTimeDisplay;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    Set<Entry> xygrphs= new HashSet<>();

    //
//    SharedPreferences sharedPreferences=getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);
//    final SharedPreferences.Editor editor=sharedPreferences.edit();
    GraphView graphView;
    RangingActivity rangingActivity=new RangingActivity();
    LineGraphSeries series;
    int distCount=0;
    int j=0;
    public static int xvalue =0;
    public static int yvalue=0;
    String date1;
    int date_int=0;
    Set<Integer> hash_Set = new HashSet<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd" );
        date = dateFormat.format(calendar.getTime());
        try {
            date_int = Integer.parseInt(date);
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }


//
       // if(date_int-7==Integer.valueOf(dR)){
       // }
        //Log.i("Date>>>>>>>>>>>>",date);
        // graphView = (GraphView) findViewById(R.id.chart);
//        series=new LineGraphSeries();
//        series.setAnimated(true);

        // graphView.addSeries(series);

            database = FirebaseDatabase.getInstance();

        reference = database.getReference("countTable");
        sharedPreferences=getSharedPreferences("Phone",MODE_PRIVATE);
        sharedPreferences1=getSharedPreferences("date",MODE_PRIVATE);
        date1=sharedPreferences1.getString("Date",null);
        editor=sharedPreferences.edit();
        phoneshared=sharedPreferences.getString("phoneshared",null);


        Log.d("inside","oncreate");
        reference.child(phoneshared).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //151
                Log.d("inside","reff");
                ArrayList<Entry> xyvalues = new ArrayList<>();
                LineChart mChart;

                DataPoint[] dp = new DataPoint[(int) snapshot.getChildrenCount()];
                int index=0;
                for(DataSnapshot myDataSnapshot : snapshot.getChildren()){
                    PointValue pointValue = myDataSnapshot.getValue(PointValue.class);
                    xvalue=Integer.valueOf(myDataSnapshot.getKey());
                    yvalue=pointValue.yValue;
                    xyvalues.add(new Entry(index,yvalue));
                    index++;
                    Log.i("xvaluesssss", String.valueOf(xvalue)+"      "+String.valueOf(yvalue));
                    hash_Set.add(xvalue);
                }

                ///////////////////////////////////////////////////////////////////////////////////////////

                if(hash_Set.size()>7){
                    for (int value : hash_Set) {
                        System.out.print(value + ", ");
                        hash_Set.remove(value);
                        reference.child(phoneshared).child(String.valueOf(value)).removeValue();
                        break;
                    }
                    System.out.println("dequeu>>"+hash_Set);
                }

                final ArrayList<String> xLabel = new ArrayList<>();
                for (int value : hash_Set) {
                    xLabel.add(String.valueOf(value));

                }
                mChart = findViewById(R.id.chart);


                //mChart.refreshDrawableState();
                mChart.animateXY(20,20);
                mChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xLabel));
                mChart.setTouchEnabled(false);
                mChart.setPinchZoom(false);
                mChart.setBackgroundColor(Color.WHITE);
                mChart.getXAxis().setDrawGridLines(false);
                mChart.getAxisLeft().setDrawGridLines(false);
                mChart.getAxisRight().setDrawGridLines(false);
                //mChart.setDrawMarkers(true);
                //MyMarkerView mv = new MyMarkerView(getApplicationContext(), R.layout.custom_marker_view);
                //mv.setChartView(mChart);
              //  mChart.setMarker(mv);


                XAxis xAxis = mChart.getXAxis();
                xAxis.enableGridDashedLine(10f, 265f, 0f);
                xAxis.setAxisLineColor(Color.BLACK);
                xAxis.setTextColor(Color.BLACK);
                xAxis.setAxisMaximum(7f);
               xAxis.setAxisMinimum(0f);
                xAxis.setDrawLimitLinesBehindData(true);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                YAxis leftAxis = mChart.getAxisLeft();
                xAxis.setAxisLineWidth(1f);
                leftAxis.setAxisLineWidth(1f);
                leftAxis.removeAllLimitLines();
                leftAxis.setAxisMaximum(80f);
                leftAxis.setAxisMinimum(0f);
                leftAxis.setAxisLineColor(Color.BLACK);
                leftAxis.setTextColor(Color.BLACK);
                leftAxis.setTextSize(10f);


                leftAxis.enableGridDashedLine(10f, 10f, 0f);
                leftAxis.setDrawZeroLine(false); 
                leftAxis.setDrawLimitLinesBehindData(false);
                mChart.getAnimation();
                mChart.getAxisRight().setEnabled(false);
                mChart.getXAxis().setDrawLabels(true);
                mChart.getLegend().setEnabled(false);
                mChart.getDescription().setEnabled(false);



//
//
//
//        ArrayList<Entry> values = new ArrayList<>();
//        values.add(new Entry(1, 50));
//        values.add(new Entry(2, 100));
//        values.add(new Entry(3, 80));
//        values.add(new Entry(4, 120));
//        values.add(new Entry(5, 110));
//        values.add(new Entry(7, 150));
//        values.add(new Entry(8, 250));
//        values.add(new Entry(9, 190));
                if (true) {

                    set1 = new LineDataSet(xyvalues, "Sample Data");
                    set1.setDrawIcons(false);
                    set1.enableDashedLine(10f, 0f, 0f);
                    set1.enableDashedHighlightLine(10f, 0f, 0f);
                    set1.setColor(Color.BLACK);
                    set1.setCircleColor(Color.DKGRAY);
                    set1.setLineWidth(2f);
                    set1.setCircleRadius(3f);
                    set1.setDrawCircleHole(false);
                    set1.setValueTextSize(15f);
                    set1.setDrawFilled(true);
                    set1.setValueTextColor(Color.BLACK);
                    set1.setFormLineWidth(5f);
                    set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                    set1.setFormSize(15.f);
                    if (Utils.getSDKInt() >= 18) {
                        Drawable drawable = ContextCompat.getDrawable(Analytics.this, R.drawable.fade_blue);
                        set1.setFillDrawable(drawable);
                    } else {
                        set1.setFillColor(Color.DKGRAY);
                    }
                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                    dataSets.add(set1);
                    LineData data = new LineData(dataSets);
                    mChart.setData(data);
                    set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
                    set1.setValues(xyvalues);
                    Log.d("xvaluess","going in if");
//            mChart.getData().notifyDataChanged();
//            mChart.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }

    public int loadData(int todaysday){
        SharedPreferences sharedPreferences = getSharedPreferences(String.valueOf(todaysday), Context.MODE_PRIVATE);
        distCount=sharedPreferences.getInt("Distance_Count",0);
        Log.i("String", String.valueOf(distCount ));
//        Toast.makeText(this,distCount,Toast.LENGTH_SHORT).show();

        return  distCount;

    }
    private boolean deleteArtist(String date) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference(phoneshared).child(date);

        //removing artist
        dR.removeValue();

        Toast.makeText(getApplicationContext(), "Artist Deleted", Toast.LENGTH_LONG).show();

        return true;
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                DataPoint[] dp = new DataPoint[(int) snapshot.getChildrenCount()];
//                int index=0;
//
//                for(DataSnapshot myDataSnapshot : snapshot.getChildren()){
//                   PointValue pointValue = myDataSnapshot.getValue(PointValue.class);
//                   xvalue=Integer.valueOf(myDataSnapshot.getKey());
//                   dp[index]=new DataPoint(xvalue,pointValue.yValue);
//                   index++;
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
private void setupGradient(LineChart mChart) {
    Paint paint = mChart.getRenderer().getPaintRender();
    int height = mChart.getHeight();

    LinearGradient linGrad = new LinearGradient(0, 0, 0, height,
            getResources().getColor(android.R.color.holo_green_light),
            getResources().getColor(android.R.color.holo_red_light),
            Shader.TileMode.REPEAT);
    paint.setShader(linGrad);
}
    private void setData() {




    }

}