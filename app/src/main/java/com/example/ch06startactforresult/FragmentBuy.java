package com.example.ch06startactforresult;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.HashMap;
import java.util.LinkedList;
import static com.example.ch06startactforresult.StockGameActivity.*;

public class FragmentBuy extends Fragment {

    private View myView;
    private Button btnBuy;
    private EditText etBuyQ;
    private AlertDialog.Builder builder;
    private ListView lvStocks;
    private MyAdapter myAdapter;
    private static final int loadNumber=5;

    private static LinkedList<HashMap<String,String>> data;

    private SystemVoice systemVoice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //if(myView==null) {
            myView = inflater.inflate(R.layout.fragment_buy, container, false);
            ((TextView)myView.findViewById(R.id.block)).setAlpha(0.7f);
            lvStocks = myView.findViewById(R.id.lvStocks);
            btnBuy = myView.findViewById(R.id.btnBuy);
            btnBuy.setOnClickListener(buyClickListener);
            etBuyQ = myView.findViewById(R.id.etBuyQ);
          //  TextView tvStockID = myView.findViewById(R.id.tvStockID);
        //    tvStockID.setText(getStockGameActivity().getStockID());

            initListView();
            fetchRemoteData();

            builder = new AlertDialog.Builder(getContext(),R.style.MyAlertDialogTheme);
   //    }
        systemVoice=new SystemVoice(getContext());

