package com.example.ch06startactforresult;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.fragment.app.Fragment;

import static com.example.ch06startactforresult.ControlPanelActivity.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class P1 extends Fragment {
    private View mainView;
    private ViewFlipper viewFlipper;
    private TextView tvTotalTransaction,tvWinCount,tvLoseCount,tvWinPercent,tvLoseMaxPercent;
    private String account,playerName;
    public static int totalTransaction,winCount;
    public static int[] transactionBuy;
    public static int[] transactionSell;
    public static int[] profitSituation;
    public static float[] profitPercent;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        account = preferences.getString("account","");
        playerName = preferences.getString("playerName","");
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_p1, container, false);

        tvTotalTransaction =  mainView.findViewById(R.id.tvTotalTransaction);
        totalTransaction = getTotalTransaction();
        String temp = "總交易次數\n"+totalTransaction+"次";
        tvTotalTransaction.setText(temp);

        winCount = getWinCount();
        tvWinCount = mainView.findViewById(R.id.tvWinCount);
        temp = "獲利次數\n"+winCount+"次";
        tvWinCount.setText(temp);

        tvLoseCount = mainView.findViewById(R.id.tvLoseCount);
        temp = "失敗次數\n"+(totalTransaction-winCount)+"次";
        tvLoseCount.setText(temp);

        tvWinPercent = mainView.findViewById(R.id.tvWinPercent);

        if(totalTransaction!=0)
            temp = "勝率\n"+(int)(((float)winCount/totalTransaction)*100)+"%";
        else{ temp = "勝率\n0%";}
        tvWinPercent.setText(temp);

        tvLoseMaxPercent = mainView.findViewById(R.id.tvLoseMaxPercent);
        if(totalTransaction!=0)
            temp = "最高虧損率\n"+getMaxMinusPercent()+"%" ;
        else { temp = "最高虧損率\n0%" ;}
        tvLoseMaxPercent.setText(temp);



        return mainView;
    }

    private int getTotalTransaction(){
        int totalTransaction = 0;
        cursor = null;
        cursor = dbHelper.queryTransactionRecord(account,playerName);
        if(cursor.getCount()>0) {
            cursor.moveToLast();
            totalTransaction = cursor.getInt(8);
        }

        if(totalTransaction != 0){
            transactionBuy = new int[totalTransaction];
            transactionSell = new int[totalTransaction];
            profitSituation = new int[totalTransaction];
            profitPercent = new float[totalTransaction];
        }
        return totalTransaction;
    }

    private float getMaxMinusPercent()
    {
        float minusMax = 0.0000f;
        for(int i=0;i<profitPercent.length;i++){
            if (profitPercent[i]<minusMax)
                minusMax = profitPercent[i];
        }
        return minusMax;
    }

    private int getWinCount(){
        if(totalTransaction==0)
            return 0;
        int winCount = 0;
        int totalBuy = 0;
        int totalSell = 0;
        cursor = null;
        cursor = dbHelper.queryTransactionRecord(account,playerName);
        if(cursor.getCount()>0)
        {
            cursor.moveToFirst();
            for(int i=0;i<totalTransaction;i++){
                totalBuy=0;
                totalSell=0;
                while (!cursor.isAfterLast() && cursor.getInt(8)==i+1 )
                {
                    if(cursor.getString(3).equals("buy")){
                        totalBuy += (int)(cursor.getInt(5)*cursor.getFloat(6));
                    }
                    else{
                        totalSell += (int)(cursor.getInt(5)*cursor.getFloat(6));
                    }
                        cursor.moveToNext();
                }
                if(totalSell>totalBuy)
                    winCount++;
                transactionBuy[i] = totalBuy;
                transactionSell[i] = totalSell;
                profitSituation[i] = totalSell - totalBuy ;
                profitPercent[i] = (((float)profitSituation[i]/20000)*100);
            }
        }
        return winCount;
    }


//    private class MyClickListener implements View.OnClickListener{
//        @Override
//        public void onClick(View v){
//            viewFlipper.showNext();
//        }
//    }
}