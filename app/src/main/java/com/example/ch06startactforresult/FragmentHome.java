package com.example.ch06startactforresult;

import android.database.Cursor;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.HighLowDataEntry;
import com.anychart.charts.Stock;
import com.anychart.core.stock.Plot;
import com.anychart.data.Table;
import com.anychart.data.TableMapping;
import com.anychart.enums.StockSeriesType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.ch06startactforresult.StockGameActivity.*;

public class FragmentHome extends Fragment {

    private View myView;
    private Cursor cursor;
    private static int offset = -59;
    private static int number = 60;
    private List<DataEntry> data;


    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //if(myView==null) {
            myView = inflater.inflate(R.layout.fragment_home, container, false);
            AnyChartView anyChartView = myView.findViewById(R.id.any_chart_view);
            anyChartView.setProgressBar(myView.findViewById(R.id.progress_bar));
            data = new ArrayList<>();

            Table table = Table.instantiate("x");
            table.addData(getData());

            TableMapping mapping = table.mapAs("{open: 'open', high: 'high', low: 'low', close: 'close'}");

            Stock stock = AnyChart.stock();

            Plot plot = stock.plot(0);
            plot.yGrid(true)
                    .xGrid(true)
                    .yMinorGrid(true)
                    .xMinorGrid(true);

            plot.ema(table.mapAs("{value: 'close'}"), 20d, StockSeriesType.LINE);

            plot.ohlc(mapping)
                    .name("CSCO")
                    .legendItem("{\n" +
                            "        iconType: 'rising-falling'\n" +
                            "      }");

            stock.scroller().ohlc(mapping);

            anyChartView.setChart(stock);
    //    }
       
        return myView;//super.onCreateView(inflater, container, savedInstanceState);
    }

    private StockGameActivity getStockGameActivity(){
        return (StockGameActivity)getActivity();
    }
    public void homeNotifyChange(){
        fragmentHome = new FragmentHome();
        transaction.replace(R.id.flContent,fragmentHome);
        transaction.commit();

        /*
     //   if(myView!= null && data!= null){
       //     data.clear();
            AnyChartView anyChartView = myView.findViewById(R.id.any_chart_view);
            anyChartView.setProgressBar(myView.findViewById(R.id.progress_bar));
            Table table = Table.instantiate("x");
            table.addData(getData());

            TableMapping mapping = table.mapAs("{open: 'open', high: 'high', low: 'low', close: 'close'}");

            Stock stock = AnyChart.stock();

            Plot plot = stock.plot(0);
            plot.yGrid(true)
                    .xGrid(true)
                    .yMinorGrid(true)
                    .xMinorGrid(true);

            plot.ema(table.mapAs("{value: 'close'}"), 20d, StockSeriesType.LINE);

            plot.ohlc(mapping)
                    .name("CSCO")
                    .legendItem("{\n" +
                            "        iconType: 'rising-falling'\n" +
                            "      }");

            stock.scroller().ohlc(mapping);

            anyChartView.setChart(stock);
            transaction.replace(R.id.flContent,fragmentHome);
            transaction.commit();*/
      //  }
    }
    private List<DataEntry> getData() {
        String today ;
        double open,high,low,close;

        cursor = null;
        cursor = getStockGameActivity().dbHelper.queryAllStockData();
        today = currentDate;
        int index=0;
        if(cursor.getCount()>0)
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                if(cursor.getString(0).equals(today))
                {
                    index = cursor.getPosition();

                    break;
                }
                cursor.moveToNext();
            }
        }
        for(int i=0;i<number;i++){
            Long temp=0L;
            cursor.moveToPosition(index+offset+i);
            today = cursor.getString(0);
            //   Log.v("aaa",""+currentDate);
            open = cursor.getDouble(1);
            high = cursor.getDouble(2);
            low = cursor.getDouble(3);
            close = cursor.getDouble(4);


            Calendar calendar = Calendar.getInstance();
            DateFormat df = DateFormat.getDateInstance();
           // SimpleDateFormat df = new SimpleDateFormat("yyyy/mm/dd");
            Date date;
            try{
                date = df.parse(today);
                calendar.setTime(date);
            }
            catch (Exception e){e.printStackTrace();}


            temp = calendar.getTimeInMillis();
            //  Log.v("aaa",""+temp);

            data.add(new OHCLDataEntry(temp,open,high,low,close));

        }

        return data;
    }

    private class OHCLDataEntry extends HighLowDataEntry {
        OHCLDataEntry(Long x, Double open, Double high, Double low, Double close) {
            super(x, high, low);
            setValue("open", open);
            setValue("close", close);
        }
    }
}