        ShimmerTextView shimmerTextView = myView.findViewById(R.id.shimmer_tv_buy);
        Shimmer shimmer = new Shimmer();
        shimmer.start(shimmerTextView);
        return myView;
    }

    private void buyStock(int cost){

        getStockGameActivity().cursor = null;
        int transactionCount = 0;
        String account = getStockGameActivity().getAccount();
        String playerName = getStockGameActivity().getPlayerName();
        cursor = getStockGameActivity().dbHelper.queryPlayerData(account,playerName);
        boolean isInLoop =true;
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            isInLoop = Boolean.valueOf(cursor.getString(9));
            transactionCount = cursor.getInt(10);
            if(!isInLoop){
                if(cursor.getString(6).equals("")){
                    transactionCount++;
                    isInLoop = !isInLoop ;
                }
            }
        }

        String stocksList = formStocksList(isBuyingToday());
        int currentCash = getStockGameActivity().getCurrentCash();
        int totalCash = currentCash - cost;


        getStockGameActivity().dbHelper.updateBuyOutCome(
                account,playerName,stocksList,totalCash,transactionCount,isInLoop);
        getStockGameActivity().dbHelper.insertStockRecord(formBuyContentValue(cursor,transactionCount));
        etBuyQ.setText("");
        getStockGameActivity().loadingLocalUserData();



    }

    private ContentValues formBuyContentValue(Cursor cursor,int transactionLoop){
        ContentValues values = new ContentValues();
        String account = cursor.getString(0);
        String playerName = cursor.getString(1);
        String buyDate = cursor.getString(8);
        String buyStockId = cursor.getString(5);
        int buyStockQ =  Integer.parseInt(etBuyQ.getText().toString());
        float buyStockP= getStockGameActivity().getCurrentBuyPrice();

        values.put(DBHelper.COLUMN_ACCOUNT,account);
        values.put(DBHelper.COLUMN_PLAYER,playerName);
        values.put(DBHelper.COLUMN_RECORD_TYPE,"buy");
        values.put(DBHelper.COLUMN_RECORD_DATE,buyDate);
        values.put(DBHelper.COLUMN_RECORD_STOCKQ,buyStockQ);
        values.put(DBHelper.COLUMN_RECORD_STOCKP,buyStockP);
        values.put(DBHelper.COLUMN_RECORD_STOCKID,buyStockId);
        values.put(DBHelper.COLUMN_TRANSACTION_COUNT,transactionLoop);

        return values;
    }

    private void showAlertMessage(){
        int buyQ = Integer.parseInt(etBuyQ.getText().toString());
        final int cost = (int)(buyQ * getStockGameActivity().getCurrentBuyPrice());
        builder.setTitle(getResources().getString(R.string.BUYING) + "股票")//+getStockGameActivity().getStockID())
                .setMessage(getResources().getString(R.string.BUYING)+" "+buyQ+
                        getResources().getString(R.string.UNIT)+"  現金$→"+cost+" ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        buyStock(cost);
                        buyDio();
                    }
                })
                .setNegativeButton("No",null)
                .setCancelable(false)
                .show();

    }
    private void buyDio(){

        AlertDialog.Builder dialog = new AlertDialog.Builder(myView.getContext(), R.style.MyAlertDialogTheme);
        dialog.setTitle("成功買入");
        dialog.setPositiveButton("確定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub


            }

        });
        dialog.setCancelable(false);
        dialog.show();
    }

    OnClickListener buyClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if(isBuyValidate()){
                showAlertMessage();
            }
            else{
                Toast.makeText(getContext(),getResources().getString(R.string.NOT_ENOUGH_CASH),Toast.LENGTH_SHORT).show();
            }
            systemVoice.ButtonTouchVoice();
        }
    };

    private String formStocksList(boolean isBuyToday){
        int buyQ =0;
        if(!etBuyQ.getText().toString().equals(""))
            buyQ = Integer.parseInt(etBuyQ.getText().toString());
        String stocksList="";
        String price;
        String rawData = getStockGameActivity().getStocksList();
        if(!rawData.equals("")){
            String[] list = rawData.split(";");
            if(isBuyToday){
                for(int i=0;i<list.length-1;i++){
                    stocksList+=list[i]+";";
                }
                String[] temp =list[list.length-1].split(":");
                String stockDate = temp[0];
                String stockData = temp[1];
                int currentQ = Integer.parseInt(stockData.split(",")[0]);
                price = stockData.split(",")[1];
                int totalBuyQ = currentQ + buyQ;
                stocksList += stockDate+":"+totalBuyQ+","+price;
            }
            else{
                for(int i=0;i<list.length;i++){
                    stocksList+=list[i]+";";
                }
                String today = getStockGameActivity().getCurrentDate();
                price = String.valueOf(getStockGameActivity().getCurrentBuyPrice());
                stocksList += today+":"+buyQ+","+price;
            }
        }
        else {
            String today = getStockGameActivity().getCurrentDate();
            price = String.valueOf(getStockGameActivity().getCurrentBuyPrice());
            stocksList += today+":"+buyQ+","+price;
        }

        return stocksList;
    }

    private boolean isBuyValidate(){
        int cash = getStockGameActivity().getCurrentCash();
        float currentBuyPrice = getStockGameActivity().getCurrentBuyPrice();
        int buyQ = 0;
        if(!etBuyQ.getText().toString().equals(""))
           buyQ = Integer.parseInt(etBuyQ.getText().toString());
        else
            return false;

        if(cash>((int)(buyQ*currentBuyPrice)))
            return true;
        else
            return false;
    }

    private boolean isBuyingToday(){
        String rawData = getStockGameActivity().getStocksList();
        String currentDate = getStockGameActivity().getCurrentDate();
        String[] list = rawData.split(";");
        String[] stockData = list[list.length-1].split(":");
        String lastBuyingDate = stockData[0];
        if(currentDate.equals(lastBuyingDate))
            return true;
        else
            return false;

    }

    private StockGameActivity getStockGameActivity(){
        return (StockGameActivity)getActivity();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void fetchRemoteData(){
        int index=0;
        cursor=null;
        cursor = dbHelper.queryAllStockData();
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            while (!cursor.isLast())
            {
                if(cursor.getString(0).equals(currentDate)){
                    index = cursor.getPosition();
                    break;
                }
                cursor.moveToNext();
            }
        }
        cursor.moveToPosition(index);
        for(int i=0;i<loadNumber;i++){
            HashMap<String,String> dd = new HashMap<>();
            dd.put(DBHelper.COLUMN_DATE,cursor.getString(0));
            dd.put(DBHelper.COLUMN_OPEN,cursor.getString(1));
            dd.put(DBHelper.COLUMN_HIGH,cursor.getString(2));
            dd.put(DBHelper.COLUMN_LOW,cursor.getString(3));
            dd.put(DBHelper.COLUMN_CLOSE,cursor.getString(4));
            data.add(dd);
            cursor.move(-1);
        }
        myAdapter.notifyDataSetChanged();

    }

    public void buyNotifyChange(){
        if(data!= null && myAdapter!= null){
        data.clear();
        fetchRemoteData();
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
            View view = inflater.inflate(R.layout.item_stock_detail,null);
            TextView itemDate = view.findViewById(R.id.itemDate);
            itemDate.setText(data.get(position).get(DBHelper.COLUMN_DATE));
            TextView itemOpen = view.findViewById(R.id.itemOpen);
            itemOpen.setText(data.get(position).get(DBHelper.COLUMN_OPEN));
            TextView itemHigh = view.findViewById(R.id.itemHigh);
            itemHigh.setText(data.get(position).get(DBHelper.COLUMN_HIGH));
            TextView itemLow = view.findViewById(R.id.itemLow);
            itemLow.setText(data.get(position).get(DBHelper.COLUMN_LOW));
            TextView itemClose = view.findViewById(R.id.itemClose);
            itemClose.setText(data.get(position).get(DBHelper.COLUMN_CLOSE));

            return view;
        }
    }
}
