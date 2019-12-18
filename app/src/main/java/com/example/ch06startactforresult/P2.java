package com.example.ch06startactforresult;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import static com.example.ch06startactforresult.P1.*;
import static com.example.ch06startactforresult.ControlPanelActivity.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class P2 extends Fragment {

    private TextView tvProfitPercentMax,tvTotalProfit,tvCurrentProfitPercent,tvCurrentState;
    private View mainView;

    public P2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_p2, container, false);
        // Inflate the layout for this fragment

        tvProfitPercentMax = mainView.findViewById(R.id.tvProfitPercentMax);
        tvTotalProfit = mainView.findViewById(R.id.tvTotalProfit);
        tvCurrentProfitPercent = mainView.findViewById(R.id.tvCurrentProfitPercent);
        tvCurrentState = mainView.findViewById(R.id.tvCurrentState);
        String temp="";
        if(totalTransaction!=0){
            temp = "最高獲利率\n"+getProfitPercentMax()+"%";
            tvProfitPercentMax.setText(temp);
            temp="總獲利金額\n"+getTotalProfit()+"美元";
            tvTotalProfit.setText(temp);
            temp="目前獲利率\n"+profitPercent[totalTransaction-1]+"%";
            tvCurrentProfitPercent.setText(temp);
            temp="目前資產\n"+getCurrentState()+"美元";
            tvCurrentState.setText(temp);
        }
        else {
            tvProfitPercentMax.setText("最高獲利率\n0%");
            tvTotalProfit.setText("總獲利金額\n0美元");
            tvCurrentProfitPercent.setText("目前獲利率\n0%");
            tvCurrentState.setText("目前資產\n0美元");
        }


        return mainView;
    }

    private int getCurrentState(){
        int currentState = 0;
     //   Log.v("aaa","STOCKLIST"+stocksList);
        if(stocksList!=null &&!stocksList.equals("") ){
            String[] list = stocksList.split(";");
            for(int i=0;i<list.length;i++)
            {
                String[] temp2 = list[i].split(":");
                String[] detail = temp2[1].split(",");
                int stockQ = Integer.parseInt(detail[0]);
                currentState += (int)(stockQ *  Float.valueOf(currentSellPrice));
            }
        }
        currentState += playerCash;
        return currentState;
    }


    private int getTotalProfit(){
        int totalProfit = 0;
        for(int i=0;i<profitSituation.length;i++)
            totalProfit += profitSituation[i];
        return totalProfit;
    }

    private float getProfitPercentMax(){
        float profitPercentMax = 0.00f;
        for(int i=0;i<profitPercent.length;i++){
            if (profitPercent[i]>profitPercentMax)
                profitPercentMax = profitPercent[i];
        }
        return profitPercentMax;
    }



}
