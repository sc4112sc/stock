package com.example.ch06startactforresult;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.HashMap;
import java.util.LinkedList;

import static com.example.ch06startactforresult.StockGameActivity.*;

public class FragmentSell extends Fragment {

    private View myView;
    private ListView lvStocks;
    private MyAdapter myAdapter;
    private static LinkedList<HashMap<String,String>> data;
    private StockGameActivity stockGameActivity;
    private Button btnSell;
    private int itemTotal;
    private int[] sellList;
    private int[] posessList;
    private static String rawData;
    private static String account,playerName;
    private SystemVoice systemVoice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      //  if(myView==null) {
            myView = inflater.inflate(R.layout.fragment_sell, container, false);
            ((TextView)myView.findViewById(R.id.block)).setAlpha(0.8f);
            lvStocks = myView.findViewById(R.id.lvStocks);
            btnSell = myView.findViewById(R.id.btnSell);
            btnSell.setOnClickListener(sellClickListener);
            account = getStockGameActivity().getAccount();
            playerName=getStockGameActivity().getPlayerName();
           // TextView tvStockID = myView.findViewById(R.id.tvStockID);
         //   tvStockID.setText(getStockGameActivity().getStockID());
            //不需要
            /*
            Cursor cursor=null;
          //  cursor = dbHelper.queryPlayerData(getActivity().getIntent().getStringExtra("account"),
       //             getActivity().getIntent().getStringExtra("playerName"));
            cursor = dbHelper.queryPlayerData(account,playerName);
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                test.setText(cursor.getString(1));
            }
            */
            initListView();
            fetchRemoteData();
       // }
        systemVoice=new SystemVoice(getContext());

        ShimmerTextView shimmerTextView = myView.findViewById(R.id.shimmer_tv_sell);
        Shimmer shimmer = new Shimmer();
        shimmer.start(shimmerTextView);
        return myView;
    }

    private boolean isSellValidate(){
        itemTotal = lvStocks.getChildCount();
        if(itemTotal>0){
            posessList = new int[itemTotal];
            sellList = new int[itemTotal];
            int posses,sell;
            String temp;
            for(int i=0;i<itemTotal;i++){
                try{
                    temp = ((EditText)(lvStocks.getChildAt(i).findViewById(R.id.itemSellQ))).getText().toString();
                    posses = Integer.parseInt(((TextView)(lvStocks.getChildAt(i).findViewById(R.id.itemQuantity))).getText().toString());

                    posessList[i]=posses;
                    if(temp.equals("")){
                        sell=0;
                        sellList[i]=0;
                    }
                    else{
                        sell = Integer.parseInt(temp);
                        sellList[i]=sell;
                    }
                    if(sell>posses){
                        Toast.makeText(getContext(),getResources().getString(R.string.check_for_sell),Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
                catch (Exception e){e.printStackTrace();}
            }
        }
        return true;
    }


    private void sellStocks(){
        float sellPrice = getStockGameActivity().getCurrentSellPrice();
        int totalSell=getTotalSell();
        int cashAdder = (int)(sellPrice * totalSell);
        int cashTotal = getStockGameActivity().getCurrentCash() + cashAdder;
        String stocksList = getStocksList();



        dbHelper.updateSellOutcome(account,playerName,stocksList,cashTotal);

        getStockGameActivity().loadingLocalUserData();
        getStockGameActivity().notifyDataChange();
    }

    private void updateSellRecord(){
        for(int i=0;i<itemTotal;i++){
            if(posessList[i]-sellList[i]!=posessList[i]){
                String account = getStockGameActivity().getAccount();
                String playerName = getStockGameActivity().getPlayerName();
                String sellDate = getStockGameActivity().getCurrentDate();
                int sellQ = sellList[i];
                float sellP = getStockGameActivity().getCurrentSellPrice();
                String stockID = getStockGameActivity().getStockID();
                int transactionCount = getStockGameActivity().getTrasactionCount();

                ContentValues values = new ContentValues();
                values.put(DBHelper.COLUMN_ACCOUNT,account);
                values.put(DBHelper.COLUMN_PLAYER,playerName);
                values.put(DBHelper.COLUMN_RECORD_TYPE,"sell");
                values.put(DBHelper.COLUMN_RECORD_DATE,sellDate);
                values.put(DBHelper.COLUMN_RECORD_STOCKQ,sellQ);
                values.put(DBHelper.COLUMN_RECORD_STOCKP,sellP);
                values.put(DBHelper.COLUMN_STOCK_ID,stockID);
                values.put(DBHelper.COLUMN_RECORD_LOOP,transactionCount);
                getStockGameActivity().dbHelper.insertStockRecord(values);
            }

        }
    }


    private String getStocksList(){
        String stocksList="";
        resetStocksList();
        int itemTotal = lvStocks.getChildCount();
        for(int i=0;i<itemTotal;i++){
            if(posessList[i]!=0){
                String itemDate = ((TextView)lvStocks.getChildAt(i).findViewById(R.id.itemDate)).getText().toString();
                String itemQuantity = ((TextView)lvStocks.getChildAt(i).findViewById(R.id.itemQuantity)).getText().toString();
                String itemBuyPrice = ((TextView)lvStocks.getChildAt(i).findViewById(R.id.itemBuyPrice)).getText().toString();
                stocksList+=itemDate+ ":" + itemQuantity+","+itemBuyPrice;
                if(i!=itemTotal-1)
                    stocksList+=";";
            }
        }

        return stocksList;
    }

    private void resetStocksList(){
        int itemTotal = lvStocks.getChildCount();
        for(int i=0;i<itemTotal;i++){
            try{
                //posses = Integer.parseInt(((TextView)(lvStocks.getChildAt(i).findViewById(R.id.itemQuantity))).getText().toString());
              //  sell = Integer.parseInt(((EditText)(lvStocks.getChildAt(i).findViewById(R.id.etSellQ))).getText().toString());
                posessList[i]-=sellList[i];
                ((TextView)(lvStocks.getChildAt(i).findViewById(R.id.itemQuantity))).setText(""+posessList[i]);
                ((EditText)(lvStocks.getChildAt(i).findViewById(R.id.itemSellQ))).setText("");
            }
            catch (Exception e){e.printStackTrace();}
        }
    }

    private StockGameActivity getStockGameActivity(){
        return (StockGameActivity)getActivity();
    }

    private int getTotalSell(){
        int total=0;
        int itemTotal = lvStocks.getChildCount();
        //int adder;
        for(int i=0;i<itemTotal;i++){
            try{
               // adder = Integer.parseInt(((EditText)(lvStocks.getChildAt(i).findViewById(R.id.etSellQ))).getText().toString());
                total+=sellList[i];
            }
            catch (Exception e){e.printStackTrace();}
        }
        return total;
    }

    private boolean isAllZero(){
        int zeroCount=0;
        int itemTotal = lvStocks.getChildCount();
        for(int i=0;i<itemTotal;i++){
            EditText etSellQ = lvStocks.getChildAt(i).findViewById(R.id.itemSellQ);
            if(etSellQ.getText().toString().equals(""))
            { zeroCount++; }
            else if(!etSellQ.getText().toString().equals("")){
                int temp = Integer.parseInt(etSellQ.getText().toString());
                if(temp==0)
                    zeroCount++;
            }
        }
        if (zeroCount==itemTotal)
            return true;
        else
            return false;
    }
    private void sellDio(){

        AlertDialog.Builder dialog = new AlertDialog.Builder(myView.getContext(), R.style.MyAlertDialogTheme);
        dialog.setTitle("再次確認");
        dialog.setMessage("確定要賣出嗎");
        dialog.setNegativeButton("取消",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub

            }

        });
        dialog.setPositiveButton("確定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                doneDio();
                updateSellRecord();
                sellStocks();
            }

        });
        dialog.setCancelable(false);
        dialog.show();
    }
    private void doneDio(){

        AlertDialog.Builder dialog = new AlertDialog.Builder(myView.getContext(), R.style.MyAlertDialogTheme);
        dialog.setTitle("成功賣出");
        dialog.setPositiveButton("確定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub


            }

        });
        dialog.setCancelable(false);
        dialog.show();
    }
    private void noDio(){

        AlertDialog.Builder dialog = new AlertDialog.Builder(myView.getContext(), R.style.MyAlertDialogTheme);
        dialog.setTitle("請輸入股數");

        dialog.setPositiveButton("確定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub


            }

        });
        dialog.setCancelable(false);
        dialog.show();
    }


    OnClickListener sellClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!isAllZero() && isSellValidate()){
                sellDio();
            }else {
                noDio();
            }




