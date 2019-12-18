package com.example.ch06startactforresult;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.LinkedList;

public class HistroyActivity extends AppCompatActivity {
    private int[] roles;
    private Cursor cursor;
    private Cursor cursor2;
    private DBHelper dbHelper;
    private SharedPreferences preferences;



    private ImageView iconView;
    private TextView getView;
    private TextView loseView;
    private TextView dateView;
    private TextView cashView;
    private TextView nameView;

    private String account;
    private String playerName;
    private String date;
    private String lose;
    private String get;
    private String cash;
    private String icon;



    private ListView listView;
    private SimpleAdapter adapter;
    private LinkedList<HashMap<String,?>> data;
    private  String[] from = {"name","cash","get","lose","date","icon"};
    private  int[] to = {R.id.name_h,R.id.cash_h,R.id.get_h,R.id.lose_h,R.id.date_h,R.id.headIcon_h};

    private SystemVoice systemVoice;
    private BgmClass bgmClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histroy);

        bgmClass=new BgmClass(this);
        systemVoice=new SystemVoice(this);

        dbHelper = new DBHelper(this);

        preferences = getSharedPreferences("localSetting",MODE_PRIVATE);
        account = preferences.getString("account","");
        //playerName = preferences.getString("playerName","");




        roles = new int[]{R.drawable.h1, R.drawable.h2,R.drawable.h3,R.drawable.h4,R.drawable.h5
                ,R.drawable.h6,R.drawable.h7,R.drawable.h8,R.drawable.h9,R.drawable.h10};




        listView = findViewById(R.id.list_h);
        initListView();




        queryData();

    }

    private  void initListView(){
        data = new LinkedList<>();
        adapter = new SimpleAdapter(this, data,R.layout.history_item,from,to);
        listView.setAdapter(adapter);

    }

    private void putIn(String name,String cash,String get,String lose,String date,String icon){
        int myIcon = Integer.valueOf(icon);
        HashMap itemData = new HashMap();
        itemData.put(from[0],name);
        itemData.put(from[1],cash);
        itemData.put(from[2],get);
        itemData.put(from[3],lose);
        itemData.put(from[4],date);
        itemData.put(from[5],roles[myIcon]);
        data.add(0,itemData);



        adapter.notifyDataSetChanged();

    }

    private void queryData(){
        cursor=null;
        cursor = dbHelper.queryAllPlayerData(account);
        if(cursor!=null){
            cursor.moveToFirst();

            while (!cursor.isAfterLast()){


                    String playerName2 = cursor.getString(1);
                    String iconID2 = String.valueOf(cursor.getInt(2));
                    int cash2 = cursor.getInt(3);

                    queryRecordData(account,playerName2,iconID2,cash2);

                    cursor.moveToNext();


            }

        }

    }

    private void queryRecordData(String account,String playerName,String icon,int cash){
        cursor2 = null;
        cursor2 = dbHelper.queryTransactionRecord(account,playerName);
        Log.v("aaa","!!!"+cursor2.getCount());

        if(cursor2.getCount()>0){
            cursor2.moveToFirst();
            for(int i=0;i<cursor2.getCount();i++){
                Log.v("aaa","!!!"+cursor2.getString(3)+","+cursor2.getString(4)+","+cursor2.getString(5)+
                        ","+cursor2.getString(6)+","+cursor2.getString(7)+","+cursor2.getString(8));

                int rand = (int)(Math.random()*80+1);
                int rand2 = (int)(Math.random()*80+1);
                date =cursor2.getString(4);
                putIn(playerName,""+cash,rand+"%",rand2+"%",date,icon);
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        bgmClass.BGM_historyStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bgmClass.BGMDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bgmClass.BGMPause();

    }
}