/*aaa: buy,2019/05/06,100,86.76,QCOM,1
2019-12-16 13:12:21.845 16028-16028/com.example.ch06startactforresult V/aaa: buy,2019/05/07,100,83.76,QCOM,1
2019-12-16 13:12:21.846 16028-16028/com.example.ch06startactforresult V/aaa: sell,2019/05/07,100,83.76,QCOM,1
2019-12-16 13:12:21.846 16028-16028/com.example.ch06startactforresult V/aaa: sell,2019/05/08,100,83.11,QCOM,1
            int itemTotal = lvStocks.getChildCount();
            if(itemTotal>0){
                for(int i=0;i<itemTotal;i++) {
                    View item = lvStocks.getChildAt(i);
                    EditText etSell = item.findViewById(R.id.etSellQ);
                    TextView itemQuantity = item.findViewById(R.id.itemQuantity);
                    itemQuantity.setText(etSell.getText().toString());
                }

            }*/
            systemVoice.ButtonTouchVoice();
        }
    };



    public void sellNotifyChange(){
        if(data!= null && myAdapter!= null){
        data.clear();
        fetchRemoteData();
        }
    }

    public void fetchRemoteData(){
        cursor=null;
        rawData="";
        cursor = dbHelper.queryPlayerData(account,playerName);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            rawData = cursor.getString(6);
            Log.v("aaa","rawData"+rawData);
        }
        if(rawData!=""){
            String[] root = rawData.split(";");
            try{
                String temp[];
                String temp2[];
                for (int i=0;i<root.length;i++){
                    HashMap<String,String> dd = new HashMap<>();
                     temp=root[i].split(":");
                    dd.put("Date",temp[0]);
                    temp2=temp[1].split(",");
                    dd.put("Quantity",temp2[0]);
                    dd.put("Price",temp2[1]);
                    data.add(dd);
                    Log.v("aaa","data"+data.toString());
                }
                myAdapter.notifyDataSetChanged();

            }catch (Exception e){e.printStackTrace();}
        }
    }

    public void initListView(){
        myAdapter = new MyAdapter();
        data = new LinkedList<>();
        lvStocks.setAdapter(myAdapter);
        lvStocks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.item_stocks,null);
            TextView tvDate = view.findViewById(R.id.itemDate);
            tvDate.setText(data.get(position).get("Date"));
            TextView tvQuantity = view.findViewById(R.id.itemQuantity);
            tvQuantity.setText(data.get(position).get("Quantity"));
            TextView tvBuyPrice = view.findViewById(R.id.itemBuyPrice);
            tvBuyPrice.setText(data.get(position).get("Price"));
            EditText itemSellQ = view.findViewById(R.id.itemSellQ);
            itemSellQ.setText("0");

            return view;
        }
    }
}
